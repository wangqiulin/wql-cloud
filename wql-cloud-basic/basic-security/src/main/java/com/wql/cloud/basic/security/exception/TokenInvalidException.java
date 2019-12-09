package com.wql.cloud.basic.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Token 无效异常
 */
public class TokenInvalidException extends AuthenticationException {

	private static final long serialVersionUID = -2070712601391806606L;

	public TokenInvalidException(String msg) {
		super(msg);
	}

}
