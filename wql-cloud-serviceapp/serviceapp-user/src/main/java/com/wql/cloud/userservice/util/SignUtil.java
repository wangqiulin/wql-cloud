package com.wql.cloud.userservice.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


public class SignUtil {

	public static String sign(Map<String, String> params, String key) {
		String signContent = getSignContent(params);
		System.out.println(signContent);
		if (signContent == null)
			return null;
		String str = signContent + "&key=" + key;
		System.out.println(str);
		return MD5Util.MD5(str).toUpperCase();
	}

	public static String getSignContent(Map<String, String> params) {
		if (params == null) {
			return null;
		}
		params.remove(WXPayConstont.SIGN);
		// params.remove(SIGN_TYPE);
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (StringUtils.isBlank(value))
				break;
			content.append((i == 0 ? "" : "&") + key + "=" + value);
		}
		return content.toString();
	}
	
	public static boolean checkSign(Map<String,String> params,String key){
		if(params==null||StringUtils.isBlank(params.get(WXPayConstont.SIGN))){
			return false;
		}
		String wxSign=params.remove(WXPayConstont.SIGN);
		String respSign=sign(params, key);
		System.out.println(wxSign);
		System.out.println(respSign);
        return wxSign.equals(respSign);
    }
	
	
}
