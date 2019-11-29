package com.wql.cloud.basic.login.wxlogin;

import com.wql.cloud.basic.login.wxlogin.result.WechatLoginResult;

public interface WechatLoginService {

	/**
	 * 微信登录
	 * 主要步骤：
	 * 		1.打开微信获取授权authorization_code
			2.使用authorization_code、AppID、AppSecret获取access_token
			3.使用access_token获取微信部分个人信息
	 * 
	 * @param appId
	 * @param secret
	 * @param authorizationCode
	 * @return
	 */
	WechatLoginResult wechatLogin(String appId, String secret, String authorizationCode);
	
}
