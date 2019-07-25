package com.wql.cloud.basic.wechatpay.util;

/**
 * 微信支付的常量
 * @author wangqiulin
 *
 */
public interface WXPayConstant {
	
	public enum SignType {
        MD5, HMACSHA256
    }
	
	public static final String SUCCESS_CODE = "SUCCESS";
	public static final String FAIL_CODE = "FAIL";
	public static final String APPID = "appid";
	public static final String MCH_ID = "mch_id";
	public static final String PARTNERID = "partnerid";
	public static final String NONCE_STR = "nonce_str";
	public static final String SIGN = "sign";
	public static final String OUT_TRADE_NO = "out_trade_no";
	public static final String NOTIFY_URL = "notify_url";
	public static final String TRADE_TYPE = "trade_type";
	public static final String LIMIT_PAY = "limit_pay";
	public static final String FEE_TYPE = "fee_type";
	public static final String DEVICE_INFO = "device_info";
	public static final String DETAIL = "detail";
	public static final String ATTACH = "attach";
	public static final String GOODS_TAG = "goods_tag";
	public static final String TIME_START = "time_start";
	public static final String TIME_EXPIRE = "time_expire";
	public static final String TOTAL_FEE = "total_fee";
	public static final String SPBILL_CREATE_IP = "spbill_create_ip";
	public static final String PREPAYID = "prepayid";
	public static final String PACKAGE_KEY = "package";
	public static final String TRANSACTION_ID = "transaction_id";
	public static final String OUT_REFUND_NO = "out_refund_no";
	public static final String OP_USER_ID = "op_user_id";
	public static final String REFUND_FEE = "refund_fee";
	public static final String BODY = "body";
	public static final String RETURN_CODE = "return_code";
	public static final String RESULT_CODE = "result_code";
	public static final String RETURN_MSG = "return_msg";
	public static final String PREPAY_ID = "prepay_id";
	public static final String BANK_TYPE = "bank_type";
	public static final String ERR_CODE = "err_code";
	public static final String ERR_CODE_DES = "err_code_des";
	public static final String OPENID = "openid";
	public static final String TIME_END = "time_end";
	public static final String XML_ROOT = "xml";
	public static final String REFUND_ID = "refund_id";
	public static final String noncestr = "noncestr";
	public static final String timestamp = "timestamp";
}
