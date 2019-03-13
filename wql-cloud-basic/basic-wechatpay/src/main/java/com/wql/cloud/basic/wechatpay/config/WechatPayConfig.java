package com.wql.cloud.basic.wechatpay.config;

/**
 * 微信支付相关配置
 */
public interface WechatPayConfig {

    default String getPlaceOrderServerUrl() {
        return "https://api.mch.weixin.qq.com/pay/unifiedorder";
    }
    
    default String getQueryOrderServerUrl() {
        return "https://api.mch.weixin.qq.com/pay/orderquery";
    }

    /**应用ID*/
    String getAppId();
    
    /**商户号*/
    String getMchId();

    /**私钥*/
    String getPrivateKey();

    /**支付回调地址*/
    String getPayNotifyUrl();

    /**退款回调地址 */
    String getRefundNotifyUrl();

}
