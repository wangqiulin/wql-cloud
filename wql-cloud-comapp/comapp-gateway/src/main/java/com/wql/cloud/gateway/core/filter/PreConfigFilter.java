package com.wql.cloud.gateway.core.filter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.REQUEST_URI_KEY;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_HEADER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_HEADER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.wql.cloud.gateway.core.enums.FilterResponseEnum;
import com.wql.cloud.gateway.core.enums.RouteModeEnum;
import com.wql.cloud.gateway.core.factory.ApiFactory;
import com.wql.cloud.gateway.core.model.Api;
import com.wql.cloud.gateway.core.model.FilterResponse;
import com.wql.cloud.gateway.utils.JsonDataUtil;

/**
 * 2.分发请求到各个服务的过滤器
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
		JSONObject json = JsonDataUtil.getRequestJSONObject(ctx);
		if(json == null || StringUtils.isBlank(json.getString("apiKey"))) {
			FilterResponse fr = new FilterResponse();
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			fr.setCode(FilterResponseEnum.FAIL.getCode());
			fr.setMessage("apiKey is null");
			ctx.setResponseBody(JsonDataUtil.filterResponseToJSON(fr).toJSONString());
			ctx.set("isSuccess", FilterResponseEnum.FAIL.getCode());
			return null;
		}
		// 获取api信息
		String apiKey = json.getString("apiKey");
		Api api = apiFactory.getApi(apiKey);
		if (null == api) {
			FilterResponse fr = new FilterResponse();
			logger.error("api is null");
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			fr.setCode(FilterResponseEnum.FAIL.getCode());
			fr.setMessage("api is null");
			ctx.setResponseBody(JsonDataUtil.filterResponseToJSON(fr).toJSONString());
			ctx.set("isSuccess", FilterResponseEnum.FAIL.getCode());
			return null;
		}
		// 路由方式 0 ribbon 1 http
		Integer routeMode = api.getApiRouteMode();
		if (routeMode == Integer.parseInt(RouteModeEnum.RIBBON.getCode())) {
			ctx.put(REQUEST_URI_KEY, api.getApiRoutePath().trim());  // 请求uri
			ctx.set(SERVICE_ID_KEY, api.getApiRouteServiceid().trim()); // 服务ID
			ctx.setRouteHost(null);
			ctx.addOriginResponseHeader(SERVICE_ID_HEADER, api.getApiRouteServiceid());
			return null;
		} else if (routeMode == Integer.parseInt(RouteModeEnum.HTTP.getCode())) {
			ctx.setRouteHost(getUrl(api.getApiRoutePath().trim()));
			ctx.addOriginResponseHeader(SERVICE_HEADER, api.getApiRoutePath().trim());
			return null;
		}
		FilterResponse fr = new FilterResponse();
		logger.error("routeMode is not valid");
		ctx.setSendZuulResponse(false);
		ctx.setResponseStatusCode(401);
		fr.setCode(FilterResponseEnum.FAIL.getCode());
		fr.setMessage("routeMode is not valid");
		ctx.setResponseBody(JsonDataUtil.filterResponseToJSON(fr).toJSONString());
		ctx.set("isSuccess", FilterResponseEnum.FAIL.getCode());
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
