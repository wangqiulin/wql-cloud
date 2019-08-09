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
import com.wql.cloud.gateway.core.filter.inner.impl.BlackListFilter;
import com.wql.cloud.gateway.core.filter.inner.impl.DecryptFilter;
import com.wql.cloud.gateway.core.filter.inner.impl.MerchantPermissionFilter;
import com.wql.cloud.gateway.core.filter.inner.impl.ParamFilter;
import com.wql.cloud.gateway.core.filter.inner.impl.SignCheckFilter;
import com.wql.cloud.gateway.core.filter.inner.impl.WhiteListFilter;
import com.wql.cloud.gateway.core.model.Api;

/**
 * 内部过滤器工厂实现类
 */
@Component(value = "requestInnerFilterFactory")
public class RequestInnerFilterFactoryImpl implements FilterFactory {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**apiKey与内部过滤器映射关系集合 */
	private Map<String, List<InnerFilter>> apiReqFilterLocalMap = new HashMap<String, List<InnerFilter>>(1000);

	/**api工厂 */
	@Autowired
	private ApiFactory apiFactory;

	/**黑名单过滤器 */
	@Resource(name = "blackListFilter")
	private BlackListFilter blackListFilter;

	/**白名单过滤器 */
	@Resource(name = "whiteListFilter")
	private WhiteListFilter whiteListFilter;

	/**请求参数过滤器 */
	@Resource(name = "paramFilter")
	private ParamFilter paramFilter;

	/**商户api权限过滤器 */
	@Resource(name = "merchantPermissionFilter")
	private MerchantPermissionFilter merchantPermissionFilter;

	/**数据解密过滤器 */
	@Resource(name = "decryptFilter")
	private DecryptFilter decryptFilter;

	/**数据签名过滤器 */
	@Resource(name = "signCheckFilter")
	private SignCheckFilter signCheckFilter;

	/**
	 * 读取过滤器列表
	 */
	@Override
	public List<InnerFilter> getFilterList(String apiKey) {
		// 定义过滤器列表
		List<InnerFilter> innerList = new ArrayList<InnerFilter>();

		// 判断是否加载黑名单过滤器
		boolean blacklistSwitch = false;
		if (blacklistSwitch) {
			innerList.add(blackListFilter);
		}

		// 判断是否加载白名单过滤器
		boolean whitelistSwitch = false;
		if (whitelistSwitch) {
			innerList.add(whiteListFilter);
		}

		// 加载请求参数过滤器
		innerList.add(paramFilter);

		// 获取api相关过滤器列表
//		List<InnerFilter> apiInnerList = apiReqFilterLocalMap.get(apiKey);
//		if (apiInnerList == null || apiInnerList.size() < 1) {
//			apiInnerList = getCache(apiKey);
//		}
//		// 合并过滤器列表
//		for (InnerFilter innerFilter : apiInnerList) {
//			innerList.add(innerFilter);
//		}
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
		Api api = apiFactory.getApi(apiKey);
		// 设置过滤器列表
		List<InnerFilter> innerList = new ArrayList<>();

		// api权限 0 公共 1 登陆 2 角色 3商户
		Integer apiPermission = api.getApiPermission();
		if (null != apiPermission && 3 == apiPermission) {// 3商户
			innerList.add(merchantPermissionFilter);
		}

		// 判断是否做数据签名过滤
		if (api.isApiReqChecksign()) {
			innerList.add(signCheckFilter);
		}

		// 判断是否做数据解密过滤
		if (api.isApiReqDecrypt()) {
			innerList.add(decryptFilter);
		}

		// 加载到本地内存中
		apiReqFilterLocalMap.put(apiKey, innerList);
		return apiReqFilterLocalMap.get(apiKey);
	}

	@Override
	public void initApiFilterMap() {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>清空apiReqFilterLocalMap中的api filter 信息>>>>>>>>>>>>>>>>begin>>>>>");
		apiReqFilterLocalMap.clear();
		logger.info("<<<<<<<<<<<<<<<<<<<<<<清空apiReqFilterLocalMap中的api filter 信息<<<<<<<<<<<<<<<<<<end<<<<<");
	}
	
}
