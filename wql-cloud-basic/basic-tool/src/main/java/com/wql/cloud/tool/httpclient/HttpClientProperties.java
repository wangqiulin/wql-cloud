package com.wql.cloud.tool.httpclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public int getMaxConnTotalInt() {
		return maxConnTotalInt;
	}

	public int getMaxConnPerRouteInt() {
		return maxConnPerRouteInt;
	}

	public int getConnectionRequestTimeoutInt() {
		return connectionRequestTimeoutInt;
	}

}
