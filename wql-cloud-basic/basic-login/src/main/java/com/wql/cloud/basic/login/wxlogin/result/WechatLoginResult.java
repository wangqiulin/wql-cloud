package com.wql.cloud.basic.login.wxlogin.result;

public class WechatLoginResult {

	/**
	 * 用户昵称
	 */
	private String nickName;   
	
	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	 */
	private String sex; 
	
	/**
	 * 用户唯一标识
	 */
	private String openid;
	
	/**
	 * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的 unionid 是唯一的。
	 */
	private String unionid; 
	
	/**
	 * 在用户修改微信头像后，旧的微信头像 URL 将会失效，因此开发者应该自己在获取用户信息后，将头像图片保存下来，避免微信头像 URL 失效后的异常情况。
	 */
	private String headimgurl;  
	
	private String country;  

	private String province;  
	
	private String city;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	} 
	
}
