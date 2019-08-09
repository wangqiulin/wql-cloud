package com.wql.cloud.gateway.job;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wql.cloud.gateway.core.factory.ApiFactory;
import com.wql.cloud.gateway.core.factory.FilterFactory;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 刷新api 任务
 */
@JobHandler(value = "apiRefreshJobHandler")
@Component
public class ApiRefreshJobHandler extends IJobHandler {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ApiFactory apiFactory;
	
	@Resource(name="requestInnerFilterFactory")
	private FilterFactory requestInnerFilterFactory;
	
	@Resource(name="responseInnerFilterFactory")
	private FilterFactory responseInnerFilterFactory;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			apiFactory.initApiLocalMap();
			requestInnerFilterFactory.initApiFilterMap();
			responseInnerFilterFactory.initApiFilterMap();
		} catch (Exception e) {
			XxlJobLogger.log(e);
			return FAIL;
		} 
		return SUCCESS;
	}
}
