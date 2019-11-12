package com.wql.cloud.basic.oss.service;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;

public interface LoginService {

	/**
	 * 阿里一键登录，获取手机号
	 * 
	 * @param accessKeyId
	 * @param accessSecret
	 * @param accessToken: app传入的参数
	 * @return
	 * @throws ClientException 
	 * @throws ServerException 
	 */
	String aliOneClickLogin(String accessKeyId, String accessSecret, String accessToken) throws ServerException, ClientException;
	
}
