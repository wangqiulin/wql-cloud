package com.wql.cloud.gateway.property;

import org.springframework.stereotype.Component;

@Component
public class GatewayProperty {

	/**
	 * 黑名单开关
	 */
	//@XxlConf("gateway.black.switch")
	public Boolean isBlacklistSwitch = false;
	
	/**
	 * 白名单开关
	 */
	//@XxlConf("gateway.white.switch")
	public Boolean isWhitelistSwitch = false;
	
}
