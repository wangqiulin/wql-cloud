package com.wql.cloud.basic.alipay.config;

/**
 * 支付宝支付相关配置
 */
public interface AliPayConfig {

    /**获取支付宝服务端地址*/
    default String getServerUrl() {
        return "https://openapi.alipay.com/gateway.do";
    }

    default String getFormat() {
        return "json";
    }
    
    default String getCharset() {
        return "UTF-8";
    }

    /**应用ID*/
    String getAppId();

    /**应用私钥*/
    String getPrivateKey();

    /**支付宝公钥*/
    String getPublicKey();

    /**加密方式*/
    String getSignType();

    /**支付回调地址*/
    String getPayNotifyUrl();

    /**退款回调地址 */
    String getRefundNotifyUrl();

}
