package com.wql.cloud.gateway.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wql.cloud.gateway.core.factory.MerchantFactory;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 刷新merchant 任务
 */
@JobHandler(value = "merchantRefreshJobHandler")
@Component
public class MerchantRefreshJobHandler extends IJobHandler {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MerchantFactory merchantFactory;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		XxlJobLogger.log("--------------merchantRefreshJobHandler execute------------begin-->");
		try {
			merchantFactory.initMerchantLocalMap();
		} catch (Exception e) {
			XxlJobLogger.log(e);
			return FAIL;
		} finally {
			XxlJobLogger.log("--------------merchantRefreshJobHandler execute------------end-->");
		}
		return SUCCESS;
	}
}
