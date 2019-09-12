package com.wql.cloud.payservice.pojo.domain;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_pay_mert")
public class Mert extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 商户id
	 */
	private String mertId;
	
	/**
	 * 商户名称
	 */
	private String mertName;
	
	/**
	 * 商户状态：0停用，1启用
	 */
	private Integer mertState;

	public String getMertId() {
		return mertId;
	}

	public void setMertId(String mertId) {
		this.mertId = mertId;
	}

	public String getMertName() {
		return mertName;
	}

	public void setMertName(String mertName) {
		this.mertName = mertName;
	}

	public Integer getMertState() {
		return mertState;
	}

	public void setMertState(Integer mertState) {
		this.mertState = mertState;
	}

}