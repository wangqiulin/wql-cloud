package com.wql.cloud.userservice.service;

public interface WechatAppPayService {

	/**
	 * 确认下单
	 */
	void unifiedOrder();
	
	/**
	 * 微信支付成功后的回调业务处理
	 * @param reqBody
	 */
	boolean updatePayResultNotify(String reqBody);
	
}
