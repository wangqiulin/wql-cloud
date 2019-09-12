package com.wql.cloud.payservice.pojo.domain;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_pay_apppayment")
public class AppPayment extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 应用id
	 */
	private String appId;

	/**
	 * app类型：iOS or Android
	 */
	private String appType;

	/**
	 * app版本
	 */
	private String appVersion;

	/**
	 * 支付方式
	 */
	private String paymentWay;

	/**
	 * 渠道方式
	 */
	private String channelWay;

	/**
	 * 支付方式排序, 值越小越靠前
	 */
	private Integer sort;

	/**
	 * 启用状态（0关闭，1启用）
	 */
	private Integer state;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getPaymentWay() {
		return paymentWay;
	}

	public void setPaymentWay(String paymentWay) {
		this.paymentWay = paymentWay;
	}

	public String getChannelWay() {
		return channelWay;
	}

	public void setChannelWay(String channelWay) {
		this.channelWay = channelWay;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}