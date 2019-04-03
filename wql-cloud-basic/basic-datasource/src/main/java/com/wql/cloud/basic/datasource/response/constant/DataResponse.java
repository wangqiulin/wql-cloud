package com.wql.cloud.basic.datasource.response.constant;

import java.io.Serializable;

/**
 * 数据响应类
 * 
 * @author wangqiulin
 *
 */
public class DataResponse<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 响应code
	 */
	private String code;

	/**
	 * 响应描述
	 */
	private String msg;

	/**
	 * 返回数据
	 */
	private T data;

	public DataResponse() {
	}

	public DataResponse(BusinessEnum busEnum) {
		this.code = busEnum.getCode();
		this.msg = busEnum.getMsg();
	}

	public DataResponse(BusinessEnum busEnum, T data) {
		this.code = busEnum.getCode();
		this.msg = busEnum.getMsg();
		this.data = data;
	}

	public DataResponse(String code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public DataResponse(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
