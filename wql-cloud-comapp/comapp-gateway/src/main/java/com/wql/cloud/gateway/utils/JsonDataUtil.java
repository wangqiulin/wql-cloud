package com.wql.cloud.gateway.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.context.RequestContext;
import com.wql.cloud.gateway.core.model.FilterResponse;

/**
 * JSON工具类
 */
public class JsonDataUtil {

	/**
	 * 日志
	 */
	public static final Logger logger = LoggerFactory.getLogger(JsonDataUtil.class);

	/**
	 * 从request请求中获取json数据
	 * 
	 * @param request
	 * @return
	 */
	public static JSONObject getJSONObject(HttpServletRequest request) {
		JSONObject jsonObject = null;
		try {
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			StringBuilder responseStrBuilder = new StringBuilder();
			String inputStr;
			while ((inputStr = streamReader.readLine()) != null) {
				responseStrBuilder.append(inputStr);
			}
			jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
		} catch (Exception e) {
			logger.error("getJSONObject解析异常:" + e);
		}
		return jsonObject;
	}

	/**
	 * 获取请求数据
	 * 
	 * @param ctx
	 * @return
	 */
	public static JSONObject getRequestJSONObject(RequestContext ctx) {
		JSONObject jsonObject = null;
		try {
			// 前序过滤器已经解析过数据
			if (ctx.get("requestJson") != null) {
				return jsonObject = (JSONObject) ctx.get("requestJson");
			}

			// 读取流数据
			InputStream in = ctx.getRequest().getInputStream();
			String requestBody = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
			jsonObject = JSONObject.parseObject(requestBody);

			// 设置到共享空间
			ctx.set("requestJson", jsonObject);
		} catch (Exception e) {
			logger.error("getRequestJSONObject解析异常:" + e);
		}
		return jsonObject;
	}

	/**
	 * 获取响应数据
	 * 
	 * @param ctx
	 * @return
	 */
	public static JSONObject getResponseJSONObject(RequestContext ctx) {
		JSONObject jsonObject = null;
		try {
			// 读取流数据
			InputStream in = ctx.getResponseDataStream();
			String responseBody = StreamUtils.copyToString(in, Charset.forName("UTF-8"));

			if (responseBody == null || "".equals(responseBody)) {
				responseBody = ctx.getResponseBody();
			}

			jsonObject = JSONObject.parseObject(responseBody);
		} catch (Exception e) {
			logger.error("getResponseJSONObject解析异常:" + e);
		}
		return jsonObject;
	}

	/**
	 * 过滤响应结果转换JSON格式
	 * 
	 * @param fr
	 * @return
	 */
	public static JSONObject filterResponseToJSON(FilterResponse fr) {
		JSONObject json = new JSONObject();
		json.put("code", fr.getCode());
		json.put("message", fr.getMessage());
		return json;
	}
}
