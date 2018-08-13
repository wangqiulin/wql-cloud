package com.wql.cloud.basic.redis.lock;
/**
 * 异常类
 * 
 * @author wangqiulin
 * @date 2018年5月16日
 */
public class UnableToAquireLockException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnableToAquireLockException() {
    }

    public UnableToAquireLockException(String message) {
        super(message);
    }

    public UnableToAquireLockException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
