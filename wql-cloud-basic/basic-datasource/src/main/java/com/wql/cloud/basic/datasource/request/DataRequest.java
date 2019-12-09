package com.wql.cloud.basic.datasource.request;

import java.io.Serializable;

public class DataRequest<T> implements Serializable {

	private static final long serialVersionUID = 1069045630658643720L;

	/** 用户code */
	private String userCode = null;

	/** 系统级参数 */
	private SystemParam system = null;

	/** 数据 */
	private T data = null;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public SystemParam getSystem() {
		return system;
	}

	public void setSystem(SystemParam system) {
		this.system = system;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
