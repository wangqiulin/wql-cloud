package com.wql.cloud.gateway.core.manage.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.context.RequestContext;
import com.wql.cloud.gateway.core.enums.FilterResponseEnum;
import com.wql.cloud.gateway.core.factory.FilterFactory;
import com.wql.cloud.gateway.core.filter.InnerFilter;
import com.wql.cloud.gateway.core.manage.FilterManager;
import com.wql.cloud.gateway.core.model.FilterResponse;
import com.wql.cloud.gateway.utils.DealJsonDataUtil;

/**
 * 过滤规则管理类
 */
@Component
public class FilterManagerImpl implements FilterManager {

	/**
	 * 日志
	 */
	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 过滤器工厂
	 */
	private FilterFactory filterFactory;

	/**
	 * 设置过滤器工厂
	 * 
	 * @param filterFactory
	 */
	@Override
	public void setFilterFactory(FilterFactory filterFactory) {
		this.filterFactory = filterFactory;
	}

	@Override
	public FilterFactory getFilterFactory() {
		return this.filterFactory;
	}

	@Override
	public FilterResponse doFilter(RequestContext ctx) {
		// 获取apiKey
		JSONObject json = DealJsonDataUtil.getRequestJSONObject(ctx);
		String apiKey = json.getString("apiKey");
		
		// 返回结果
		FilterResponse fr = new FilterResponse();
		
		// 过滤器列表
		List<InnerFilter> innerFilterList = filterFactory.getFilterList(apiKey);
		logger.info("apiKey:" + apiKey + " , 对应的过滤器列表:" + innerFilterList);

		// 执行过滤规则
		for (InnerFilter innerFilter : innerFilterList) {
			// 过滤结果
			fr = innerFilter.run(ctx);
			// 命中一条过滤规则，则不通过
			if (FilterResponseEnum.FAIL.getCode().equals(fr.getCode())) {
				break;
			}
		}
		return fr;
	}

}
