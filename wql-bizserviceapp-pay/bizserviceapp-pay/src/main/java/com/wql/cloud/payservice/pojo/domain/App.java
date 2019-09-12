package com.wql.cloud.payservice.pojo.domain;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_pay_app")
public class App extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 商户id
	 */
	private String mertId;

	/**
	 * 应用名称
	 */
	private String appName;

	/**
	 * 应用id
	 */
	private String appId;

	/**
	 * app状态：0停用，1启用
	 */
	private Integer appState;

	public String getMertId() {
		return mertId;
	}

	public void setMertId(String mertId) {
		this.mertId = mertId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getAppState() {
		return appState;
	}

	public void setAppState(Integer appState) {
		this.appState = appState;
	}

}