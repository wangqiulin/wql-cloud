package com.wql.cloud.payservice.pojo.res;

import java.util.Date;

public class QueryRefundOrderRes {

	private Date refundTime;
	
	private String refundStatus;

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	
}
