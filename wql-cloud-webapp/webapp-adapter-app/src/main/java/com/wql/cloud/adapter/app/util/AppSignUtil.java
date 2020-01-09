package com.wql.cloud.adapter.app.util;

import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;

public class AppSignUtil {
	
	public static final String sign = "sjdo12379341jik890879&*#@$^&*Q*&";

	/**
	 * 系统级参数
	 */
	private static HashMap<String, Object> getSystem() {
		HashMap<String, Object> system = new HashMap<>();
		system.put("identifier", "com.wisdom.market.dev");
		system.put("appType", "Android");
		system.put("appVersion", "1.0.0");
		system.put("bundleVersion", "278");
		system.put("channel", "default_channel");
		system.put("deviceId", "ac3f97e8e00d93a0");
		system.put("systemVersion", "8.1.0");
		system.put("hardware","ONEPLUS A5010");
		return system;
	}
	
	/**
	 * 用户信息 TODO
	 */
	private static HashMap<String, Object> getSession() {
		//String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0YTljYTQyZjE5NzE0MDAwOTU1NGU3Mzg0Y2ZlYjVmMCIsImlzcyI6InNobGgiLCJzdWIiOiJ7XCJ1c2VyQ29kZVwiOlwiMTExXCJ9IiwiaWF0IjoxNTI3ODQzODU0fQ.cp7fperYriVfEovzOSuYB3QbKh23Yks2dV7u5YTb84E";
		HashMap<String, Object> session = new HashMap<>();
//		session.put("userId", "111");
//		session.put("token", token);
		return session;
	}

	/**
	 * 具体参数 TODO
	 */
	private static String getData() {
		HashMap<String, Object> map = new HashMap<>();
		return JSONObject.toJSONString(map);
	}

	public static void main(String[] args) {
		String apiKey = "wisdom.credit.index.getCreditShopList"; // TODO

		String data = getData();
		HashMap<String, Object> session = getSession();
		String token = (String) session.get("token");
		if (token == null) {
			token = "";
		}
		String signStr = Md5Util.md5(apiKey + token + data + sign).toUpperCase();

		HashMap<String, Object> request = new HashMap<>();
		request.put("apiKey", apiKey);
		request.put("data", data);
		request.put("session", session);
		request.put("system", getSystem());
		request.put("sign", signStr);

		System.out.println(JSONObject.toJSONString(request));// 请求入参
	}

}
