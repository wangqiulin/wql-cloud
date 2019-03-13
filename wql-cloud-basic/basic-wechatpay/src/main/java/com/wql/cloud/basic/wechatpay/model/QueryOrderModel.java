package com.wql.cloud.basic.wechatpay.model;

/**
 * 下单请求参数
 * @author wangqiulin
 *
 */
public class QueryOrderModel {

	/**
	 * 微信分配的appId
	 */
	private String appId;
	
	/**
	 * 微信分配的应用商户号
	 */
	private String mchId;
	
	/**
	 * 微信支付的密钥
	 */
	private String privateKey;
	
	/**
	 * 订单号
	 */
	private String outTradeNo;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

}
