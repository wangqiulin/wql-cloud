package com.wql.cloud.basic.alipay.result;

import java.util.Date;

public class QueryOrderResult {

	/**
	 * 交易状态：
	 * 	TRADE_SUCCESS： 支付成功
	 *  WAIT_BUYER_PAY：待支付
	 *  TRADE_CLOSED： 未付款交易超时关闭，或支付完成后全额退款
	 *  TRADE_FINISHED： 交易结束，不可退款
	 *  TRADE_FAIL： 支付失败
	 *  TRADE_UNKNOW： 交易结果未知
	 */
	private String tradeStatus;

	/**
	 * 支付结果描述
	 */
	private String resultMsg;
	
	/**
	 * 支付完成时间
	 */
	private Date payedTime;

	public QueryOrderResult() {
	}

	public QueryOrderResult(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public QueryOrderResult(String resultMsg, String tradeStatus) {
		this.resultMsg = resultMsg;
		this.tradeStatus = tradeStatus;
	}

	public QueryOrderResult(String resultMsg, String tradeStatus, Date payedTime) {
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
