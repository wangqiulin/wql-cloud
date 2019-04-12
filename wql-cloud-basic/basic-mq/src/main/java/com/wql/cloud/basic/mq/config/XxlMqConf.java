package com.wql.cloud.basic.mq.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.xxl.mq.client.factory.impl.XxlMqSpringClientFactory;

/**
 * 生产者：
 * 并行消费: XxlMqProducer.produce(new XxlMqMessage("topic_1", data));
 * 串行消费: XxlMqProducer.produce(new XxlMqMessage("topic_1", data, 1L));
 * 广播消费: XxlMqProducer.broadcast(new XxlMqMessage("topic_1", data));
 * 延时消息: XxlMqProducer.produce(new XxlMqMessage("topic_1", data, effectTime));
 * 
 * 消费者：
 * 实现类上
 * 添加注解 @MqConsumer(topic = "topic_1")
 * 实现接口 implements IMqConsumer
 * 
 * @author wangqiulin
 */
@Component
public class XxlMqConf {

	@Value("${xxl.mq.admin.address}")
	private String adminAddress;
	
	@Value("${xxl.mq.accessToken}")
	private String accessToken;

	@Bean
	public XxlMqSpringClientFactory getXxlMqConsumer() {
		XxlMqSpringClientFactory xxlMqSpringClientFactory = new XxlMqSpringClientFactory();
		xxlMqSpringClientFactory.setAdminAddress(adminAddress);
		xxlMqSpringClientFactory.setAccessToken(accessToken);
		return xxlMqSpringClientFactory;
	}

}