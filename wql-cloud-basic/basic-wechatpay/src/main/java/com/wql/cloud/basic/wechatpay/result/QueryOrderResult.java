package com.wql.cloud.basic.wechatpay.result;

import java.util.Date;

/**
 * 查询订单支付结果-返回对象
 * @author wangqiulin
 *
 */
public class QueryOrderResult {
	
	private Boolean resultCode;
	
	private String resultMsg;

	private String tradeState;
	
	private Date payedTime;

	public QueryOrderResult() {
		super();
	}

	public QueryOrderResult(Boolean resultCode, String resultMsg) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

	public QueryOrderResult(Boolean resultCode, String resultMsg, String tradeState) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.tradeState = tradeState;
	}

	public QueryOrderResult(Boolean resultCode, String resultMsg, String tradeState, Date payedTime) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.tradeState = tradeState;
		this.payedTime = payedTime;
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

	public String getTradeState() {
		return tradeState;
	}

	public void setTradeState(String tradeState) {
		this.tradeState = tradeState;
	}

	public Date getPayedTime() {
		return payedTime;
	}

	public void setPayedTime(Date payedTime) {
		this.payedTime = payedTime;
	}

}