package com.wql.cloud.gateway.core.filter.inner.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
 * 加密过滤器
 */
@Component(value = "encryptFilter")
public class EncryptFilter implements InnerFilter {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 商户信息工厂
	 */
	@Autowired
	private MerchantFactory merchantFactory;

	/**
	 * 数据加密验证
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
			if (data == null || "".equals(data)) {
				ctx.setResponseBody(responseJson.toJSONString());
				return fr;
			}

			// 获取商户信息
			MerchantCacheInfo merchant = merchantFactory.getMerchant(merchantCode);
			// 商户公钥数据加密
			byte[] encryptData = RSAUtils.encryptByPublicKey(data.getBytes(), merchant.getMertPublicKey());

			// 加密成功修改响应报文中data属性值,替换成加密之后的数据
			responseJson.put("data", Base64.encode(encryptData));
			String newResponseBody = responseJson.toString();
			logger.info("newResponseBody:" + newResponseBody);
			ctx.setResponseBody(newResponseBody);
		} catch (Exception e) {
			logger.error("数据加密过滤异常:" + e);
			fr.setCode(FilterResponseEnum.FAIL.getCode());
			fr.setMessage("数据加密过滤异常:" + e);
		}
		return fr;
	}

}
