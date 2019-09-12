package com.wql.cloud.basic.alipay.model;

import java.math.BigDecimal;

public class RefundOrderModel {

	/**
	 * 订单支付时传入的商户订单号,不能和 trade_no同时为空。
	 */
	private String outTradeNo;
	
	/**
	 * 请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号: 同一笔交易部分多次退款，需保证唯一
	 */
	private String outRequestNo;
	
	/**
	 * 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
	 */
	private BigDecimal refundAmount;
	
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOutRequestNo() {
		return outRequestNo;
	}

	public void setOutRequestNo(String outRequestNo) {
		this.outRequestNo = outRequestNo;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

}
