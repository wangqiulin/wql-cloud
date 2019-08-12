package com.wql.cloud.basic.alipay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AliPayConfig {

	public final String SERVER_URL = "https://openapi.alipay.com/gateway.do";
	public final String FORMAT = "json";
	public final String CHARSET = "utf-8";
	
	/**
	 * 支付宝应用id
	 */
	@Value("${alipay.appId:}")
	private String appId;
	
	/**
	 * 商户密钥
	 */
	@Value("${alipay.privateKey:}")
	private String privateKey;
	
	/**
	 * 支付宝公钥
	 */
	@Value("${alipay.publicKey:}")
	private String publicKey;
	
	/**
	 * 签名方式，一般为RSA2
	 */
	@Value("${alipay.signType:RSA2}")
	private String signType;
	
	/**
	 * 支付成功回调地址
	 */
	@Value("${alipay.payNotifyUrl:}")
	private String payNotifyUrl;
	
	/**
	 * 退款成功回调地址
	 */
	@Value("${alipay.refundNotifyUrl:}")
	private String refundNotifyUrl;

	public String getAppId() {
		return appId;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public String getSignType() {
		return signType;
	}

	public String getPayNotifyUrl() {
		return payNotifyUrl;
	}

	public String getRefundNotifyUrl() {
		return refundNotifyUrl;
	}
	
}
