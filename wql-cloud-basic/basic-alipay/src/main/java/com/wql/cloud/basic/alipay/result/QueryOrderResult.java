package com.wql.cloud.basic.alipay.result;

import java.util.Date;

public class QueryOrderResult {

	private String resultMsg;
	
	private String tradeStatus;
	
	private Date payedTime;

	public QueryOrderResult() {
		super();
	}

	public QueryOrderResult(String resultMsg) {
		super();
		this.resultMsg = resultMsg;
	}

	public QueryOrderResult(String resultMsg, String tradeStatus) {
		super();
		this.resultMsg = resultMsg;
		this.tradeStatus = tradeStatus;
	}

	public QueryOrderResult(String resultMsg, String tradeStatus, Date payedTime) {
		super();
		this.resultMsg = resultMsg;
		this.tradeStatus = tradeStatus;
		this.payedTime = payedTime;
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
