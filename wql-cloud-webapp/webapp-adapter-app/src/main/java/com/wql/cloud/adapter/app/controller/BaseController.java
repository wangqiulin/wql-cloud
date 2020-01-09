package com.wql.cloud.adapter.app.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wql.cloud.adapter.app.constants.Constant;
import com.wql.cloud.adapter.app.constants.RedisConstants;
import com.wql.cloud.adapter.app.exception.CreditAdapterException;
import com.wql.cloud.adapter.app.exception.CreditAdaptorErrorCode;
import com.wql.cloud.adapter.app.form.BaseRequestForm;
import com.wql.cloud.adapter.app.form.SystemModel;
import com.wql.cloud.adapter.app.model.ApiModel;
import com.wql.cloud.adapter.app.model.RequestParamModel;
import com.wql.cloud.adapter.app.properties.SystemProperties;
import com.wql.cloud.adapter.app.util.JsonUtils;
import com.wql.cloud.adapter.app.util.Md5Util;
import com.wql.cloud.basic.redis.util.RedisUtil;

@RestController
public class BaseController {

	private static Logger log = LoggerFactory.getLogger(BaseController.class);

	/**apiKey与路由映射关系集合*/
	private static Map<String, ApiModel> apiMap = new HashMap<String, ApiModel>(1000);
	
	@Autowired
	private SystemProperties systemProperties;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RedisUtil redisUtil;

	protected void requestParamsValidate(BaseRequestForm form) {
		// 调用方法、请求数据、系统参数、签名不能为空
		if (StringUtils.isBlank(form.getApiKey()) || StringUtils.isBlank(form.getData()) || form.getSystem() == null
				|| form.getSign() == null) {
			throw new CreditAdapterException(CreditAdaptorErrorCode.REQUEST_PARAMS_VALIDATE_IS_ERROR);
		}
		log.info("requestParamsValidate true");
	}

	/**
	 * Check Sign
	 *
	 * @param form
	 */
	protected void checkSign(BaseRequestForm form) {
		StringBuilder sb = new StringBuilder();
		sb.append(form.getApiKey());
		if (form.getSession() != null && StringUtils.isNotBlank(form.getSession().getToken())) {
			sb.append(form.getSession().getToken());
		}
		sb.append(form.getData());
		if (Constant.REQUEST_SOURCE_H5.equalsIgnoreCase(form.getSystem().getAppType())) {
			sb.append(systemProperties.getH5SignKey());
		} else {
			sb.append(systemProperties.getAppSignKey());
		}
		String sign = Md5Util.md5(sb.toString());
		if (!sign.equalsIgnoreCase(form.getSign())) {
			log.info("checkSign false {}={}", sign, form.getSign());
			throw new CreditAdapterException(CreditAdaptorErrorCode.SIGN_IS_ERROR);
		}
		log.info("checkSign true");
	}

	/**
	 * @param form
	 */
	protected void validateLogin(BaseRequestForm form) {
		// Token 验证(JWT)
		if (StringUtils.isBlank(form.getSession().getUserId()) && StringUtils.isBlank(form.getSession().getToken())) {
		    //修改为登录过期
			throw new CreditAdapterException(CreditAdaptorErrorCode.TOKEN_ERROR);
		}
		String cacheToken = (String) redisUtil.get(RedisConstants.USER_LOGIN_KEY + form.getSession().getUserId());
		if (StringUtils.isBlank(cacheToken)) {
			throw new CreditAdapterException(CreditAdaptorErrorCode.TOKEN_ERROR);
		}
		// 账号被冻结 取缓存 如果存在则说明是冻结账号
		if (redisUtil.exists(RedisConstants.USER_FREEZE_KEY + form.getSession().getUserId())) {
			CreditAdapterException exception = new CreditAdapterException(CreditAdaptorErrorCode.USER_FREEZE);
			throw exception;
		}
		// 被挤下线
		if (!cacheToken.equals(form.getSession().getToken())) {
			throw new CreditAdapterException(CreditAdaptorErrorCode.USER_SQUEEZE);
		}
		//TODO  验证Token有效性
//		if (!TokenMgr.validateJWT(form.getSession().getToken()).isSuccess()) {
//			throw new CreditAdapterException(CreditAdaptorErrorCode.TOKEN_ERROR);
//		}
	}

	/**
	 * 检查Apie Key是否需要登陆验证
	 *
	 * @param form
	 */
	protected ApiModel checkApi(BaseRequestForm form) {
		return checkApi(form.getApiKey());
	}

	/**
	 * @param apiKey
	 * @return
	 */
	protected ApiModel checkApi(String apiKey) {
		ApiModel apiModel = apiMap.get(apiKey);
		if (apiModel == null) {
			// Redis取得Api Key对应的信息（json格式）
			String rest = redisUtil.getStr(RedisConstants.SYSTEM_API + apiKey);
			if (StringUtils.isBlank(rest)) {
				// 不存在
				throw new CreditAdapterException(CreditAdaptorErrorCode.API_KEY_IS_ERROR);
			} else {
				apiModel = JSON.parseObject(rest, ApiModel.class);
			}
			log.info("set api info", JsonUtils.toJsonString(apiModel));
			apiMap.put(apiKey, apiModel);
		}
		return apiModel;
	}

