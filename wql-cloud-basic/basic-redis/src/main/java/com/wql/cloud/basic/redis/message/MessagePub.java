package com.wql.cloud.basic.redis.message;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

/**
 * 消息发布
 * 
 * @author wangqiulin
 *
 */
@Service
public class MessagePub {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Resource(name = "redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
    private GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer;
	
	/**
	 * 消息发布
	 * 
	 * @param channel
	 * @param message
	 */
	public void pub(String channel, String message) {
		redisTemplate.convertAndSend(channel, message);
		logger.info("MessagePub------->向通道{}, 发布消息：{}", channel, message);
	}
	
	/**
	 * 消息发布
	 * 
	 * @param channel
	 * @param obj
	 */
	public void pub(String channel, Object obj) {
		byte[] msg =jackson2JsonRedisSerializer.serialize(obj);
		String message = new String(msg);
		redisTemplate.convertAndSend(channel, message);
		logger.info("MessagePub------->向通道{}, 发布消息：{}", channel, message);
	}
	
	
	
}
