package com.wql.cloud.basic.push.route.vo;

public class MessagePushConfig {

	/** 配置名称 */
	private String pushConfigName;

	/** 配置类型 0小米 1个推 */
	private Integer pushConfigType;

	/** appId */
	private String pushAppId;

	/** appKey */
	private String pushAppKey;

	/** sercret */
	private String pushSecretKey;

	/** 包名（小米） */
	private String pushPackageName;

	/** 个推url */
	private String pushGtUrl;

	/** 业务code */
	private String businessCode;

	/** 跳转页 */
	private String activity;

	private String intent;

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public void setPushConfigName(String pushConfigName) {
		this.pushConfigName = pushConfigName;
	}

	public String getPushConfigName() {
		return pushConfigName;
	}

	public void setPushConfigType(Integer pushConfigType) {
		this.pushConfigType = pushConfigType;
	}

	public Integer getPushConfigType() {
		return pushConfigType;
	}

	public void setPushAppId(String pushAppId) {
		this.pushAppId = pushAppId;
	}

	public String getPushAppId() {
		return pushAppId;
	}

	public void setPushAppKey(String pushAppKey) {
		this.pushAppKey = pushAppKey;
	}

	public String getPushAppKey() {
		return pushAppKey;
	}

	public void setPushSecretKey(String pushSecretKey) {
		this.pushSecretKey = pushSecretKey;
	}

	public String getPushSecretKey() {
		return pushSecretKey;
	}

	public void setPushPackageName(String pushPackageName) {
		this.pushPackageName = pushPackageName;
	}

	public String getPushPackageName() {
		return pushPackageName;
	}

	public void setPushGtUrl(String pushGtUrl) {
		this.pushGtUrl = pushGtUrl;
	}

	public String getPushGtUrl() {
		return pushGtUrl;
	}

}
