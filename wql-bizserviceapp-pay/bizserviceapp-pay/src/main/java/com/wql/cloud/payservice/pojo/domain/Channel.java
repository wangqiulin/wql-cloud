package com.wql.cloud.payservice.pojo.domain;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_pay_channel")
public class Channel extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 支付方式简称
	 */
	private String paymentWay;
	
	/**
	 * 支付渠道名称
	 */
	private String channelName;

	/**
	 * 支付渠道简称
	 */
	private String channelWay;

	public String getPaymentWay() {
		return paymentWay;
	}

	public void setPaymentWay(String paymentWay) {
		this.paymentWay = paymentWay;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelWay() {
		return channelWay;
	}

	public void setChannelWay(String channelWay) {
		this.channelWay = channelWay;
	}

}