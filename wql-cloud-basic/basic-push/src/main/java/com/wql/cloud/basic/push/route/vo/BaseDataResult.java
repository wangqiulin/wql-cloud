package com.wql.cloud.basic.push.route.vo;

/**
 * 推送消息内部返回结果类
 */
public abstract class BaseDataResult {

	protected boolean success;
	protected String data;
	protected String message;

	protected BaseDataResult() {
		super();
	}

	protected BaseDataResult(String data) {
		this.data = data;
	}

	protected BaseDataResult(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	protected BaseDataResult(boolean success, String message, String data) {
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
