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
	
	@SuppressWarnings("unused")
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

}
