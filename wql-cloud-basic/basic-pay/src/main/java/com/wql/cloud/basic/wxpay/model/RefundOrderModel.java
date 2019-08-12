package com.wql.cloud.basic.wxpay.model;

import java.math.BigDecimal;

public class RefundOrderModel {

	/**
	 * 支付时的订单号
	 */
	private String outTradeNo;

	/**
	 * 退款订单号，唯一
	 * 一笔退款失败后重新提交，请不要更换退款单号，请使用原商户退款单号
	 */
	private String outRefundNo;

	/**
	 * 订单金额
	 */
	private BigDecimal totalFee;

	/**
	 * 退款金额
	 */
	private BigDecimal refundFee;

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public BigDecimal getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(BigDecimal refundFee) {
		this.refundFee = refundFee;
	}

}
