package com.wql.cloud.basic.oss.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChuangLanConfig {

	@Value("${chuanglan.oneclicklogin.cmccUrl:https://api.253.com/open/flashsdk/mobile-query-m}")
	private String cmccUrl;
	
	@Value("${chuanglan.oneclicklogin.cuccUrl:https://api.253.com/open/flashsdk/mobile-query-u}")
	private String cuccUrl;
	
	@Value("${chuanglan.oneclicklogin.ctccUrl:https://api.253.com/open/flashsdk/mobile-query-t}")
	private String ctccUrl;
	
	@Value("${chuanglan.oneclicklogin.iosAppKey:}")
	private String iosAppKey;
	
	@Value("${chuanglan.oneclicklogin.androidAppKey:}")
	private String androidAppKey;

	public String getCmccUrl() {
		return cmccUrl;
	}

	public void setCmccUrl(String cmccUrl) {
		this.cmccUrl = cmccUrl;
	}

	public String getCuccUrl() {
		return cuccUrl;
	}

	public void setCuccUrl(String cuccUrl) {
		this.cuccUrl = cuccUrl;
	}

	public String getCtccUrl() {
		return ctccUrl;
	}

	public void setCtccUrl(String ctccUrl) {
		this.ctccUrl = ctccUrl;
	}

	public String getIosAppKey() {
		return iosAppKey;
	}

	public void setIosAppKey(String iosAppKey) {
		this.iosAppKey = iosAppKey;
	}

	public String getAndroidAppKey() {
		return androidAppKey;
	}

	public void setAndroidAppKey(String androidAppKey) {
		this.androidAppKey = androidAppKey;
	}
	
}
