package com.wql.cloud.basic.push.channel.huawei.template;

import java.util.Map;

import com.wql.cloud.basic.push.route.template.PushTemplate;

/**
 * 华为push模板
 */
public class HwPushTemplate extends PushTemplate {

    private String appSecret;

    private String appId;


    /**
     * intent://www.wisdom.push/pushActivity?pushMessage=" +
     *                 "{0}#Intent;scheme=qdx;launchFlags=0x4000000;end"
     */
    private String intent;

    /**
     * 需要拉起的应用包名，必须和注册推送的包名一致。
     */
    private String appPkgName;

    public HwPushTemplate(String appSecret, String appId, String intent, String appPkgName) {
        this.appSecret = appSecret;
        this.appId = appId;
        this.intent = intent;
        this.appPkgName = appPkgName;
    }

    public HwPushTemplate(String messageId, String title, String router, String content, Map<String, String> params, String appSecret, String appId, String intent, String appPkgName) {
        super(messageId, title, router, content, params);
        this.appSecret = appSecret;
        this.appId = appId;
        this.intent = intent;
        this.appPkgName = appPkgName;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getAppPkgName() {
        return appPkgName;
    }

    public void setAppPkgName(String appPkgName) {
        this.appPkgName = appPkgName;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

}
