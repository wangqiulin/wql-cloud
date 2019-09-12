package com.wql.cloud.payservice.pojo.domain;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_pay_payment")
public class Payment extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 支付方式名称
	 */
	private String paymentName;

	/**
	 * 支付方式简称
	 */
	private String paymentWay;

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public String getPaymentWay() {
		return paymentWay;
	}

	public void setPaymentWay(String paymentWay) {
		this.paymentWay = paymentWay;
	}

}