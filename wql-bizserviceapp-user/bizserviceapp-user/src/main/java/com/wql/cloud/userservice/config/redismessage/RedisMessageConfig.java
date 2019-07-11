package com.wql.cloud.userservice.config.redismessage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.wql.cloud.userservice.service.messagesend.MessageReceiver;

/**
 * redis消息订阅 【将监听器注入监听容器】
 * 
 * @author wangqiulin
 *
 */
@Configuration
public class RedisMessageConfig {

	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter, MessageListenerAdapter listenerAdapter2) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		// 订阅了一个叫top的通道
		container.addMessageListener(listenerAdapter, new PatternTopic("top"));
		container.addMessageListener(listenerAdapter2, new PatternTopic("top2")); //配置多个
		return container;
	}

	/**消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法*/
	@Bean
	public MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
		// 给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
		// 不填defaultListenerMethod默认调用handleMessage
		return new MessageListenerAdapter(receiver, "receiverMessage");
	}

	
	//配置多个
	@Bean
	public MessageListenerAdapter listenerAdapter2(MessageReceiver receiver) {
		// 给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
		// 不填defaultListenerMethod默认调用handleMessage
		return new MessageListenerAdapter(receiver, "receiverMessage");
	}
	
	
}
