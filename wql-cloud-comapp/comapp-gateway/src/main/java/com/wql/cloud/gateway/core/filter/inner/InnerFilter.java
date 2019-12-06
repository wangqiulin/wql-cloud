package com.wql.cloud.gateway.core.filter.inner;

import com.netflix.zuul.context.RequestContext;
import com.wql.cloud.gateway.core.model.FilterResponse;

/**
 * 内部过滤器接口
 */
public interface InnerFilter {

	/**
	 * 执行过滤规则
	 * 
	 * @param ctx
	 * @return
	 */
	public FilterResponse run(RequestContext ctx);

}
