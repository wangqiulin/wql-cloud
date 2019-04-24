package com.wql.cloud.gateway.core.model;

import com.wql.cloud.gateway.core.enums.FilterResponseEnum;

/**
 * 过滤器响应类
 */
public class FilterResponse {

	/**
	 * 返回码默认过滤成功
	 */
	private String code = FilterResponseEnum.SUCCESS.getCode();
	/**
	 * 返回信息
	 */
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "FilterResponse [code=" + code + ", message=" + message + "]";
	}

}
