package com.wql.cloud.userservice.job;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wql.cloud.tool.json.JsonUtil;
import com.wql.cloud.tool.string.StringUtils;
import com.wql.cloud.userservice.pojo.domain.User;
import com.wql.cloud.userservice.service.UserService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.mq.client.message.XxlMqMessage;
import com.xxl.mq.client.producer.XxlMqProducer;


@JobHandler(value="testMqJobHandler")
@Component
public class TestMqJobHandler extends IJobHandler {

	@Autowired
	private UserService userService;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		XxlJobLogger.log("XXL-JOB, Hello World. ---> "+ param);
		
		int count = 0;
		if(StringUtils.isBlank(param)) {
			count = 10;
		} else {
			count = Integer.parseInt(param);
		}
		List<User> list = userService.queryList(null);
		List<Integer> ids = list.stream().map(User::getId).collect(Collectors.toList());
		for (int i = 0; i < count; i++) {
			XxlMqProducer.produce(new XxlMqMessage("topic_1", JsonUtil.toJsonString(ids)));
		}
		return SUCCESS;
	}

}
