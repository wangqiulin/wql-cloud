package com.wql.cloud.gateway.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wql.cloud.gateway.core.factory.BlackListFactory;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 刷新黑名单 任务
 */
@JobHandler(value = "blackListRefreshJobHandler")
@Component
public class BlackListRefreshJobHandler extends IJobHandler {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BlackListFactory blackListFactory;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			blackListFactory.initBlackList();
		} catch (Exception e) {
			XxlJobLogger.log(e);
			return FAIL;
		} 
		return SUCCESS;
	}
}
