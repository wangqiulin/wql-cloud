package com.wql.cloud.basic.redis.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

/**
 * 消息订阅
 * 
 * @author wangqiulin
 *
 */
@Service
public class MessageSub implements MessageListener {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired
    private StringRedisSerializer stringRedisSerializer;
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		byte[] body = message.getBody();
		byte[] channel = message.getChannel();
		String msg = (String)stringRedisSerializer.deserialize(body);  
        String topic = (String)stringRedisSerializer.deserialize(channel);
        logger.info("MessageSub------->监听：{}, 收到消息：{}", topic, msg);
	}
	
	
}
