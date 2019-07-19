//package com.wql.cloud.userservice.controller;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class KafkaController {
//
//	@Autowired
//	private KafkaTemplate<String, String> kafkaTemplate;
//
//	/**
//	 * 发送消息的方法
//	 *
//	 * @param key
//	 *            推送数据的key
//	 * @param data
//	 *            推送数据的data
//	 */
//	private void send(String key, String data) {
//		// topic 名称 key data 消息数据
//		kafkaTemplate.send("my_test", key, data);
//	}
//
//	// test 主题 1 my_test 3
//	@RequestMapping("/kafka")
//	public String testKafka() {
//		int iMax = 6;
//		for (int i = 1; i < iMax; i++) {
//			send("key" + i, "data" + i);
//		}
//		return "success";
//	}
//
//	/**
//	 * 消费者使用日志打印消息
//	 */
//	@KafkaListener(topics = "my_test")
//	public void receive(ConsumerRecord<?, ?> consumer) {
//		System.out.println("topic名称:" + consumer.topic() 
//            + ",key:" + consumer.key() + ",分区位置:" + consumer.partition()
//			+ ", 下标" + consumer.offset());
//	}
//	
//}
