package com.wql.cloud.gateway.core.enums;

/**
 * 过滤器响应枚举值
 */
public enum FilterResponseEnum {

	/**
	 * SUCCESS:成功 FAIL:失败
	 */
	SUCCESS("success", "成功"), FAIL("failure", "失败"), ERROR("error", "错误");

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
	private FilterResponseEnum(String code, String value) {
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
