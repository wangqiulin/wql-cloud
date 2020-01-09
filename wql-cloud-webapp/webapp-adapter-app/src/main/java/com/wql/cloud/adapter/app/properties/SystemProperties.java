package com.wql.cloud.adapter.app.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class SystemProperties {

	@Value("${sign.app.key}")
	private String appSignKey;

	@Value("${sign.h5.key}")
	private String h5SignKey;

	@Value("${remote.call.retry.cnt}")
	private Integer maxRetryCnt;

	@Value("${zuul.url}")
	private String zuulUrl;

	@Value("${log.traceId:traceId}")
	private String tranceId;

	public String getAppSignKey() {
		return appSignKey;
	}

	public String getH5SignKey() {
		return h5SignKey;
	}

	public Integer getMaxRetryCnt() {
		return maxRetryCnt;
	}

	public String getZuulUrl() {
		return zuulUrl;
	}

	public String getTranceId() {
		return tranceId;
	}

}
