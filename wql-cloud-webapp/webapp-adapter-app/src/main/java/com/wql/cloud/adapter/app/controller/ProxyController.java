package com.wql.cloud.adapter.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wql.cloud.adapter.app.factory.RequestObjectAdapterFactory;
import com.wql.cloud.adapter.app.factory.ResponseObjectAdapterFactory;
import com.wql.cloud.adapter.app.form.BaseRequestForm;
import com.wql.cloud.adapter.app.model.ApiModel;
import com.wql.cloud.adapter.app.model.RequestParamModel;

/**
 * 通用请求处理
 */
@RestController
public class ProxyController extends BaseController {

	@Autowired
	private RequestObjectAdapterFactory requestFactory;
	
	@Autowired
	private ResponseObjectAdapterFactory responseFactory;

	@ResponseBody
	@RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public String index() {
		return "success";
	}

	/**
	 * 通用请求入口
	 * 
	 * @param form
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/proxy", method = { RequestMethod.GET, RequestMethod.POST }, produces = {
			"application/json; charset=utf-8" })
	public Object proxy(@RequestBody BaseRequestForm form, HttpServletResponse response, HttpServletRequest request) {
		// 基本校验
		this.requestParamsValidate(form);
		// Sign判断
		this.checkSign(form);
		// ApiKey有效验证
		ApiModel apiModel = this.checkApi(form);
		// api权限 0 公共 1 登陆 2 角色 3商户
		String apiPermission = apiModel.getApiPermission();
		if (StringUtils.isNotBlank(apiPermission) && "1".equals(apiPermission)) {
			this.validateLogin(form);
		}
		return callRemoteMethod(form, request);
	}

	/**
	 * 调用远程方法
	 * 
	 * @param form
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object callRemoteMethod(BaseRequestForm form, HttpServletRequest request) {
		/** 1:封装请求服务对象参数 */
		RequestParamModel req = createRequestModel(request, form);
		Object requestParam = requestFactory.getObjectAdapter(getRequestAdaptorKey(form)).convert(req);
		String rest = callZuulMethod(form, requestParam);
		JSONObject obj = JSON.parseObject(rest);
		// 转换返回数据
		return responseFactory.getObjectAdapter(getResponseAdaptorKey(form)).convert(obj);
	}
}
