//package com.wql.cloud.basic.elasticsearch.util;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.elasticsearch.action.search.SearchRequestBuilder;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.common.text.Text;
//import org.elasticsearch.common.xcontent.XContentBuilder;
//import org.elasticsearch.common.xcontent.XContentFactory;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHits;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.CollectionUtils;
//
//import io.searchbox.client.JestClient;
//import io.searchbox.client.JestResult;
//import io.searchbox.client.JestResultHandler;
//import io.searchbox.core.Bulk;
//import io.searchbox.core.DocumentResult;
//import io.searchbox.core.Get;
//import io.searchbox.core.Index;
//import io.searchbox.core.Search;
//import io.searchbox.core.Update;
//import io.searchbox.indices.DeleteIndex;
//import io.searchbox.indices.mapping.PutMapping;
//import io.searchbox.params.SearchType;
//
//public class ESUtil {
//	
//    public final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//	private JestClient jestClient;
//	
////	public JestClient getClient() {
////		JestClientFactory factory = new JestClientFactory();
////	    factory.setHttpClientConfig(new HttpClientConfig
////	            .Builder(Arrays.asList("http://192.168.1.92:9200"))
////	            .multiThreaded(true)
////	            .defaultMaxTotalConnectionPerRoute(Integer.valueOf(100))
////	            .maxTotalConnection(Integer.valueOf(20))
////	            .build());
////	    this.jestClient = factory.getObject();
////	    return jestClient;
////	}
//	
//	
//	public boolean createIndex(String index, String type) throws IOException {
//		XContentBuilder mapping = XContentFactory.jsonBuilder().startObject()
//					// 索引库名（类似数据库中的表）
//					.startObject(type)
//					// 匹配全部
//					.startObject("properties")
//					// 根据只对content这个Fields分词
////					.startObject(field).field("type", "string").field("store", "no")
//					.field("term_vector", "with_positions_offsets").field("analyzer", "ik_max_word")
//					.field("search_analyzer", "ik_max_word").field("include_in_all", "true").field("boost", 8)
//					.endObject().endObject().endObject().endObject();
//		
//		//创建index, type
//		PutMapping.Builder builder = new PutMapping.Builder(index, type, mapping);
//		JestResult result = jestClient.execute(builder.build());
//		return result.isSucceeded();
//	}
//	
//	
//	/**
//	 * 删除索引
//	 * 
//	 * @param index
//	 * @return
//	 * @throws IOException
//	 */
//	public boolean deleteIndex(String index) throws IOException {
//		JestResult result = jestClient.execute(new DeleteIndex.Builder(index).build());
//		return result.isSucceeded();
//	}
//	
//	
//	
//	/**
//     * 获取对象
//     * 可以加路由：setParameter(Parameters.ROUTING, "")
//	 * @throws IOException 
//     */
//    public <T> T getData(String index, String type, String _id, Class<T> clazz) throws IOException {
//        Get get = new Get.Builder(index, _id).type(type).build();
//        
//        JestResult result = jestClient.execute(get);
//        if (result.isSucceeded()) {
//            return result.getSourceAsObject(clazz);
//        }
//		return null;
//    }
//    
//    /**
//     * 写入数据
//     * 对象里要有 id
//     */
//    public <T> boolean insertData(String index, String type, List<T> list) {
//        Index action = new Index.Builder(list).index(index).type(type).build();
//        try {
//        	DocumentResult result = jestClient.execute(action);
//        	return result.isSucceeded();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//    
//    /**
//     * 功能描述：插入数据
//     * @entity 里面最好有@JestId id，要不然会自动生成一个
//     */
//    public <T> boolean insertData(String index, String type, T entity) {
//        Index action = new Index.Builder(entity).index(index).type(type).build();
//        try {
//        	DocumentResult result = jestClient.execute(action);
//        	return result.isSucceeded();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//    
//	
//    /**
//     * 功能描述：插入数据
//     *
//     * @param index 索引名
//     * @param type  类型
//     * @entity 里面最好有 id，要不然会自动生成一个
//     */
//    public <T> void insertDataAsync(String index, String type, T entity) {
//        Index action = new Index.Builder(entity).index(index).type(type).build();
//        
//        jestClient.executeAsync(action, new JestResultHandler<JestResult>() {
//            @Override
//            public void completed(JestResult result) {
//                logger.debug("insert success");
//            }
//
//            @Override
//            public void failed(Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//    
//    /**
//     * 功能描述：异步更新数据
//     */
//    public <T> void updateDataAsync(String index, String type, String _id, T entity) {
//    	jestClient.executeAsync(new Update.Builder(entity).id(_id)
//                .index(index)
//                .type(type)
//                .build(), new JestResultHandler<JestResult>() {
//            @Override
//            public void completed(JestResult result) {
//                logger.debug("insert success");
//            }
//
//            @Override
//            public void failed(Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//    
//    /**
//     * 功能描述：异步批量插入数据
//     */
//    public <T> void bulkInsertData(String index, String type, List<T> dataList) throws Exception{
//
//        List<Index> actions = new ArrayList<>();
//        assert dataList != null;
//        dataList.forEach(item -> {
//            actions.add(new Index.Builder(item).build());
//        });
//
//        Bulk bulk = new Bulk.Builder()
//                .defaultIndex(index)
//                .defaultType(type)
//                .addAction(actions)
//                .build();
//
//        jestClient.execute(bulk);
//    }
//    
//    
// 
//    /**
//     * 查找记录列表
//     * @param type
//     * @param searchParams
//     * @param <T>
//     * @return
//     */
//    public <T> List<T> queryRecords(String index, String type, Map<String,Object> searchParams, Class<T> clz) {
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        BoolQueryBuilder filterQueryBuilders = QueryBuilders.boolQuery();
//        for (Map.Entry<String,Object> entry : searchParams.entrySet()) {
//            filterQueryBuilders.must(QueryBuilders.termQuery(entry.getKey(),entry.getValue()));
//        }
//        searchSourceBuilder.postFilter(filterQueryBuilders);
//        
////        Sort sort = new Sort("gmtCreate",Sort.Sorting.DESC);
//       /* Search search = new Search.Builder(searchSourceBuilder.toString())
//                .addIndex(index).addType(type).addSort(sort).build();*/
//        Search search = new Search.Builder(searchSourceBuilder.toString())
//                .addIndex(index).addType(type).build();
//
//        try {
//            JestResult result = jestClient.execute(search);
//            if (result != null && result.isSucceeded()){
//                return result.getSourceAsObjectList(clz);
//            }
//            logger.error("=====================>result:{}.",result.getJsonString());
//        } catch (IOException e) {
//        	logger.error("queryRecords", e);
//        }
//        return null;
//    }
//
//    
//	
//	/**
//	 * 复杂查询
//	 */
//	public Map<String, Object> searchByContent(String indexName, String type, int from, int size,
//			Map<String, String> mustMap, Map<String, String[]> shouldMap, Map<String, Object> filterMap) {
//		//生成搜索条件
//		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//		BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
//		//高亮
//		HighlightBuilder hiBuilder = new HighlightBuilder();
//		hiBuilder.preTags("<span style=\"color:red\">");
//		hiBuilder.postTags("</span>");
//		//并且关系
//		if(!CollectionUtils.isEmpty(mustMap)) {
//			for (Entry<String, String> entry : mustMap.entrySet()) {
//				hiBuilder.field(entry.getKey());
//				boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(entry.getKey(), entry.getValue()));
//			}
//		}
//		//或者关系
//		if(!CollectionUtils.isEmpty(shouldMap)) {
//			for (Entry<String, String[]> entry : shouldMap.entrySet()) {
//				String field[] = entry.getValue();
//				String keyword = entry.getKey();
//				boolQueryBuilder.should(QueryBuilders.multiMatchQuery(keyword, field));
//				for (String highKey : field) {
//					hiBuilder.field(highKey);
//				}
//			}
//		}
//		//过滤关系
//		if(!CollectionUtils.isEmpty(filterMap)) {
//			for (Entry<String, Object> entry : filterMap.entrySet()) {
//				filterQueryBuilder.filter(QueryBuilders.matchPhraseQuery(entry.getKey(), entry.getValue()));
//			}
//		}
//		SearchRequestBuilder requestBuilder = jestClient.prepareSearch(indexName).setTypes(type)
//					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//					.setQuery(boolQueryBuilder.filter(filterQueryBuilder))
//					.setExplain(true)
//					.setFrom(from)
//					.setSize(size);
//		logger.info("es的请求参数：{}", requestBuilder.toString());
//	    //执行搜索
//	    SearchResponse myresponse = requestBuilder.execute().actionGet();
//	    //获取查询结果，生成返回对象
//		SearchHits searchHits = myresponse.getHits();
//		//命中的列表
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		for (int i = 0; i < searchHits.getHits().length; i++) {
//			Map<String, Object> source = searchHits.getHits()[i].getSourceAsMap();
//			list.add(source);
//		}
//		logger.info("索引库：{}， 类型：{} ----->查询到的数据：{}", indexName, type, list);
//		
//		Map<String, Object> map = new HashMap<String, Object>();
//		//命中的总数量
//		map.put("total", searchHits.getTotalHits());
//		//总页数
//		long totalPage = myresponse.getHits().totalHits/10;
//		if ((myresponse.getHits().totalHits % 10) != 0) {
//		    totalPage ++;
//		}
//		map.put("totalPage", totalPage);
//		//命中的列表
//		map.put("rows", processKeyHighlighter(mustMap, shouldMap, searchHits));
//		return map;
//	}
//	
//	/************************** 以下为私有方法 **************************/
//	
//	//处理关键字高亮
//	private List<Map<String, Object>> processKeyHighlighter(Map<String, String> mustMap,
//			Map<String, String[]> shouldMap, SearchHits searchHits) {
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		for (int i = 0; i < searchHits.getHits().length; i++) {
//			Map<String, HighlightField> highlightFields = searchHits.getHits()[i].getHighlightFields();
//			Map<String, Object> source = searchHits.getHits()[i].getSourceAsMap();
//			//结果高亮返回
//			putResultKeyHighlight(mustMap, shouldMap, highlightFields, source);
//			list.add(source);
//		}
//		return list;
//	}
//
//	private void putResultKeyHighlight(Map<String, String> mustMap, Map<String, String[]> shouldMap,
//			Map<String, HighlightField> highlightFields, Map<String, Object> source) {
//		//单字段
//		if(!CollectionUtils.isEmpty(mustMap)) {
//			for (Entry<String, String> entry : mustMap.entrySet()) {
//				HighlightField titleField = highlightFields.get(entry.getKey());
//				if (titleField != null) {
//					Text[] fragments = titleField.fragments();
//					String name = "";
//					for (Text text : fragments) {
//						name += text;
//					}
//					source.put(entry.getKey(), name);
//				}
//			}
//		}
//		//多字段
//		if(!CollectionUtils.isEmpty(shouldMap)) {
//			for (Entry<String, String[]> entry : shouldMap.entrySet()) {
//				String[] shouldFields = entry.getValue();
//				for (String shouldField : shouldFields) {
//					HighlightField titleField = highlightFields.get(shouldField);
//					if (titleField != null) {
//						Text[] fragments = titleField.fragments();
//						String name = "";
//						for (Text text : fragments) {
//							name += text;
//						}
//						source.put(shouldField, name);
//					}
//				}
//			}
//		}
//	}
//    
//    
//}
