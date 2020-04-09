package com.wql.cloud.basic.datasource.response.exception;

import org.springframework.util.Assert;

import com.wql.cloud.basic.datasource.response.constant.ApiEnum;

public class Asserts extends Assert {

	public static void fail(String message) {
        throw new ApiException(ApiEnum.FAILURE.getCode(), message);
    }

    public static void fail(ApiEnum errorCode) {
        throw new ApiException(errorCode);
    }
    
    public static void fail(String code, String message) {
        throw new ApiException(code, message);
    }

}
