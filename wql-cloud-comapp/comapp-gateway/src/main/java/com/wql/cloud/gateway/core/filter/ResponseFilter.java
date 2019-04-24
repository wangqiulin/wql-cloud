package com.wql.cloud.gateway.core.filter;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.wql.cloud.gateway.core.enums.FilterResponseEnum;
import com.wql.cloud.gateway.core.factory.FilterFactory;
import com.wql.cloud.gateway.core.manage.impl.FilterManagerImpl;
import com.wql.cloud.gateway.core.model.FilterResponse;
import com.wql.cloud.gateway.utils.JsonUtil;

/**
 * 响应过滤器
 */
public class ResponseFilter extends ZuulFilter {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 过滤规则管理类
	 */
	@Autowired
	private FilterManagerImpl filterManager;

	/**
	 * 响应过滤器工厂
	 */
	@Resource(name = "responseInnerFilterFactory")
	private FilterFactory responseInnerFilterFactory;

	/**
	 * 过滤器是否生效
	 */
	@Override
	public boolean shouldFilter() {
		RequestContext context = RequestContext.getCurrentContext();
		return context.getThrowable() == null && (!context.getZuulResponseHeaders().isEmpty()
				|| context.getResponseDataStream() != null || context.getResponseBody() != null);
	}

	/**
	 * 执行过滤规则验证
	 */
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		// 设置编码
		ctx.getResponse().setContentType("text/html;charset=UTF-8");
		// 设置过滤器工厂
		filterManager.setFilterFactory(responseInnerFilterFactory);

		// 过滤结果
		FilterResponse fr = filterManager.doFilter(ctx);

		// 判断过滤结果是否通过
		if (FilterResponseEnum.FAIL.getCode().equals(fr.getCode())) {
			logger.error("响应过滤器验证未通过,错误信息:" + fr.getMessage());
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			ctx.setResponseBody(JsonUtil.filterResponseToJSON(fr).toJSONString());
			ctx.set("isSuccess", FilterResponseEnum.FAIL.getCode());
		} else {
			ctx.setSendZuulResponse(true);
			ctx.setResponseStatusCode(200);
			ctx.set("isSuccess", FilterResponseEnum.SUCCESS.getCode());
		}

		return null;
	}

	/**
	 * 响应之后执行过滤
	 */
	@Override
	public String filterType() {
		return FilterConstants.POST_TYPE;
	}

	/**
	 * 过滤顺序排在第999位
	 */
	@Override
	public int filterOrder() {
		return 999;
	}

}
