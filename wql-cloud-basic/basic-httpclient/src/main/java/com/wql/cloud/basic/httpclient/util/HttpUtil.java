package com.wql.cloud.basic.httpclient.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author wangqiulin
 * @date 2018年5月13日
 */
@Component
public class HttpUtil {

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 表单格式的post请求
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 */
	public String postForEntity(String url, Map<String, Object> paramMap, Map<String, Object> headerMap) {
		HttpHeaders headers = new HttpHeaders();
		//表单提交
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAcceptCharset(Arrays.asList(Charset.forName("utf-8")));
		if (!CollectionUtils.isEmpty(headerMap)) {
			for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
				headers.add(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
		//封装参数，千万不要替换为Map与HashMap，否则参数无法传递
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		if(!CollectionUtils.isEmpty(paramMap)){
			for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
				params.add(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
		return restTemplate.postForObject(url, new HttpEntity<MultiValueMap<String, String>>(params, headers), String.class);
	}
	
	/**
	 * json格式的post请求
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 */
	@SuppressWarnings("all")
	public String postForJson(String url, Map<String, Object> paramMap, Map<String, Object> headerMap) {
		HttpHeaders headers = new HttpHeaders();
		//json格式
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		if (!CollectionUtils.isEmpty(headerMap)) {
			for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
				headers.add(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
		String jsonStr = null;
		if (!CollectionUtils.isEmpty(paramMap)) {
			jsonStr = JSON.toJSONString(paramMap);
		}
		return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(jsonStr, headers), String.class).getBody();
	}
	
	/**
	 * get请求(无参数)
	 * @param url
	 * @return
	 */
	public String get(String url) {
		return restTemplate.getForObject(url, String.class);
	}
	
	
	/**
	 * get请求(有参数)
	 * @param url, 拼接参数
	 * @param paramMap, 参数内容以Map接收 
	 * @return
	 */
	public String getWithParam(String url, Map<String, String> paramMap) {
		return restTemplate.getForObject(url, String.class, paramMap);
	}
	
	/**
	 * json格式的get请求
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 */
	@SuppressWarnings("all")
	public String getForJson(String url, Map<String, Object> paramMap, Map<String, Object> headerMap) {
		HttpHeaders headers = new HttpHeaders();
		//json格式
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		if (!CollectionUtils.isEmpty(headerMap)) {
			for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
				headers.add(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
		String jsonStr = null;
		if (!CollectionUtils.isEmpty(paramMap)) {
			jsonStr = JSON.toJSONString(paramMap);
		}
		return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(jsonStr, headers), String.class).getBody();
	}
	
	
	/**
	 * 发送form-data格式数据
	 * 
	 * @param url
	 * @param paramMap
	 * @return
	 */
	public String postFormData(String url, Map<String, Object> paramMap) {
        //设置请求头(注意会产生中文乱码)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        //封装参数，千万不要替换为Map与HashMap，否则参数无法传递
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		if(!CollectionUtils.isEmpty(paramMap)){
			for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
				params.add(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
        return restTemplate.postForEntity(url, new HttpEntity<MultiValueMap<String, String>>(params, headers), String.class).getBody();
	}

	
}
