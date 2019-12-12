package com.wql.cloud.gateway.core.filter.inner.impl;

import java.io.IOException;

import javax.servlet.ServletInputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import com.wql.cloud.gateway.core.enums.FilterResponseEnum;
import com.wql.cloud.gateway.core.factory.MerchantFactory;
import com.wql.cloud.gateway.core.filter.inner.InnerFilter;
import com.wql.cloud.gateway.core.model.FilterResponse;
import com.wql.cloud.gateway.core.model.MerchantCacheInfo;
import com.wql.cloud.gateway.utils.Base64Utils;
import com.wql.cloud.gateway.utils.DealJsonDataUtil;
import com.wql.cloud.gateway.utils.RSAUtils;

/**
 * 解密过滤器
 */
@Component(value = "decryptFilter")
public class DecryptFilter implements InnerFilter {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 商户信息工厂
	 */
	@Autowired
	private MerchantFactory merchantFactory;

	/**
	 * 数据解密验证
	 */
	@Override
	public FilterResponse run(RequestContext ctx) {
		FilterResponse fr = new FilterResponse();
		try {

			JSONObject json = DealJsonDataUtil.getRequestJSONObject(ctx);
			// 获取商户号
			String merchantCode = json.getString("merchantCode");
			// 获取请求报文数据
			String data = json.getString("data");
			data = data.replace(" ", "+");

			/**
			 * 请求数据为空不需要解密
			 */
			if (StringUtils.isBlank(data) || data.indexOf("\"") >= 0) {
				fr.setCode(FilterResponseEnum.FAIL.getCode());
				fr.setMessage("data or sign is null");
				return fr;
			}

			// 获取商户信息
			MerchantCacheInfo merchant = merchantFactory.getMerchant(merchantCode);
			// 网站公钥数据解密
			byte[] decodedData = RSAUtils.decryptByPrivateKey(Base64Utils.decode(data),
					merchant.getPlatformPrivateKey());

			/**
			 * 解密成功修改请求报文中data属性值,替换成解密之后的数据
			 */
			json.put("data", JSONObject.parseObject(new String(decodedData, "utf-8")));
			String newRequestBody = json.toJSONString();
			logger.info("newRequestBody:" + newRequestBody);

			final byte[] reqBodyBytes = newRequestBody.getBytes();

			// 设置新的requestBody
			ctx.setRequest(new HttpServletRequestWrapper(ctx.getRequest()) {
				@Override
				public ServletInputStream getInputStream() throws IOException {
					return new ServletInputStreamWrapper(reqBodyBytes);
				}

				@Override
				public int getContentLength() {
					return reqBodyBytes.length;
				}

				@Override
				public long getContentLengthLong() {
					return reqBodyBytes.length;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("数据解密过滤异常:" + e);
			fr.setCode(FilterResponseEnum.FAIL.getCode());
			fr.setMessage("数据解密过滤异常:" + e);
		}
		return fr;
	}

}
