package com.wql.cloud.gateway.core.enums;

/**
 * 请求类型枚举类
 */
public enum RouteModeEnum {

	RIBBON("0", "ribbon"), HTTP("1", "http");

	/**
	 * 枚举编号
	 */
	private String code;

	/**
	 * 枚举值
	 */
	private String value;

	/**
	 * 构造方法
	 * 
	 * @param code
	 * @param value
	 */
	private RouteModeEnum(String code, String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
