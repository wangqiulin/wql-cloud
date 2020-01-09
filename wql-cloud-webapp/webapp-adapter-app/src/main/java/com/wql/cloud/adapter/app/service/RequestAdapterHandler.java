package com.wql.cloud.adapter.app.service;

/**
 * 请求基础处理接口模板
 */
public interface RequestAdapterHandler<T> {

	/**
	 * 请求对象转换方法
	 *
	 * @param t
	 *            入参json字符串
	 * @return 返回请求结果转换对象即java服务入参对象
	 */
	public Object convert(T t);

}
