package com.wql.cloud.userservice.service.messagesend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 消息订阅
 * 
 * @author wangqiulin
 *
 */
@Service
public class MessageReceiver {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	public void receiverMessage(String message) {
        logger.info("MessageSub------->收到消息：{}", message);
	}
	
}
