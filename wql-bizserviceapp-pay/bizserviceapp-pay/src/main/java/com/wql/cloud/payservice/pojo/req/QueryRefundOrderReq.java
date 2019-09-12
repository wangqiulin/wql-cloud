package com.wql.cloud.payservice.pojo.req;

public class QueryRefundOrderReq {

	/**
	 * 应用id
	 */
	private String appId;

	/**
	 * 订单号
	 */
	private String orderNo;

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

}
