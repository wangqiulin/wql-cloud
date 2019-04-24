package com.wql.cloud.gateway.core.filter.inner.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.context.RequestContext;
import com.wql.cloud.gateway.core.enums.FilterResponseEnum;
import com.wql.cloud.gateway.core.factory.ApiFactory;
import com.wql.cloud.gateway.core.filter.inner.InnerFilter;
import com.wql.cloud.gateway.core.model.ApiModel;
import com.wql.cloud.gateway.core.model.FilterResponse;
import com.wql.cloud.gateway.utils.JsonUtil;

/**
 * 参数验证过滤器
 */
@Component(value = "paramFilter")
public class ParamFilter implements InnerFilter {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * api工厂
	 */
	@Autowired
	private ApiFactory apiFactory;

	/**
	 * 请求参数校验
	 */
	@Override
	public FilterResponse run(RequestContext ctx) {
		FilterResponse fr = new FilterResponse();
		try {
			// 获取请求数据
			JSONObject json = JsonUtil.getRequestJSONObject(ctx);
			// 获取apiKey
			String apiKey = json.getString("apiKey");
			logger.info("apiKey------>" + apiKey);
			// 验证参数apiKey是否存在
			if (apiKey == null || "".equals(apiKey)) {
				logger.error("apiKey is null");
				fr.setCode(FilterResponseEnum.FAIL.getCode());
				fr.setMessage("apiKey is not null");
				return fr;
			}

			// 获取api信息
			ApiModel api = apiFactory.getApi(apiKey);
			logger.info("api------>" + JSON.toJSON(api));
			if (null == api) {
				logger.error("api is null");
				fr.setCode(FilterResponseEnum.FAIL.getCode());
				fr.setMessage("api is not null");
				return fr;
			}

			// 验证路由信息是否存在
			boolean routeFlag = false;
			Integer routeMode = api.getRouteMode();
			if (null == routeMode) {
				routeFlag = true;
			} else {
				if (routeMode == 0
						&& (StringUtils.isEmpty(api.getRouteServiceId()) || StringUtils.isEmpty(api.getRoutePath()))) {
					routeFlag = true;
				} else if (routeMode == 1 && StringUtils.isEmpty(api.getRoutePath())) {
					routeFlag = true;
				}
			}

			if (routeFlag) {
				logger.error("apiKey:" + apiKey + " 不存在的api");
				fr.setCode(FilterResponseEnum.FAIL.getCode());
				fr.setMessage("非法请求");
				return fr;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("请求参数校验异常:" + e);
			fr.setCode(FilterResponseEnum.FAIL.getCode());
			fr.setMessage("请求参数校验异常:" + e);
		}
		return fr;
	}

}
