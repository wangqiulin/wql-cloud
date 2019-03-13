package com.wql.cloud.basic.alipay.result;

import java.util.Date;

public class QueryOrderResult {

	private Boolean resultCode;
	
	private String resultMsg;
	
	private String tradeStatus;
	
	private Date payedTime;

	public QueryOrderResult() {
		super();
	}

	public QueryOrderResult(Boolean resultCode, String resultMsg) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

	public QueryOrderResult(Boolean resultCode, String resultMsg, String tradeStatus) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.tradeStatus = tradeStatus;
	}

	public QueryOrderResult(Boolean resultCode, String resultMsg, String tradeStatus, Date payedTime) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.tradeStatus = tradeStatus;
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

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public Date getPayedTime() {
		return payedTime;
	}

	public void setPayedTime(Date payedTime) {
		this.payedTime = payedTime;
	}
	
}
