package com.wql.cloud.payservice.config.rabbitmq;
//package com.wql.cloud.userservice.config.rabbitmq;
//
//import org.springframework.amqp.core.AcknowledgeMode;
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.rabbitmq.client.Channel;
//
///**
// * RabbitMQ配置
// * 
// * @author wangqiulin
// * @date 2018年10月17日
// */
//@Configuration
//public class ProducerConfig {
//
//	@Autowired
//	private ConnectionFactory connectionFactory;
//	
//	public static final String EXCHANGE = "wql-cloud-exchange";
//	public static final String ROUTINGKEY = "wql-cloud-routingKey";
//
//	/**
//	 * 针对消费者配置 FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念 HeadersExchange
//	 * 通过添加属性key-value匹配 DirectExchange:按照routingkey分发到指定队列 TopicExchange:多关键字匹配
//	 * 
//	 * @return
//	 */
//	@Bean
//	public DirectExchange defaultExchange() {
//		return new DirectExchange(EXCHANGE); // 在申明交换机时需要指定交换机名称，默认创建可持久交换机
//	}
//
//	@Bean
//	public Queue queue() {
//		return new Queue("wql-cloud-queue", true); // true:队列持久
//	}
//
//	@Bean
//	public Binding binding() {
//		return BindingBuilder.bind(queue()).to(defaultExchange()).with(ROUTINGKEY);
//	}
//
//	// 通过消息监听容器实现消息的监听，在消息到来时执行回调操作。
//	@Bean
//	public SimpleMessageListenerContainer messageContainer() {
//		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
//		// 设置队列信息
//		container.setQueues(queue());
//		// 添加队列信息
//		container.setExposeListenerChannel(true);
//		// 设置并发消费者数量，默认情况为1
//		container.setMaxConcurrentConsumers(15);
//		container.setConcurrentConsumers(3);
//		// 设置确认模式手工确认
//		container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//		// 监听的业务逻辑
//		container.setMessageListener(new ChannelAwareMessageListener() {
//			@Override
//			public void onMessage(Message message, Channel channel) throws Exception {
//				byte[] body = message.getBody();
//				String content = new String(body);
//				System.err.println("receive msg : " + content);
//				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); // 确认消息成功消费
//			}
//		});
//		return container;
//	}
//
//}
