package com.wql.cloud.basic.login.wxlogin.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wql.cloud.basic.login.wxlogin.WechatLoginService;
import com.wql.cloud.basic.login.wxlogin.result.WechatLoginResult;

@Service
public class WechatLoginServiceImpl implements WechatLoginService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Override
	public WechatLoginResult wechatLogin(String appId, String secret, String authorizationCode) {
		if(StringUtils.isBlank(authorizationCode)) {
			return null;
		}
		//第一步根据authorizationCode，获取access_token
        String queryTokenUrl = this.getWebAccessUrl(appId, secret, authorizationCode);
        String tokenRes = this.httpRequest(queryTokenUrl, "GET", null);
        JSONObject jo = JSON.parseObject(tokenRes);
        if(jo == null) {
        	return null;
        }
        String errcode = jo.getString("errcode");
        if(StringUtils.isNotBlank(errcode)) {
        	return null;
        }
        //根据access_token，获取用户信息
        String queryUserinfoUrl = this.getUserinfoUrl(jo.getString("access_token"), jo.getString("openid"));
        String userinfoRes = this.httpRequest(queryUserinfoUrl, "GET", null);
        JSONObject jo2 = JSON.parseObject(userinfoRes);
        if (jo2 == null) {
        	return null;
        }
        //返回结果
        WechatLoginResult result = new WechatLoginResult();
        result.setUnionid(jo2.getString("unionid"));
        result.setOpenid(jo2.getString("openid"));
        result.setNickName(jo2.getString("nickname"));
        result.setHeadimgurl(jo2.getString("headimgurl"));
        result.setSex(jo2.getString("sex"));
        result.setCountry(jo2.getString("country"));
        result.setProvince(jo2.getString("province"));
        result.setCity(jo2.getString("city"));
        return result;
	}
	
	
	/***************************私有方法******************************/

	// 获取code的请求地址，redirect_uri需要去微信公众号里面配置；回调地址不能写http://或者https://开头就好直接写你的www.xxx.com这样的格式 
	//scope -> snsapi_base ，不弹出授权页面，直接跳转，只能获取用户openid ;  snsapi_userinfo 弹出授权页面，可通过openid拿到昵称、性别、所在地
	public String getCodeUrl(String APPID, String REDIRECT_URI, String SCOPE) {
		return String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=STAT#wechat_redirect", 
				APPID, REDIRECT_URI, SCOPE);
	}

	// 获取Web_access_tokenhttps的请求地址
	public String getWebAccessUrl(String APPID, String SECRET, String CODE) {
		return String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", APPID, SECRET, CODE);
	}

	//接口用于获取用户个人信息
	public String getUserinfoUrl(String access_token, String openid) {
		return String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN", access_token, openid);
	}
	
	//处理http请求  requestUrl为请求地址  requestMethod请求方式，值为"GET"或"POST"
   	public String httpRequest(String requestUrl, String requestMethod, String outputStr){
   		StringBuffer buffer=null;
   		try{
	   		URL url=new URL(requestUrl);
	   		HttpURLConnection conn=(HttpURLConnection)url.openConnection();
	   		conn.setDoOutput(true);
	   		conn.setDoInput(true);
	   		conn.setRequestMethod(requestMethod);
	   		conn.connect();
	   		//往服务器端写内容 也就是发起http请求需要带的参数
	   		if(null != outputStr){
	   			OutputStream os=conn.getOutputStream();
	   			os.write(outputStr.getBytes("utf-8"));
	   			os.close();
	   		}
	   		//读取服务器端返回的内容
	   		InputStream is=conn.getInputStream();
	   		InputStreamReader isr=new InputStreamReader(is,"utf-8");
	   		BufferedReader br=new BufferedReader(isr);
	   		buffer=new StringBuffer();
	   		String line=null;
	   		while((line=br.readLine())!=null){
	   			buffer.append(line);
	   		}
	   		logger.info("[weixin]: do get request({}), and get response({}).", url, buffer.toString());
   		} catch(Exception e){
   			logger.error("", e);
   		}
   		return buffer.toString();
   	}

	
}
