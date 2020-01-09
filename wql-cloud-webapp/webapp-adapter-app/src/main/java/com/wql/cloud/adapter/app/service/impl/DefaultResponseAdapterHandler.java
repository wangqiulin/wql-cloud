package com.wql.cloud.adapter.app.service.impl;

import org.springframework.stereotype.Service;

import com.wql.cloud.adapter.app.service.ResponseAdapterHandler;

/**
 * 默认Response转换
 * @param <T>
 */
@Service("defaultResponseAdapterHandler")
public class DefaultResponseAdapterHandler<T> implements ResponseAdapterHandler<T> {

	@Override
	public Object convert(T t) {
		return t;
	}

}
