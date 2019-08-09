package com.wql.cloud.gateway.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class GatewayProperty {

	/**
	 * 放行url, 格式: {"routeUrl":"serviceId", "routeUrl":"serviceId"}
	 */
	@Value("${gateway.pass.urls:}")
	private String passUrls;

	public String getPassUrls() {
		return passUrls;
	}

}
