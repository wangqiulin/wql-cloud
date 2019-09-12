package com.wql.cloud.basic.alipay.model;

import java.math.BigDecimal;

public class CreateOrderModel {

	/**
	 * 交易订单号
	 */
	private String outTradeNo;
	
	/**
	 * 支付金额： 0.01
	 */
	private BigDecimal totalAmount;
	
	/**
	 * 商品描述
	 */
	private String body;
	
	/**
	 * 订单有效时间
	 * 	30m：表示30分钟
	 * 	1h: 表示1小时
	 */
	private String timeoutExpress;
	
	/**
	 * 支付宝H5返回地址
	 */
	private String returnUrl;

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTimeoutExpress() {
		return timeoutExpress;
	}

	public void setTimeoutExpress(String timeoutExpress) {
		this.timeoutExpress = timeoutExpress;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	
}
