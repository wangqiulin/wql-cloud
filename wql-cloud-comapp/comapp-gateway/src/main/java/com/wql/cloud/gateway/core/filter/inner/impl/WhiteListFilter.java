package com.wql.cloud.gateway.core.filter.inner.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.wql.cloud.gateway.core.model.MerchantCacheInfo;
import com.wql.cloud.gateway.utils.JsonDataUtil;
import com.wql.cloud.gateway.utils.IPAddressUtil;

/**
 * 白名单过滤器
 */
@Component(value = "whiteListFilter")
public class WhiteListFilter implements InnerFilter {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MerchantFactory merchantFactory;

	@Override
	public FilterResponse run(RequestContext ctx) {
		FilterResponse fr = new FilterResponse();
		try {
			// 获取请求的ip地址
			String ipAddress = IPAddressUtil.getIPAddress(ctx.getRequest());
			JSONObject json = JsonDataUtil.getRequestJSONObject(ctx);
			// 获取商户号
			String mertId = json.getString("mertId");
			if (StringUtils.isEmpty(mertId)) {
				logger.error("mertId不能为空！");
				fr.setCode(FilterResponseEnum.FAIL.getCode());
				fr.setMessage("mertId不能为空！");
			} else {
				// 验证ip是否在白名单中
				MerchantCacheInfo merchant = merchantFactory.getMerchant(mertId);
				// 商户是否存在校验
				if (null == merchant) {
					logger.error("商户不存在！");
					fr.setCode(FilterResponseEnum.FAIL.getCode());
					fr.setMessage("商户不存在");
					return fr;
				}
				// 白名单是否为空校验
				List<String> whiteList = merchant.getWhiteList();
				if (CollectionUtils.isEmpty(whiteList)) {
					logger.error("商户白名单为空！");
					fr.setCode(FilterResponseEnum.FAIL.getCode());
					fr.setMessage("商户白名单为空");
					return fr;
				}
				// 白名单校验
				if (!whiteList.contains(ipAddress)) {
					logger.error("ip:" + ipAddress + ",不在白名单中,校验不通过");
					fr.setCode(FilterResponseEnum.FAIL.getCode());
					fr.setMessage("非法请求");
				}
			}
		} catch (Exception e) {
			logger.error("白名单校验异常:" + e);
			fr.setCode(FilterResponseEnum.FAIL.getCode());
			fr.setMessage("白名单校验异常:" + e);
		}
		return fr;
	}

}
