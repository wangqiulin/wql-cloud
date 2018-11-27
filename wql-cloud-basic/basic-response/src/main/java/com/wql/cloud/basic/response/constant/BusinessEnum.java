package com.wql.cloud.basic.response.constant;

/**
 * 系统枚举类
 * 
 * @author wangqiulin
 *
 */
public enum BusinessEnum {

	/**
	 * 系统级别
	 */
	SUCCESS("0000", "处理成功"),
	FAIL("1000", "系统异常"), 
	PARAM_FAIL("1001", "参数异常"), 
	USER_NOT_LOGIN("1002", "请登录"),
	
	/**
	 * 业务级别
	 */
	USER_EXIST("100000", "用户已存在"),
	USER_NOT_EXIST("100001", "用户不存在"),
	USER_REGISTER_FAIL("100002", "注册失败"),
	USER_PWD_ERROR("100003", "密码错误"),
	
	;
	
	private String code;
	
	private String msg;

	private BusinessEnum(String code, String msg) {
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

	
}
