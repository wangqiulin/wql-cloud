package com.wql.cloud.systemservice.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wql.cloud.systemservice.service.blacklist.BlacklistService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 将网关黑名单加入缓存
 */
@JobHandler(value = "blackListJobHandler")
@Component
public class BlackListJobHandler extends IJobHandler {

	@Autowired
	private BlacklistService blacklistService;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			blacklistService.loadBlacklistCache();
		} catch (Exception e) {
			XxlJobLogger.log(e);
			return FAIL;
		} 
		return SUCCESS;
	}
}
