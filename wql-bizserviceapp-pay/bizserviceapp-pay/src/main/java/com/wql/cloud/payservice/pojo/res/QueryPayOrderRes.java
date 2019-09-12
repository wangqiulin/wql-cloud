package com.wql.cloud.payservice.pojo.res;

import java.math.BigDecimal;
import java.util.Date;

public class QueryPayOrderRes {

	/**
	 * 支付状态：1待支付，2支付成功，3支付失败，4支付取消，5支付超时
	 */
	private Integer payState;
	
	/**
	 * 实际支付金额
	 */
	private BigDecimal payAmount; 
	
	/**
	 * 支付完成时间
	 */
	private Date payedTime;

	/**
	 * 支付结果描述
	 */
	private String payDesc;

	public Integer getPayState() {
		return payState;
	}

	public void setPayState(Integer payState) {
		this.payState = payState;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Date getPayedTime() {
		return payedTime;
	}

	public void setPayedTime(Date payedTime) {
		this.payedTime = payedTime;
	}

	public String getPayDesc() {
		return payDesc;
	}

	public void setPayDesc(String payDesc) {
		this.payDesc = payDesc;
	}
	
}
