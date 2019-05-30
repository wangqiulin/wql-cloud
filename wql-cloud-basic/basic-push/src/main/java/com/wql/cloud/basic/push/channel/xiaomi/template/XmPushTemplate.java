package com.wql.cloud.basic.push.channel.xiaomi.template;

import java.util.Map;

import com.wql.cloud.basic.push.route.template.PushTemplate;

/**
 * 小米push模板
 */
public class XmPushTemplate extends PushTemplate {


    private String appId;

    private String appKey;

    private String appSecret;

    private String packageName;

    private String intent;

    public XmPushTemplate(String messageId, String title, String router, String content, Map<String, String> params, String appId, String appKey, String appSecret, String packageName, String intent) {
        super(messageId, title, router, content, params);
        this.appId = appId;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.packageName = packageName;
        this.intent = intent;
    }

    public XmPushTemplate(String appId, String appKey, String appSecret, String packageName, String intent) {
        this.appId = appId;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.packageName = packageName;
        this.intent = intent;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

}
