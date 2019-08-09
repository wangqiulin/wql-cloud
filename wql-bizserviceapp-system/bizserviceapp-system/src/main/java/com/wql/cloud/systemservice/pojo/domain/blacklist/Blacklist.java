package com.wql.cloud.systemservice.pojo.domain.blacklist;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_gateway_blacklist")
public class Blacklist extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String blackIp;

	private Integer blackState;

	public String getBlackIp() {
		return blackIp;
	}

	public void setBlackIp(String blackIp) {
		this.blackIp = blackIp;
	}

	public Integer getBlackState() {
		return blackState;
	}

	public void setBlackState(Integer blackState) {
		this.blackState = blackState;
	}

}
