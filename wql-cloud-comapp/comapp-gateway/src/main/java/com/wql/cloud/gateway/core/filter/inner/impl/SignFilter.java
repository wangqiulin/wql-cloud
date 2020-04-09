package com.wql.cloud.gateway.core.filter.inner.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.context.RequestContext;
import com.wql.cloud.gateway.core.enums.FilterResponseEnum;
import com.wql.cloud.gateway.core.factory.MerchantFactory;
import com.wql.cloud.gateway.core.filter.inner.InnerFilter;
import com.wql.cloud.gateway.core.model.FilterResponse;
import com.wql.cloud.gateway.core.model.MerchantCacheInfo;
import com.wql.cloud.gateway.utils.JsonDataUtil;
import com.wql.cloud.gateway.utils.RSAUtils;

import cn.hutool.core.codec.Base64;

/**
 * 签名过滤器
 */
@Component(value = "signFilter")
public class SignFilter implements InnerFilter {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 商户信息工厂
	 */
	@Autowired
	private MerchantFactory merchantFactory;

	/**
	 * 数据签名验证
	 */
	@Override
	public FilterResponse run(RequestContext ctx) {
		FilterResponse fr = new FilterResponse();
		try {
			JSONObject json = JsonDataUtil.getRequestJSONObject(ctx);

			// 获取商户号
			String merchantCode = json.getString("merchantCode");

			// 创建响应json数据
			JSONObject responseJson = JsonDataUtil.getResponseJSONObject(ctx);

			// 获取响应报文数据
			String data = responseJson.getString("data");

			// 响应数据为空不需要加密
			if (StringUtils.isEmpty(data)) {
				ctx.setResponseBody(responseJson.toJSONString());
				return fr;
			}

			// 获取商户信息
			MerchantCacheInfo merchant = merchantFactory.getMerchant(merchantCode);

			// 网站私钥数据签名
			String sign = "";
			// 判断数据是否经过加密
			if (data.indexOf("\"") >= 0) {
				// 未加密数据签名
				sign = RSAUtils.sign(data.getBytes(), merchant.getPlatformPrivateKey());
			} else {
				// 加密数据签名
				sign = RSAUtils.sign(Base64.decode(data), merchant.getPlatformPrivateKey());
			}

			// 签名成功增加响应报文sign属性
			responseJson.put("sign", sign);
			String newResponseBody = responseJson.toString();
			logger.info("newResponseBody:" + newResponseBody);
			ctx.setResponseBody(newResponseBody);

		} catch (Exception e) {
			logger.error("数据签名过滤异常:" + e);
			fr.setCode(FilterResponseEnum.FAIL.getCode());
			fr.setMessage("数据签名过滤异常:" + e);
		}
		return fr;
	}
}
