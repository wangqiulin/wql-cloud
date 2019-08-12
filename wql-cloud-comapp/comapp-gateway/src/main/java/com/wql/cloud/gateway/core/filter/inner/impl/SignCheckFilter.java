package com.wql.cloud.gateway.core.filter.inner.impl;

import java.io.IOException;

import javax.servlet.ServletInputStream;

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
import com.wql.cloud.gateway.core.filter.InnerFilter;
import com.wql.cloud.gateway.core.model.FilterResponse;
import com.wql.cloud.gateway.core.model.MerchantCacheInfo;
import com.wql.cloud.gateway.utils.Base64Utils;
import com.wql.cloud.gateway.utils.DealJsonDataUtil;
import com.wql.cloud.gateway.utils.RSAUtils;

/**
 * 签名验证过滤器
 */
@Component(value = "signCheckFilter")
public class SignCheckFilter implements InnerFilter {

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
			JSONObject json = DealJsonDataUtil.getRequestJSONObject(ctx);
			// 获取商户号
			String merchantCode = json.getString("merchantCode");
			// 获取请求报文数据
			String data = json.getString("data");

			// 获取请求数据签名
			String sign = json.getString("sign");

			// 数据或签名为空不做处理
			if (data == null || "".equals(data) || sign == null || "".equals(sign)) {
				fr.setCode(FilterResponseEnum.FAIL.getCode());
				fr.setMessage("data or sign is not null");
				return fr;
			}

			// 获取商户信息
			MerchantCacheInfo merchant = merchantFactory.getMerchant(merchantCode);

			boolean result = false;

			// 判断数据是否经过加密
			if (data.indexOf("\"") >= 0) {
				// 未加密数据验证签名
				result = RSAUtils.verify(data.getBytes(), merchant.getMerchantPublicKey(), sign);
			} else {
				// 加密数据验证签名
				result = RSAUtils.verify(Base64Utils.decode(data), merchant.getMerchantPublicKey(), sign);
			}

			// 验证通过修改请求报文去掉sign属性
			if (result) {
				// 去掉sign属性
				json.remove("sign");
				String newRequestBody = json.toString();
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

			} else {
				fr.setCode(FilterResponseEnum.FAIL.getCode());
				fr.setMessage("sign check fail");
			}
		} catch (Exception e) {
			logger.error("数据验签过滤异常:" + e);
			fr.setCode(FilterResponseEnum.FAIL.getCode());
			fr.setMessage("数据验签过滤异常:" + e);
		}

		return fr;
	}

}
