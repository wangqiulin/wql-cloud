package com.wql.cloud.gateway.core.enums;

/**
 * 过滤器是否校验枚举
 */
public enum InnerFilterCheckEnum {

	IS_CHECK("1", "校验"), IS_NOT_CHECK("0", "不校验");

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
	private InnerFilterCheckEnum(String code, String value) {
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
