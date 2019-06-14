package com.wql.cloud.basic.alipay.model;

public class RefundOrderModel {

	/**
	 * 订单支付时传入的商户订单号,不能和 trade_no同时为空。
	 */
	private String outTradeNo;
	
	/**
	 * 支付宝交易号，和商户订单号不能同时为空
	 */
	private String tradeNo;
	
	/**
	 * 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
	 */
	private String refundAmount;
	
	/**
	 * 退款原因
	 */
	private String refundReason;
	
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}
	
}
