package com.wql.cloud.systemservice.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wql.cloud.systemservice.service.merchant.MerchantService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 加载商户信息至缓存（商户、商户密钥、商户白名单、商户api权限）
 */
@JobHandler(value = "merchantJobHandler")
@Component
public class MerchantJobHandler extends IJobHandler {

	@Autowired
	private MerchantService merchantService;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			merchantService.loadMerchantCache();
		} catch (Exception e) {
			XxlJobLogger.log(e);
			return FAIL;
		} 
		return SUCCESS;
	}
	
}
