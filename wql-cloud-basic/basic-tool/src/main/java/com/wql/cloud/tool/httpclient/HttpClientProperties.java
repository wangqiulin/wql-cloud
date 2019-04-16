package com.wql.cloud.tool.httpclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * redisson配置
 * 
 * @auther wangqiulin
 * @date 2018/10/11
 */
@Configuration
public class HttpClientProperties {

	@Value("${http.connectTimeout:3000}")
	private int connectTimeout;
	
	@Value("${http.socketTimeout:24000}")
	private int socketTimeout;
	
	@Value("${http.maxConnTotalInt:10}")
	private int maxConnTotalInt;
	
	@Value("${http.maxConnPerRouteInt:40}")
	private int maxConnPerRouteInt;
	
	@Value("${http.connectionRequestTimeoutInt:3000}")
	private int connectionRequestTimeoutInt;

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getMaxConnTotalInt() {
		return maxConnTotalInt;
	}

	public void setMaxConnTotalInt(int maxConnTotalInt) {
		this.maxConnTotalInt = maxConnTotalInt;
	}

	public int getMaxConnPerRouteInt() {
		return maxConnPerRouteInt;
	}

	public void setMaxConnPerRouteInt(int maxConnPerRouteInt) {
		this.maxConnPerRouteInt = maxConnPerRouteInt;
	}

	public int getConnectionRequestTimeoutInt() {
		return connectionRequestTimeoutInt;
	}

	public void setConnectionRequestTimeoutInt(int connectionRequestTimeoutInt) {
		this.connectionRequestTimeoutInt = connectionRequestTimeoutInt;
	}

}
