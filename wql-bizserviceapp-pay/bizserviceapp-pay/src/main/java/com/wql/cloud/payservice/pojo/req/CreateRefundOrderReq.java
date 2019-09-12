package com.wql.cloud.payservice.pojo.req;

import java.math.BigDecimal;

public class CreateRefundOrderReq {
	
	/**
	 * 应用id
	 */
	private String appId;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 需要退款的金额，如果为空则取支付金额
	 */
	private BigDecimal refundAmount;
	
	/**
	 * 退款异步通知地址
	 */
	private String notifyUrl;
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	
}
