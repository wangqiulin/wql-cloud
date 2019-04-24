package com.wql.cloud.gateway.property;

import org.springframework.stereotype.Component;

@Component
public class GatewayProperty {

	/**
	 * 黑名单开关
	 */
	//@XxlConf("gateway.black.switch")
	public Boolean isBlacklistSwitch = true;
	
	/**
	 * 白名单开关
	 */
	//@XxlConf("gateway.white.switch")
	public Boolean isWhitelistSwitch = true;
	
	/**
	 * ip白名单，多个以逗号隔开
	 */
	//@XxlConf("gateway.white.ips")
	public String whiteIps;
	
	/**
	 * 放行的url
	 */
	//@XxlConf("gateway.white.urls")
	public String whiteUrls;
	
}
