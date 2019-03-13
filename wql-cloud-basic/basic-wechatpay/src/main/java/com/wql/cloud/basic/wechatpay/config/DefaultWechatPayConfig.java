package com.wql.cloud.basic.wechatpay.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * 微信支付配置默认实现类
 */
@Service
public class DefaultWechatPayConfig implements WechatPayConfig {

    private static final String PREFIX = "wxpay.";

    @Autowired
    private Environment env;

    @Override
    public String getAppId() {
        return env.getProperty(PREFIX.concat("appId"));
    }
    
    @Override
    public String getMchId() {
        return env.getProperty(PREFIX.concat("mchId"));
    }

    @Override
    public String getPrivateKey() {
        return env.getProperty(PREFIX.concat("privateKey"));
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
