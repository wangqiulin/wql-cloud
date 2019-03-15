package com.wql.cloud.basic.alipay.model;

public class CreateOrderModel {

	/**
	 * 交易订单号
	 */
	private String outTradeNo;
	
	/**
	 * 支付金额： 0.01
	 */
	private String totalAmount;
	
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

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
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
	
}
