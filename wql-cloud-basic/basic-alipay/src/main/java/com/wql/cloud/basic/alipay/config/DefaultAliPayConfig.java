package com.wql.cloud.basic.alipay.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * 支付宝配置默认实现类
 */
@Service
public class DefaultAliPayConfig implements AliPayConfig {

    private static final String PREFIX = "alipay.";

    @Autowired
    private Environment env;

    @Override
    public String getAppId() {
        return env.getProperty(PREFIX.concat("appId"));
    }

    @Override
    public String getPrivateKey() {
        return env.getProperty(PREFIX.concat("privateKey"));
    }

    @Override
    public String getPublicKey() {
        return env.getProperty(PREFIX.concat("publicKey"));
    }

    @Override
    public String getSignType() {
        return env.getProperty(PREFIX.concat("signType"));
    }

    @Override
    public String getPayNotifyUrl()  {
        return env.getProperty(PREFIX.concat("payNotifyUrl"));
    }

    @Override
    public String getRefundNotifyUrl()   {
        return env.getProperty(PREFIX.concat("refundNotifyUrl"));
    }
}
