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
	
	/**
	 * 创蓝一键登录，获取手机号
	 * 
	 * @param appType : iOS or Android, 用于区分返回结果用哪个解密
	 * @param appId: 当前app对应的appid
	 * @param accessToken: 运营商token
	 * @param telecom: 运营商
	 * @param timestamp: 毫秒级时间戳
	 * @param randoms: 随机数
	 * @param sign: 签名
	 * @param version: SDK版本号
	 * @param device: 设备型号
	 * @return
	 * @throws Exception 
	 */
	String chuanglanOneClickLogin(String appType, String appId, String accessToken, String telecom, 
			String timestamp, String randoms, String sign, String version, String device) throws Exception;
	
}
