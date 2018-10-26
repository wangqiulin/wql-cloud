package com.wql.cloud.basic.elasticsearch.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
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

	//由于项目从2.2.4配置的升级到 5.4.1版本 原配置文件不想动还是指定原来配置参数
    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    private TransportClient client;

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
        buildClient();
    }

    @SuppressWarnings("unchecked")
	protected void buildClient() throws UnknownHostException  {
    	//设置集群名称
		Settings settings = Settings.builder()
				.put("cluster.name", clusterName)
				.put("client.transport.sniff", true)
				.build();
    	TransportClient preBuiltTransportClient = new PreBuiltTransportClient(settings);
    	if(StringUtils.isNoneBlank(clusterNodes)) {
            for (String nodes:clusterNodes.split(",")) {
                String InetSocket [] = nodes.split(":");
                String Address = InetSocket[0];
                Integer port = Integer.valueOf(InetSocket[1]);
                preBuiltTransportClient.addTransportAddress(new
                		TransportAddress(InetAddress.getByName(Address),port ));
            }
            client = preBuiltTransportClient;
        }
    }
	
	
}
