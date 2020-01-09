package com.wql.cloud.adapter.app.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * 适配层错误码中心
 * 
 * errorCode:错误编码（6位数字）
 * <p>
 * <li>错误码分类</li>
 * <li>公共错误码 以 [10]开头</li>
 * <li>参数基本校验错误码以[20]开头</li>
 * <li>业务错误码以[30]开头</li>
 * </p>
 * @author xiaojun
 *
 */
public enum CreditAdaptorErrorCode {

	/*********************** 公共错误码10开头 *********************/
	SUCCESS("success", "处理成功"), 
	FAIL("error", "系统异常"),
	NVOKE_SERVICE_BEAN_FAIL("100002", "获取服务层对象失败"),
	INVOKE_SERVICE_METHOD_FAIL("100003", "调用远程服务失败"),
	TIMEOUT_FAIL("100004", "超时失败"),

	/*********************** 参数基本校验错误码以[20]开头 *********************/
	REQUEST_PARAMS_VALIDATE_IS_ERROR("200001","请求参数不能为null"),
	PICTURE_UPLOAD_IS_NOT_NULL("200002", "上传图片参数不能为空！"),
	SIGN_IS_ERROR("200003", "参数签名不正确！"),
	API_KEY_IS_ERROR("200004", "请求方法不正确！"),

	/*********************** 业务错误码以[30]开头 *********************/
	REQUEST_VALIDATE_LOGIN_SATE_IS_ERROR("300001","请求登录校验服务异常"),
	REQUEST_VALIDATE_LOGIN_SATE_CODE_IS_ERROR("300002","登录校验返回响应码不合法"),
	LOGIN_PARAMS_VALIDATE_ERROR("300003","登录校验参数异常"),
	H5_ACCESS_NOT_ALLOW("300004","访问拒绝"),
	FILE_SAVE_TO_SERVER_FAIL("300005", "保存文件到服务器失败!"),
	FILE_UPLOAD_TO_COS_FAIL("300006", "文件上传到OSS失败!"),
	FILE_FORMAT_ERROR("300007", "上传文件格式错误"),
	FILE_SIZE_ERROR("300008", "上传文件大小不超过10M"),
	READ_FILE_STREAM_FAIL("300009", "读取文件流失败"),
	FILE_DIR_NOT_EXIST("300010", "上传文件路径不存在"),
	TOKEN_ERROR("300011", "登录已过期，请重新登录！"),
	USER_FREEZE("300012", "您的账户已被冻结，具体详情请关注微信公众号咨询！"),
	USER_SQUEEZE("300013", "您的账号在其他设备上登录！"),
	;

	private String errorCode;

	private String errorMsg;

	private CreditAdaptorErrorCode(String errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	/**
	 * 
	 * @return
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * 
	 * @return
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * 通过枚举<code>code</code>获得枚举
	 * 
	 * @param code
	 * @return
	 */
	public static CreditAdaptorErrorCode getByCode(String code) {
		for (CreditAdaptorErrorCode value : values()) {
			if (StringUtils.equals(value.getErrorCode(), code)) {
				return value;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "errorCode[" + getErrorCode() + "],errorMsg[" + getErrorMsg() + "]";
	}
}
