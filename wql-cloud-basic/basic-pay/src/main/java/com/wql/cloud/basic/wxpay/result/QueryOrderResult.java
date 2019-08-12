package com.wql.cloud.basic.wxpay.result;

import java.util.Date;

/**
 * 查询订单支付结果-返回对象
 * @author wangqiulin
 *
 */
public class QueryOrderResult {
	
	/**
	 * 交易状态：
	 * 	SUCCESS： 支付成功
	 *  NOTPAY： 未支付
	 *  CLOSED： 已关闭
	 *  USERPAYING：支付中
	 *  PAYERROR：支付失败
	 *  UNKONW: 未知
	 */
	private String tradeState;

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

	public QueryOrderResult(String resultMsg, String tradeState) {
		this.resultMsg = resultMsg;
		this.tradeState = tradeState;
	}

	public QueryOrderResult(String resultMsg, String tradeState, Date payedTime) {
		this.resultMsg = resultMsg;
		this.tradeState = tradeState;
		this.payedTime = payedTime;
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
