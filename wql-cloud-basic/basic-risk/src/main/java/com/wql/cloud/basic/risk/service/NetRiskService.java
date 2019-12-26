package com.wql.cloud.basic.risk.service;

/**
 * 网易风控
 * 
 * @author wangqiulin
 *
 */
public interface NetRiskService {

	/**
	 * 网易二次验证结果
	 * 
	 * @param secretId: 账户
	 * @param secretKey: 私钥
	 * @param captchaId: 哪种类型的验证码
	 * @param validate: 验证参数
	 * @return
	 */
	Boolean verifyResult(String secretId, String secretKey, String captchaId, String validate);
	
}
