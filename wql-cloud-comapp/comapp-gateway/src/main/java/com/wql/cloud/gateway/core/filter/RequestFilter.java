package com.wql.cloud.gateway.core.filter;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.wql.cloud.gateway.core.enums.FilterResponseEnum;
import com.wql.cloud.gateway.core.factory.FilterFactory;
import com.wql.cloud.gateway.core.manage.FilterManager;
import com.wql.cloud.gateway.core.model.FilterResponse;
import com.wql.cloud.gateway.utils.JsonDataUtil;

/**
 * 1.请求过滤器
 */
@Component
public class RequestFilter extends ZuulFilter {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**过滤规则管理类 */
	@Autowired
	private FilterManager filterManager;

	/**请求过滤器工厂 */
	@Resource(name = "requestInnerFilterFactory")
	private FilterFactory requestInnerFilterFactory;

	/**过滤器是否生效 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	/**执行过滤规则验证*/
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		// 设置编码
		ctx.getResponse().setContentType("text/html;charset=UTF-8");
		// 设置过滤器工厂
		filterManager.setFilterFactory(requestInnerFilterFactory);
		// 过滤结果
		FilterResponse fr = null;
		try {
			fr = filterManager.doFilter(ctx);
			// 判断过滤结果是否通过
			if (FilterResponseEnum.FAIL.getCode().equals(fr.getCode())) {
				logger.error("请求过滤器验证未通过,错误信息:" + fr.getMessage());
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(401);
				ctx.setResponseBody(JsonDataUtil.filterResponseToJSON(fr).toJSONString());
				ctx.set("isSuccess", FilterResponseEnum.FAIL.getCode());
				return null;
			} else {
				ctx.setSendZuulResponse(true);
				ctx.setResponseStatusCode(200);
				ctx.set("isSuccess", FilterResponseEnum.SUCCESS.getCode());
			}
		} catch (Exception e) {
			logger.error("请求过滤验证异常:" + e);
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			fr.setCode(FilterResponseEnum.FAIL.getCode());
			fr.setMessage("请求过滤验证异常:" + e.getMessage());
			ctx.setResponseBody(JsonDataUtil.filterResponseToJSON(fr).toJSONString());
			ctx.set("isSuccess", FilterResponseEnum.FAIL.getCode());
		}
		return null;
	}

	/**
	 * 在请求达到之前执行过滤
	 */
	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	/**
	 * 过滤顺序排在第3位
	 */
	@Override
	public int filterOrder() {
		return 3;
	}

}
