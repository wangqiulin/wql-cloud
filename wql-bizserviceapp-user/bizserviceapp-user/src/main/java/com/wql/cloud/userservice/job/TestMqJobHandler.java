//package com.wql.cloud.userservice.job;
//
//import org.springframework.stereotype.Component;
//
//import com.wql.cloud.tool.string.StringUtils;
//import com.xxl.job.core.biz.model.ReturnT;
//import com.xxl.job.core.handler.IJobHandler;
//import com.xxl.job.core.handler.annotation.JobHandler;
//import com.xxl.job.core.log.XxlJobLogger;
//import com.xxl.mq.client.message.XxlMqMessage;
//import com.xxl.mq.client.producer.XxlMqProducer;
//
//
//@JobHandler(value="testMqJobHandler")
//@Component
//public class TestMqJobHandler extends IJobHandler {
//
//	@Override
//	public ReturnT<String> execute(String param) throws Exception {
//		XxlJobLogger.log("XXL-JOB, Hello World. ---> "+ param);
//		
//		int count = 0;
//		if(StringUtils.isBlank(param)) {
//			count = 10;
//		} else {
//			count = Integer.parseInt(param);
//		}
//		for (int i = 0; i < count; i++) {
//			XxlMqProducer.produce(new XxlMqMessage("topic_1", String.valueOf(i)));
//		}
//		return SUCCESS;
//	}
//
//}
