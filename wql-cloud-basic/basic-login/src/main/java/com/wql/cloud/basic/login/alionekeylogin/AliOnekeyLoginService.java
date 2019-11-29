package com.wql.cloud.basic.login.alionekeylogin;

import java.rmi.ServerException;

import com.aliyuncs.exceptions.ClientException;

public interface AliOnekeyLoginService {

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
	String aliOnekeyLogin(String accessKeyId, String accessSecret, String accessToken) throws ServerException, ClientException;
	
}
