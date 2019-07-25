package com.wql.cloud.basic.wechatpay.result;

import java.util.Map;

/**
 * 下单返回结果
 * @author wangqiulin
 *
 */
public class PlaceOrderResult {
	
	private Boolean resultCode;
	
	private String resultMsg;
	
	/**
	 * 微信H5支付，下单链接
	 */
	private String mwebUrl;
	
	/**
	 * 微信App支付，返回内容
	 */
	private Map<String, String> wxPayDataMap;

	public PlaceOrderResult() {
		super();
	}

	public PlaceOrderResult(Boolean resultCode, String resultMsg) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

	public PlaceOrderResult(Boolean resultCode, String resultMsg, Map<String, String> wxPayDataMap) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.wxPayDataMap = wxPayDataMap;
	}

	public PlaceOrderResult(Boolean resultCode, String resultMsg, String mwebUrl) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.mwebUrl = mwebUrl;
	}
	
	public Boolean getResultCode() {
		return resultCode;
	}

	public void setResultCode(Boolean resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getMwebUrl() {
		return mwebUrl;
	}

	public void setMwebUrl(String mwebUrl) {
		this.mwebUrl = mwebUrl;
	}

	public Map<String, String> getWxPayDataMap() {
		return wxPayDataMap;
	}

	public void setWxPayDataMap(Map<String, String> wxPayDataMap) {
		this.wxPayDataMap = wxPayDataMap;
	}

}
