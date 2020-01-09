package com.wql.cloud.adapter.app.service;

/**
 * 响应基础处理接口模板
 */
public interface ResponseAdapterHandler<T> {

	/**
	 * 响应对象转换方法
	 * 
	 * @param t
	 *            入参数为SoaResponse对象
	 * @return 返回结果转换对象即APP/H5服务出参对象
	 */
	public Object convert(T t);

}
