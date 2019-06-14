package com.wql.cloud.basic.wechatpay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WechatPayConfig {

	/** 创建支付订单url */
	public final String PLACE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/** 查询支付结果url */
	public final static String QUERY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	/** 退款url */
	public final String REFUND_ORDER_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	/** 查询退款结果url */
	public final static String QUERY_REFUND_ORDER_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	
	/**
	 * 微信应用appId
	 */
	@Value("${wxpay.appId:}")
	private String appId;

	/**
	 * 微信分配的应用appId下的商户号
	 */
	@Value("${wxpay.mcdId:}")
	private String mchId;

	/**
	 * 微信支付的私钥
	 */
	@Value("${wxpay.privateKey:}")
	private String privateKey;

	/**
	 * 支付成功回调地址
	 */
	@Value("${wxpay.payNotifyUrl:}")
	private String payNotifyUrl;

	/**
	 * 退款成功回调地址
	 */
	@Value("${wxpay.refundNotifyUrl:}")
	private String refundNotifyUrl;

	/**
	 * 退款操作的证书
	 */
	@Value("${wxpay.refund.certLocalPath:}")
	private String certLocalPath;
	
	public String getAppId() {
		return appId;
	}

	public String getMchId() {
		return mchId;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public String getPayNotifyUrl() {
		return payNotifyUrl;
	}

	public String getRefundNotifyUrl() {
		return refundNotifyUrl;
	}

	public String getCertLocalPath() {
		return certLocalPath;
	}

}
