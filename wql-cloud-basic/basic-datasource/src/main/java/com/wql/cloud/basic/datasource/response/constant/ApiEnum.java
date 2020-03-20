package com.wql.cloud.basic.datasource.response.constant;

/**
 * 系统枚举类
 * 
 * @author wangqiulin
 *
 */
public enum ApiEnum {

	SUCCESS("success", "处理成功"),
	FAILURE("failure", "处理失败"), 
	SYSTEM_FAIL("error", "服务开小差"), 
	PARAM_FAIL("param_error", "参数异常"), 
	NOT_LOGIN("no_login", "未登录"),
	
	;
	
	private String code;
	
	private String message;

	private ApiEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

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
	
}
