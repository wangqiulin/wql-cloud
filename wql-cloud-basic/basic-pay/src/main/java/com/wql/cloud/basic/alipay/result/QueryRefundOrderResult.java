package com.wql.cloud.basic.alipay.result;

import java.util.Date;

public class QueryRefundOrderResult {

	/**
	 * 退款状态
	 */
	private String refundStatus;
	
	/**
	 * 退款时间
	 */
	private Date gmtRefundPay;

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public Date getGmtRefundPay() {
		return gmtRefundPay;
	}

	public void setGmtRefundPay(Date gmtRefundPay) {
		this.gmtRefundPay = gmtRefundPay;
	}

}
