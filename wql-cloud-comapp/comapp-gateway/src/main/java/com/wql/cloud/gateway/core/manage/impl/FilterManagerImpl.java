package com.wql.cloud.gateway.core.manage.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.context.RequestContext;
import com.wql.cloud.gateway.core.enums.FilterResponseEnum;
import com.wql.cloud.gateway.core.factory.FilterFactory;
import com.wql.cloud.gateway.core.filter.inner.InnerFilter;
import com.wql.cloud.gateway.core.manage.FilterManager;
import com.wql.cloud.gateway.core.model.FilterResponse;
import com.wql.cloud.gateway.utils.JsonDataUtil;

/**
 * 过滤规则管理类
 */
@Component
public class FilterManagerImpl implements FilterManager {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**过滤器工厂 */
	private FilterFactory filterFactory;

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
		// 返回结果
		FilterResponse fr = new FilterResponse();
		// 获取apiKey
		JSONObject json = JsonDataUtil.getRequestJSONObject(ctx);
		if(json == null || StringUtils.isBlank(json.getString("apiKey"))) {
			fr.setCode(FilterResponseEnum.FAIL.getCode());
			fr.setMessage("apiKey is null");
			return fr;
		}
		// 过滤器列表
		String apiKey = json.getString("apiKey");
		List<InnerFilter> innerFilterList = filterFactory.getFilterList(apiKey);
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
