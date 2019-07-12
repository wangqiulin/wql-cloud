package com.wql.cloud.basic.oss.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.wql.cloud.basic.oss.config.ChuangLanConfig;
import com.wql.cloud.basic.oss.service.LoginService;
import com.wql.cloud.basic.oss.util.DES;


@Service
public class LoginServiceImpl implements LoginService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired
	private ChuangLanConfig chuangLanConfig;
	
	@Autowired
    private RestTemplate restTemplate;
	
	/**
	 * {@link} https://help.aliyun.com/document_detail/85198.html 
	 */
	@Override
	@SuppressWarnings("deprecation")
	public String aliOneClickLogin(String accessKeyId, String accessSecret, String accessToken) throws ServerException, ClientException {
		DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, accessKeyId);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dypnsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("GetMobile");
        request.setProtocol(ProtocolType.HTTPS);
        request.putQueryParameter("AccessToken", accessToken);
        logger.info("阿里一键登录，原始请求token：{}", accessToken);
        CommonResponse response = client.getCommonResponse(request);
        String result = response.getData();
        logger.info("阿里一键登录，原始返回结果：{}", result);
        if(StringUtils.isNotBlank(result)) {
			JSONObject jo = JSONObject.parseObject(result);
			if("OK".equals(jo.getString("Code"))) { //OK 成功码
				JSONObject jo2 = jo.getJSONObject("GetMobileResultDTO");
				if(jo2 != null) {
					return jo2.getString("Mobile");
				}
			}
		}
        return null;
	}
	
	
	@Override
	public String chuanglanOneClickLogin(String appType, String appId, String accessToken, String telecom, String timestamp,
			String randoms, String sign, String version, String device) throws Exception {
		String url = null;
		switch (telecom) {
			case "CMCC": url = chuangLanConfig.getCmccUrl();   break;
			case "CUCC": url = chuangLanConfig.getCuccUrl();   break;
			case "CTCC": url = chuangLanConfig.getCtccUrl();   break;
			default: 
				Assert.notNull(url, "运营商有误");  break;
		}
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("appId", appId);
		paramMap.put("accessToken", accessToken);
		paramMap.put("telecom", telecom);
		paramMap.put("timestamp", timestamp);
		paramMap.put("randoms", randoms);
		paramMap.put("version", version);
		paramMap.put("device", device);
		paramMap.put("sign", sign);
		logger.info("创蓝一键登录---请求参数：{}", JSONObject.toJSONString(paramMap));
		String result = postFormData(url, paramMap);
		logger.info("创蓝一键登录---原始返回结果：{}", JSONObject.toJSONString(result));
        if(StringUtils.isNotBlank(result)) {
			JSONObject jo = JSONObject.parseObject(result);
			if("200000".equals(jo.getString("code"))) { //200000 成功码
				JSONObject jo2 = jo.getJSONObject("data");
				if(jo2 != null) {
					String appKey = "iOS".equals(appType) ? chuangLanConfig.getIosAppKey() : chuangLanConfig.getAndroidAppKey();
					return DES.decryptDES(jo2.getString("mobileName"), appKey);
				}
			} 
		}
		return null;
	}
	
    public String postFormData(String url, Map<String, Object> paramMap) {
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
