package com.wql.cloud.gateway.core.manage;

import com.netflix.zuul.context.RequestContext;
import com.wql.cloud.gateway.core.factory.FilterFactory;
import com.wql.cloud.gateway.core.model.FilterResponse;

/**
 * 过滤器管理接口
 */
public interface FilterManager {

	/**
	 * 执行过滤列表
	 * 
	 * @param ctx
	 * @return
	 */
	public FilterResponse doFilter(RequestContext ctx);

	/**
	 * 设置过滤器工厂
	 * 
	 * @param filterFactory
	 */
	public void setFilterFactory(FilterFactory filterFactory);

	/**
	 * 获得过滤器工厂
	 * 
	 * @return
	 */
	public FilterFactory getFilterFactory();

}