	/**
	 * 生成请求对象
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	protected RequestParamModel createRequestModel(HttpServletRequest request, BaseRequestForm form) {
		RequestParamModel param = new RequestParamModel();
		param.setMerchantCode(form.getMerchantCode());
		param.setApiKey(form.getApiKey());
		param.setData(JSON.parseObject(form.getData()));
		if (form.getSession() != null) {
			param.setUserId(form.getSession().getUserId());
		}
		if (form.getSystem() != null) {
			param.setSystem(form.getSystem());
			param.getSystem().setClientIp(getRemoteIp(request));
		}
		return param;
	}

	/**
	 * 获取适配器工程类的key值
	 *
	 * @param form
	 * @return
	 */
	protected String getRequestAdaptorKey(BaseRequestForm form) {
		String adaptorKey = form.getApiKey() + Constant.REQUEST_ADAPTER;
		return adaptorKey;
	}

	/**
	 * 获取适配器工程类的key值
	 *
	 * @param form
	 * @return
	 */
	protected String getResponseAdaptorKey(BaseRequestForm form) {
		String adaptorKey = form.getApiKey() + Constant.RESPONSE_ADAPTER;
		return adaptorKey;
	}

	/**
	 * SpEl 生成Cache Key
	 *
	 * @param form
	 * @param cacheKey
	 * @return
	 */
	protected String getCacheKey(BaseRequestForm form, String cacheKey) {
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		String dataStr = form.getData();
		if (StringUtils.isNotEmpty(dataStr)) {
			JSONObject data = JSON.parseObject(form.getData());
			for (Entry<String, Object> entry : data.entrySet()) {
				context.setVariable(entry.getKey(), entry.getValue());
			}
		}
		SystemModel systemModel = form.getSystem();
		if (null != systemModel) {
			JSONObject systemStr = JSON.parseObject(JSON.toJSONString(systemModel));
			for (Entry<String, Object> entry : systemStr.entrySet()) {
				context.setVariable(entry.getKey(), entry.getValue());
			}
		}
		return parser.parseExpression(cacheKey, new TemplateParserContext()).getValue(context, String.class);
	}

	/**
	 * 获取远程IP
	 *
	 * @param request
	 */
	protected String getRemoteIp(HttpServletRequest request) {
		String ipString = request.getHeader("x-forwarded-for");
		if (StringUtils.isBlank(ipString) || Constant.UNKNOWN.equalsIgnoreCase(ipString)) {
			ipString = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ipString) || Constant.UNKNOWN.equalsIgnoreCase(ipString)) {
			ipString = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ipString) || Constant.UNKNOWN.equalsIgnoreCase(ipString)) {
			ipString = request.getRemoteAddr();
		}

		// 多个路由时，取第一个非unknown的ip
		final String[] arr = ipString.split(",");
		for (final String str : arr) {
			if (!Constant.UNKNOWN.equalsIgnoreCase(str)) {
				ipString = str;
				break;
			}
		}
		return ipString;
	}

	/**
	 * @param form
	 * @param requestParam
	 * @return
	 */
	protected String callZuulMethod(BaseRequestForm form, Object requestParam) {
		return callZuulMethod(form,requestParam,true);
	}

	/**
	 * @param form
	 * @param requestParam
	 * @return
	 */
	protected String callZuulMethod(BaseRequestForm form, Object requestParam,boolean logEnabled) {
		String rest = null;
		if(logEnabled){
			log.info("Zuul url:" + systemProperties.getZuulUrl());
			log.info("Zuul request:" + JSON.toJSONString(requestParam));
		}
		rest = callRemoteMethod(systemProperties.getZuulUrl(), form.getApiKey(), requestParam);
		if(logEnabled){
			log.info("Zuul response:" + rest);
		}
		return rest;
	}

	/**
	 * 调用远程方法
	 *
	 * @param url
	 * @param apiKey
	 * @param requestParam
	 * @return
	 */
	protected String callRemoteMethod(String url, String apiKey, Object requestParam) {
		int retryCnt = 0;
		String rest = "{}";
		while (true) {
			try {
				rest = restTemplate.exchange(url, HttpMethod.POST,
						new HttpEntity<>(requestParam, createLogHttpHeader()), String.class).getBody();
				break;
			} catch (HttpServerErrorException e) {
				retryCnt++;
				log.error("接口请求错误，{}, {}", apiKey, e);
				if (retryCnt >= systemProperties.getMaxRetryCnt()) {
					throw e;
				}
			}
		}
		return rest;
	}

	/**
	 * 生成包含日志id的请求头
	 *
	 * @return
	 */
	private HttpHeaders createLogHttpHeader() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add(systemProperties.getTranceId(), MDC.get(systemProperties.getTranceId()));
		return requestHeaders;
	}

	/**
	 * 清空apiMap
	 */
	public void clearApiMap() {
		apiMap.clear();
	}
}
