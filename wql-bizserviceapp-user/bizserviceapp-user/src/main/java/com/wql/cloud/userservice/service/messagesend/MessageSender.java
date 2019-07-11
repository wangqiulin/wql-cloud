package com.wql.cloud.userservice.service.messagesend;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 消息发布
 * 
 * @author wangqiulin
 *
 */
@Service
public class MessageSender {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Resource(name = "redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * 消息发布
	 * 
	 * @param channel
	 * @param message
	 */
	public void sendMessage(String channel, String message) {
		redisTemplate.convertAndSend(channel, message);
		logger.info("MessageSender------->向通道{}, 发布消息：{}", channel, message);
	}
	
}
