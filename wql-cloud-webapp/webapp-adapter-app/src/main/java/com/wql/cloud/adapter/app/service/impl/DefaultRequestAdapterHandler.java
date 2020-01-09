package com.wql.cloud.adapter.app.service.impl;

import org.springframework.stereotype.Service;

import com.wql.cloud.adapter.app.service.RequestAdapterHandler;

/**
 * 默认请求转换
 * @param <T>
 */
@Service("defaultRequestAdapterHandler")
public class DefaultRequestAdapterHandler<T> implements RequestAdapterHandler<T> {

	@Override
	public Object convert(T t) {
		return t;
	}

}
