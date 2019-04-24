package com.wql.cloud.gateway.core.factory.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wql.cloud.gateway.core.factory.ApiFactory;
import com.wql.cloud.gateway.core.factory.FilterFactory;
import com.wql.cloud.gateway.core.filter.inner.InnerFilter;
import com.wql.cloud.gateway.core.filter.inner.impl.EncryptFilter;
import com.wql.cloud.gateway.core.filter.inner.impl.SignFilter;
import com.wql.cloud.gateway.core.model.ApiModel;

/**
 * 响应内部过滤器工厂实现类
 */
@Component(value = "responseInnerFilterFactory")
public class ResponseInnerFilterFactoryImpl implements FilterFactory {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * apiKey与响应内部过滤器映射关系集合
	 */
	private Map<String, List<InnerFilter>> apiResFilterLocalMap = new HashMap<String, List<InnerFilter>>(1000);

	/**
	 * api工厂
	 */
	@Autowired
	private ApiFactory apiFactory;

	/**
	 * 加密过滤器
	 */
	@Resource(name = "encryptFilter")
	private EncryptFilter encryptFilter;

	/**
	 * 签名过滤器
	 */
	@Resource(name = "signFilter")
	private SignFilter signFilter;

	/**
	 * 读取响应过滤器列表
	 */
	@Override
	public List<InnerFilter> getFilterList(String apiKey) {
		List<InnerFilter> innerList = apiResFilterLocalMap.get(apiKey);
		if (innerList == null || innerList.size() < 1) {
			innerList = getCache(apiKey);
		}
		return innerList;
	}

	/**
	 * 缓存中获取过滤器列表
	 * 
	 * @param apiKey
	 * @return
	 */
	private List<InnerFilter> getCache(String apiKey) {
		// 获取路由信息
		ApiModel api = apiFactory.getApi(apiKey);
		// 设置过滤器列表
		List<InnerFilter> innerList = new ArrayList<>();
		// 判断是否做响应加密过滤
		if (api.isApiResEncrypt()) {
			innerList.add(encryptFilter);
		}
		// 判断是否做响应签名过滤
		if (api.isApiResSign()) {
			innerList.add(signFilter);
		}
		// 加载到本地内存中
		apiResFilterLocalMap.put(apiKey, innerList);
		return apiResFilterLocalMap.get(apiKey);
	}

	@Override
	public void initApiFilterMap() {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>清空apiResFilterLocalMap中的api filter 信息>>>>>>>>>>>>>>>>>>>>>>begin>>>>>");
		apiResFilterLocalMap.clear();
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<清空apiResFilterLocalMap中的api filter 信息<<<<<<<<<<<<<<<<<<<<<<<<end<<<<<");
	}
}
