package com.wql.cloud.gateway.core.enums;

/**
 * 过滤器响应枚举值
 */
public enum FilterResponseEnum {

	SUCCESS("success", "成功"), FAIL("failure", "失败"), ERROR("error", "错误");

	private String code;

	private String value;

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
