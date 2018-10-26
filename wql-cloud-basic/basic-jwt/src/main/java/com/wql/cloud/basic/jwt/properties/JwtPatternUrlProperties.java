package com.wql.cloud.basic.jwt.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt.exclude")
public class JwtPatternUrlProperties {

	private List<String> urlPatterns;

	public List<String> getUrlPatterns() {
		return urlPatterns;
	}

	public void setUrlPatterns(List<String> urlPatterns) {
		this.urlPatterns = urlPatterns;
	}
	
}
