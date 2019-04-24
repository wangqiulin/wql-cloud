package com.wql.cloud.gateway.core.model;

import java.io.Serializable;

/**
 * 请求Api路由类
 */
public class Route implements Serializable {

	private static final long serialVersionUID = 6554619872331937343L;
	
	/**
	 * 路由方式(目前支持ribbon http)
	 */
	private String routeMode;
	
	/**
	 * 服务ID(ribbon时使用)
	 */
	private String routeServiceId;
	
	/**
	 * 请求路径
	 */
	private String routePath;

	public String getRouteMode() {
		return routeMode;
	}

	public void setRouteMode(String routeMode) {
		this.routeMode = routeMode;
	}

	public String getRouteServiceId() {
		return routeServiceId;
	}

	public void setRouteServiceId(String routeServiceId) {
		this.routeServiceId = routeServiceId;
	}

	public String getRoutePath() {
		return routePath;
	}

	public void setRoutePath(String routePath) {
		this.routePath = routePath;
	}


}
