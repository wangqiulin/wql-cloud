package com.wql.cloud.gateway.core.filter.inner.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.context.RequestContext;
import com.wql.cloud.gateway.core.enums.FilterResponseEnum;
import com.wql.cloud.gateway.core.factory.MerchantFactory;
import com.wql.cloud.gateway.core.filter.inner.InnerFilter;
import com.wql.cloud.gateway.core.model.FilterResponse;
import com.wql.cloud.gateway.core.model.Merchant;
import com.wql.cloud.gateway.utils.JsonUtil;

/**
 * 商户api权限过滤器
 */
@Component(value = "merchantPermissionFilter")
public class MerchantPermissionFilter implements InnerFilter {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 商户信息工厂
	 */
	@Autowired
	private MerchantFactory merchantFactory;

	/**
	 * 商户api权限过滤
	 */
	@Override
	public FilterResponse run(RequestContext ctx) {
		FilterResponse fr = new FilterResponse();
		try {
			JSONObject json = JsonUtil.getJSONObject(ctx.getRequest());

			// 获取商户号
			String merchantCode = json.getString("merchantCode");
			// 获取商户号
			String apiKey = json.getString("apiKey");
			// 获取商户信息
			Merchant merchant = merchantFactory.getMerchant(merchantCode);
			if (null == merchant) {
				fr.setCode(FilterResponseEnum.FAIL.getCode());
				fr.setMessage("商户信息不存在");
				return fr;
			}
			List<String> apiList = merchant.getApiList();
			if (CollectionUtils.isEmpty(apiList) || StringUtils.isEmpty(apiKey) || !apiList.contains(apiKey)) {
				fr.setCode(FilterResponseEnum.FAIL.getCode());
				fr.setMessage("非法请求");
				return fr;
			}
		} catch (Exception e) {
			logger.error("商户api权限过滤异常:" + e);
			fr.setCode(FilterResponseEnum.FAIL.getCode());
			fr.setMessage("商户api权限过滤异常:" + e);
		}
		return fr;
	}

}
