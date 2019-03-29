package com.wql.cloud.gateway.property;

import org.springframework.stereotype.Component;

import com.xxl.conf.core.annotation.XxlConf;

@Component
public class GatewayProperty {

	/**
	 * ip白名单，多个以逗号隔开
	 */
	@XxlConf("gateway.white.ips")
	public String whiteIps;
	
	/**
	 * 放行的url
	 */
	@XxlConf("gateway.white.urls")
	public String whiteUrls;
	
}
