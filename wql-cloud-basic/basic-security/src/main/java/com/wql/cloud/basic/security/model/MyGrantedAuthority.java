package com.wql.cloud.basic.security.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * 授权信息
 */
public class MyGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 4923044641281645425L;
	
	/**
	 * 请求链接
	 */
	private String url;

	@Override
	public String getAuthority() {
		return url;
	}

    public void setAuthority(String url) {
	    this.url = url;
    }

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
}
