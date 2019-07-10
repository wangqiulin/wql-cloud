package com.wql.cloud.basic.oss.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
import com.wql.cloud.basic.oss.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	/**
	 * {@link} https://help.aliyun.com/document_detail/85198.html 
	 */
	@Override
	@SuppressWarnings("deprecation")
	public String oneClickLogin(String accessKeyId, String accessSecret, String accessToken) throws ServerException, ClientException {
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
	
}
