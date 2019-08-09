package com.wql.cloud.systemservice.pojo.domain.microserviceconfig;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_system_microservice_config")
public class MicroserviceConfig extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String serviceName;

	private String serviceUrl;

	private Integer serviceExecute;

	private Integer serviceState;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public Integer getServiceExecute() {
		return serviceExecute;
	}

	public void setServiceExecute(Integer serviceExecute) {
		this.serviceExecute = serviceExecute;
	}

	public Integer getServiceState() {
		return serviceState;
	}

	public void setServiceState(Integer serviceState) {
		this.serviceState = serviceState;
	}

}
