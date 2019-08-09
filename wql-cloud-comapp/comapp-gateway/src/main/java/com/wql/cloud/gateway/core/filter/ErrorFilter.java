package com.wql.cloud.gateway.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.wql.cloud.gateway.core.enums.FilterResponseEnum;
import com.wql.cloud.gateway.core.model.FilterResponse;
import com.wql.cloud.gateway.utils.JsonUtil;

/**
 * 全局错误处理
 */
public class ErrorFilter extends ZuulFilter {

	/**
	 * 日志
	 */
	private final Logger log = LoggerFactory.getLogger(ErrorFilter.class);

	/**
	 * 过滤类型错误级别
	 */
	@Override
	public String filterType() {
		return FilterConstants.ERROR_TYPE;
	}

	@Override
	public int filterOrder() {
		// 需要在默认的 SendErrorFilter 之前
		return -1;
	}

	@Override
	public boolean shouldFilter() {
		// 请求出现异常过滤器生效
		return RequestContext.getCurrentContext().containsKey("throwable");
	}

	@Override
	public Object run() {
		try {
			RequestContext ctx = RequestContext.getCurrentContext();
			// 设置编码
			ctx.getResponse().setContentType("text/html;charset=UTF-8");
			Object e = ctx.get("throwable");
			if (e != null && e instanceof ZuulException) {
				// 删除该异常信息,不然在下一个过滤器中还会被执行处理
				ctx.remove("throwable");
				// 根据具体的业务逻辑来处理
				ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
				// 自定义返回信息
				FilterResponse fr = new FilterResponse();
				fr.setCode(FilterResponseEnum.ERROR.getCode());
				fr.setMessage("服务开小差！");
				ctx.setResponseBody(JsonUtil.filterResponseToJSON(fr).toJSONString());
			}
		} catch (Exception ex) {
			log.error("Exception filtering in custom error filter", ex);
			ReflectionUtils.rethrowRuntimeException(ex);
		}
		return null;
	}

}
