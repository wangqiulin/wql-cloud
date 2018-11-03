package com.wql.cloud.basic.elasticsearch.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

/**
 * 
 * @author wangqiulin
 *
 */
//@Component
public class ElasticSearchUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ElasticSearchUtil.class);
	
	@Autowired
	private Client client;
	
	/**
	 * 单个添加
	 * @param indexName
	 * @param type
	 * @param docId
	 * @param jsonStr
	 * @return
	 */
	public boolean addSingle(String indexName, String type, String docId, String jsonStr){
		long start = System.currentTimeMillis();
		IndexResponse response = null;
		if(docId == null){
			//response = client.prepareIndex(indexName, type).setId(docId).setSource(jsonStr).execute().actionGet();
			response = client.prepareIndex(indexName, type).setSource(jsonStr, XContentType.JSON).get();
		}else{
			response = client.prepareIndex(indexName, type, docId).setSource(jsonStr, XContentType.JSON).get();
		}
		RestStatus status = response.status();
		logger.info("elasticsearch---插入耗时：{} ms", System.currentTimeMillis() - start);
		return status.getStatus() == 201;
	}
	
	/**
	 * 修改文档
	 * @param indexName
	 * @param type
	 * @param docId
	 * @param jsonStr
	 * @return
	 */
	public boolean updateDoc(String indexName, String type, String docId, String jsonStr){
		try {
			UpdateRequest updateRequest = new UpdateRequest();
			updateRequest.index(indexName);
			updateRequest.type(type);
			updateRequest.id(docId);
			updateRequest.doc(jsonStr, XContentType.JSON);
			String strResult = client.update(updateRequest).get().getResult().toString();
			return strResult == "UPDATED";
		} catch (Exception e) {
			logger.error("索引记录，修改失败", e);
		} 
		return false;
	}
	
	/**
	 * 删除文档
	 * @param indexName
	 * @param type
	 * @param docId
	 * @return
	 */
	public boolean deleteDoc(String indexName, String type, String docId){
		String strResult = client.prepareDelete(indexName, type, docId).get().getResult().toString();;
		return strResult.equals("DELETED");
	}
	
	/**
	 * 根据id，查询记录
	 * @param indexName
	 * @param type
	 * @param id
	 * @return
	 */
	public String searchById(String indexName, String type, String id){
		long start = System.currentTimeMillis();
		GetResponse response = client.prepareGet(indexName, type, id)
				.setOperationThreaded(false).execute().actionGet();  
		String result = response.isExists() ? response.getSourceAsString() : null;
		logger.info("elasticsearch---查询耗时：{} ms", System.currentTimeMillis() - start);
		return result;
	}
	
	/**
	 * 复杂查询
	 */
	public Map<String, Object> searchByContent(String indexName, String type, int from, int size,
			Map<String, String> mustMap, Map<String, String[]> shouldMap, Map<String, Object> filterMap) {
		//生成搜索条件
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
		//高亮
		HighlightBuilder hiBuilder = new HighlightBuilder();
		hiBuilder.preTags("<span style=\"color:red\">");
		hiBuilder.postTags("</span>");
		//并且关系
		if(!CollectionUtils.isEmpty(mustMap)) {
			for (Entry<String, String> entry : mustMap.entrySet()) {
				hiBuilder.field(entry.getKey());
				boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(entry.getKey(), entry.getValue()));
			}
		}
		//或者关系
		if(!CollectionUtils.isEmpty(shouldMap)) {
			for (Entry<String, String[]> entry : shouldMap.entrySet()) {
				String field[] = entry.getValue();
				String keyword = entry.getKey();
				boolQueryBuilder.should(QueryBuilders.multiMatchQuery(keyword, field));
				for (String highKey : field) {
					hiBuilder.field(highKey);
				}
			}
		}
		//过滤关系
		if(!CollectionUtils.isEmpty(filterMap)) {
			for (Entry<String, Object> entry : filterMap.entrySet()) {
				filterQueryBuilder.filter(QueryBuilders.matchPhraseQuery(entry.getKey(), entry.getValue()));
			}
		}
		SearchRequestBuilder requestBuilder = client.prepareSearch(indexName).setTypes(type)
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setQuery(boolQueryBuilder.filter(filterQueryBuilder))
					.setExplain(true)
					.setFrom(from)
					.setSize(size);
		logger.info("es的请求参数：{}", requestBuilder.toString());
	    //执行搜索
	    SearchResponse myresponse = requestBuilder.execute().actionGet();
	    //获取查询结果，生成返回对象
		SearchHits searchHits = myresponse.getHits();
		//命中的列表
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < searchHits.getHits().length; i++) {
			Map<String, Object> source = searchHits.getHits()[i].getSourceAsMap();
			list.add(source);
		}
		logger.info("索引库：{}， 类型：{} ----->查询到的数据：{}", indexName, type, list);
		
		Map<String, Object> map = new HashMap<String, Object>();
		//命中的总数量
		map.put("total", searchHits.getTotalHits());
		//总页数
		long totalPage = myresponse.getHits().totalHits/10;
		if ((myresponse.getHits().totalHits % 10) != 0) {
		    totalPage ++;
		}
		map.put("totalPage", totalPage);
		//命中的列表
		map.put("rows", processKeyHighlighter(mustMap, shouldMap, searchHits));
		return map;
	}
	
	/************************** 以下为私有方法 **************************/
	
	//处理关键字高亮
	private List<Map<String, Object>> processKeyHighlighter(Map<String, String> mustMap,
			Map<String, String[]> shouldMap, SearchHits searchHits) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < searchHits.getHits().length; i++) {
			Map<String, HighlightField> highlightFields = searchHits.getHits()[i].getHighlightFields();
			Map<String, Object> source = searchHits.getHits()[i].getSourceAsMap();
			//结果高亮返回
			putResultKeyHighlight(mustMap, shouldMap, highlightFields, source);
			list.add(source);
		}
		return list;
	}

	private void putResultKeyHighlight(Map<String, String> mustMap, Map<String, String[]> shouldMap,
			Map<String, HighlightField> highlightFields, Map<String, Object> source) {
		//单字段
		if(!CollectionUtils.isEmpty(mustMap)) {
			for (Entry<String, String> entry : mustMap.entrySet()) {
				HighlightField titleField = highlightFields.get(entry.getKey());
				if (titleField != null) {
					Text[] fragments = titleField.fragments();
					String name = "";
					for (Text text : fragments) {
						name += text;
					}
					source.put(entry.getKey(), name);
				}
			}
		}
		//多字段
		if(!CollectionUtils.isEmpty(shouldMap)) {
			for (Entry<String, String[]> entry : shouldMap.entrySet()) {
				String[] shouldFields = entry.getValue();
				for (String shouldField : shouldFields) {
					HighlightField titleField = highlightFields.get(shouldField);
					if (titleField != null) {
						Text[] fragments = titleField.fragments();
						String name = "";
						for (Text text : fragments) {
							name += text;
						}
						source.put(shouldField, name);
					}
				}
			}
		}
	}
	
	
}
