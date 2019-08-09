package com.wql.cloud.gateway.core.filter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.REQUEST_URI_KEY;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_HEADER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_HEADER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.wql.cloud.gateway.core.enums.FilterResponseEnum;
import com.wql.cloud.gateway.core.enums.RouteModeEnum;
import com.wql.cloud.gateway.core.factory.ApiFactory;
import com.wql.cloud.gateway.core.model.Api;
import com.wql.cloud.gateway.core.model.FilterResponse;
import com.wql.cloud.gateway.utils.JsonUtil;

/**
 * 分发请求到各个服务的过滤器
 */
public class PreConfigFilter extends ZuulFilter {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ApiFactory apiFactory;

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		Object obj = ctx.get("isSuccess");
		if (null != obj) {
			String isSuccess = obj.toString();
			// 根据前置过滤器的结果判断当前过滤器是否执行
			if (FilterResponseEnum.FAIL.getCode().equals(isSuccess)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		// 设置编码
		ctx.getResponse().setContentType("text/html;charset=UTF-8");
		// 获取apikey
		JSONObject json = JsonUtil.getRequestJSONObject(ctx);
		String apiKey = json.getString("apiKey");
		logger.info("apiKey------>" + apiKey);
		// 验证参数apiKey是否存在
		if (apiKey == null || "".equals(apiKey)) {
			// 返回结果
			FilterResponse fr = new FilterResponse();
			logger.error("apiKey is null");
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			fr.setCode(FilterResponseEnum.FAIL.getCode());
			fr.setMessage("apiKey is null");
			ctx.setResponseBody(JsonUtil.filterResponseToJSON(fr).toJSONString());
			ctx.set("isSuccess", FilterResponseEnum.FAIL.getCode());
			return null;
		}

		// 获取api信息
		Api api = apiFactory.getApi(apiKey);
		logger.info("api------>" + JSON.toJSON(api));
		if (null == api) {
			// 返回结果
			FilterResponse fr = new FilterResponse();
			logger.error("api is null");
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			fr.setCode(FilterResponseEnum.FAIL.getCode());
			fr.setMessage("api is null");
			ctx.setResponseBody(JsonUtil.filterResponseToJSON(fr).toJSONString());
			ctx.set("isSuccess", FilterResponseEnum.FAIL.getCode());
			return null;
		}

		// 路由方式 0 ribbon 1 http
		Integer routeMode = api.getApiRouteMode();
		if (routeMode == 0) {// 处理ribbon请求
			// 服务ID
			String serviceId = api.getApiRouteServiceid();
			// 请求uri
			String uri = api.getApiRoutePath();
			// 设置请求参数
			ctx.put(REQUEST_URI_KEY, uri);
			ctx.set(SERVICE_ID_KEY, serviceId);
			ctx.setRouteHost(null);
			ctx.addOriginResponseHeader(SERVICE_ID_HEADER, serviceId);
		} else if (RouteModeEnum.HTTP.getCode().equals(routeMode)) {// 处理http请求
			ctx.setRouteHost(getUrl(api.getApiRoutePath()));
			ctx.addOriginResponseHeader(SERVICE_HEADER, api.getApiRoutePath());
		} else {
			FilterResponse fr = new FilterResponse();
			logger.error("routeMode is not valid");
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			fr.setCode(FilterResponseEnum.FAIL.getCode());
			fr.setMessage("routeMode is not valid");
			ctx.setResponseBody(JsonUtil.filterResponseToJSON(fr).toJSONString());
			ctx.set("isSuccess", FilterResponseEnum.FAIL.getCode());
			return null;
		}
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 7;
	}

	private URL getUrl(String target) {
		try {
			return new URL(target);
		} catch (MalformedURLException ex) {
			throw new IllegalStateException("Target URL is malformed", ex);
		}
	}

}
