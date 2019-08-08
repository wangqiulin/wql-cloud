package com.wql.cloud.gateway.core.filter.inner.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.netflix.zuul.context.RequestContext;
import com.wql.cloud.gateway.core.enums.FilterResponseEnum;
import com.wql.cloud.gateway.core.factory.BlackListFactory;
import com.wql.cloud.gateway.core.filter.inner.InnerFilter;
import com.wql.cloud.gateway.core.model.FilterResponse;
import com.wql.cloud.gateway.utils.IPAddressUtil;

/**
 * 黑名单过滤器
 */
@Component(value = "blackListFilter")
public class BlackListFilter implements InnerFilter {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 黑名单工厂
	 */
	@Autowired
	private BlackListFactory blackListFactory;

	/**
	 * 黑名单校验
	 */
	@Override
	public FilterResponse run(RequestContext ctx) {
		FilterResponse fr = new FilterResponse();
		try {
			List<String> blackList = blackListFactory.getBlackList();
			if (!CollectionUtils.isEmpty(blackList)) {
				// 获取请求的ip地址
				String ipAddress = IPAddressUtil.getIPAddress(ctx.getRequest());
				// 验证ip是否在黑名单中
				if (blackList.contains(ipAddress)) {
					logger.error("ip:" + ipAddress + " 黑名单");
					fr.setCode(FilterResponseEnum.FAIL.getCode());
					fr.setMessage("非法请求");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("黑名单校验异常:" + e);
			fr.setCode(FilterResponseEnum.FAIL.getCode());
			fr.setMessage("黑名单校验异常:" + e);
		}
		return fr;
	}

}
