package com.wql.cloud.basic.security.model;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * 登陆Token Bean
 */
public class MyAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 3231188977826819121L;

	public MyAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}

	public MyAuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}
}
