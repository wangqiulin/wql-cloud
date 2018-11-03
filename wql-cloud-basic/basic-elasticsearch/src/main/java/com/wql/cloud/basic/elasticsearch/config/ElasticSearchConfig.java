package com.wql.cloud.basic.elasticsearch.config;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig implements FactoryBean<TransportClient>, InitializingBean, DisposableBean {

	private static final Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);

	private TransportClient client;

	// 由于项目从2.2.4配置的升级到 5.4.1版本 原配置文件不想动还是指定原来配置参数
	@Value("${spring.data.elasticsearch.cluster-nodes}")
	private String clusterNodes;

	@Value("${spring.data.elasticsearch.cluster-name}")
	private String clusterName;

	@Value("${spring.data.elasticsearch.indexName}")
	private String indexName;

	@Value("${spring.data.elasticsearch.indexType}")
	private String indexType;

	@Value("${spring.data.elasticsearch.ikWordMappings}")
	private String ikWordMappings;

	@Override
	public void destroy() throws Exception {
		try {
			logger.info("Closing elasticSearch client");
			if (client != null) {
				client.close();
			}
		} catch (final Exception e) {
			logger.error("Error closing ElasticSearch client: ", e);
		}
	}

	@Override
	public TransportClient getObject() throws Exception {
		return client;
	}

	@Override
	public Class<TransportClient> getObjectType() {
		return TransportClient.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 设置集群名称
		Settings settings = Settings.builder().put("cluster.name", clusterName).put("client.transport.sniff", true)
				.build();
		TransportClient preBuiltTransportClient = new PreBuiltTransportClient(settings);
		if (StringUtils.isNoneBlank(clusterNodes)) {
			for (String nodes : clusterNodes.split(",")) {
				String InetSocket[] = nodes.split(":");
				String Address = InetSocket[0];
				Integer port = Integer.valueOf(InetSocket[1]);
				preBuiltTransportClient.addTransportAddress(new TransportAddress(InetAddress.getByName(Address), port));
			}
			client = preBuiltTransportClient;
		}

		IndicesAdminClient indicesAdminClient = preBuiltTransportClient.admin().indices();
		// 查看索引是否存在,不存在就创建索引
		if (!checkExistsIndex(indicesAdminClient, indexName)) {
			indicesAdminClient.prepareCreate(indexName).setSettings().execute().actionGet();
		}

		// 查询mapping是否存在，已存在就不创建了
		GetMappingsResponse getMappingsResponse = indicesAdminClient
				.getMappings(new GetMappingsRequest().indices(indexName)).actionGet();
		ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> indexToMappings = getMappingsResponse
				.getMappings();
		if (indexToMappings.get(indexName).get(indexType) == null) {
			// 创建zk分词mapping
			PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(indexType);
			for (String ikWord : ikWordMappings.split(",")) {
				// 对哪些字段设置中文分词器
				mapping.source(createIKMapping(indexType, ikWord).string());
			}
			mapping.updateAllTypes(true);
			indicesAdminClient.putMapping(mapping).actionGet();
		}
	}

	/**
	 * 查询是否创建了索引
	 * 
	 * @param indicesAdminClient
	 * @param indexName
	 * @return
	 */
	private static boolean checkExistsIndex(IndicesAdminClient indicesAdminClient, String indexName) {
		return indicesAdminClient.prepareExists(indexName).execute().actionGet().isExists();
	}

	/**
	 * 创建mapping分词IK索引 Elasticsearch的mapping一旦创建，只能增加字段，而不能修改已经mapping的字段
	 * 
	 * @param indexType
	 * @return
	 */
	private static XContentBuilder createIKMapping(String indexType, String field) {
		XContentBuilder mapping = null;
		try {
			mapping = XContentFactory.jsonBuilder().startObject()
					// 索引库名（类似数据库中的表）
					.startObject(indexType)
					// 匹配全部
					.startObject("properties")
					// 根据只对content这个Fields分词
					.startObject(field).field("type", "string").field("store", "no")
					.field("term_vector", "with_positions_offsets").field("analyzer", "ik_max_word")
					.field("search_analyzer", "ik_max_word").field("include_in_all", "true").field("boost", 8)
					.endObject().endObject().endObject().endObject();
		} catch (IOException e) {
			logger.error("创建mapping分词IK索引 error" + e.getLocalizedMessage());
		}
		return mapping;
	}

}
