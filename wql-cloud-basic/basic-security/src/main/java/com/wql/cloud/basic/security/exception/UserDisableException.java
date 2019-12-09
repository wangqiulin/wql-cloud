package com.wql.cloud.basic.security.exception;

public class UserDisableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserDisableException(String msg) {
        super(msg);
    }
}
