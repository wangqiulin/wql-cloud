package com.wql.cloud.basic.push.channel.vivo.template;

import java.util.Map;

import com.wql.cloud.basic.push.route.template.PushTemplate;

/**
 * Vivo push模板
 */
public class VivoPushTemplate extends PushTemplate {

    /**
     * 用户申请推送业务时获得的 appKey
     */
    private String appKey;

    /**
     * 用户申请推送业务时生成的 appId
     */
    private String appId;

    private String appSecret;

    private String intent;

    public VivoPushTemplate(String appKey, String appId, String appSecret, String intent) {
        this.appKey = appKey;
        this.appId = appId;
        this.appSecret = appSecret;
        this.intent = intent;
    }

    public VivoPushTemplate(String messageId, String title, String router, String content, Map<String, String> params, String appKey, String appId, String appSecret, String intent) {
        super(messageId, title, router, content, params);
        this.appKey = appKey;
        this.appId = appId;
        this.appSecret = appSecret;
        this.intent = intent;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

}
