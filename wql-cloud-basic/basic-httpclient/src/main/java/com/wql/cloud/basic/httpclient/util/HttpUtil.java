package com.wql.cloud.basic.httpclient.util;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
	 * 表单请求
	 * 
	 * @param url
	 * @param paramMap
	 * @return
	 */
	public String formPost(String url, Map<String, Object> paramMap) {
		HttpHeaders headers = new HttpHeaders();
		//表单提交
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAcceptCharset(Arrays.asList(Charset.forName("utf-8")));
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
	 * 表单格式的post请求
	 * 
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 */
	public String formPost(String url, Map<String, Object> paramMap, Map<String, Object> headerMap) {
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
	 * 
	 * @param url
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("all")
	public String jsonPost(String url, Map<String, Object> paramMap) {
		HttpHeaders headers = new HttpHeaders();
		//json格式
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		String jsonStr = null;
		if (!CollectionUtils.isEmpty(paramMap)) {
			jsonStr = JSON.toJSONString(paramMap);
		}
		return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(jsonStr, headers), String.class).getBody();
	}
	
	
	/**
	 * json格式的post请求
	 * 
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 */
	@SuppressWarnings("all")
	public String jsonPost(String url, Map<String, Object> paramMap, Map<String, Object> headerMap) {
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
	 * 
	 * @param url, 拼接参数
	 * @param paramMap, 参数内容以Map接收 
	 * @return
	 */
	public String getWithParam(String url, Map<String, String> paramMap) {
		return restTemplate.getForObject(url, String.class, paramMap);
	}
	
	
	/**
	 * json格式的get请求
	 * 
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 */
	@SuppressWarnings("all")
	public String jsonGet(String url, Map<String, Object> paramMap, Map<String, Object> headerMap) {
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
	public String formDataPost(String url, Map<String, Object> paramMap) {
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		if(!CollectionUtils.isEmpty(paramMap)){
			for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
				params.add(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
        return restTemplate.postForEntity(url, new HttpEntity<MultiValueMap<String, String>>(params, headers), String.class).getBody();
	}

	
}
