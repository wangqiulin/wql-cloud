package com.wql.cloud.basic.wechatpay.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class SignUtil {

	public static String sign(Map<String, String> params, String key) {
		List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(params.entrySet());
		// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
		Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});
		// 组装签名
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : infoIds) {
			String k = entry.getKey();
			String value = String.valueOf(entry.getValue());
			if (k.equals(WXPayConstant.SIGN)) {
				continue;
			}
			if (value.trim().length() > 0) // 参数值为空，则不参与签名
				sb.append(k).append("=").append(value.trim()).append("&");
		}
		sb.append("key=").append(key);
		return MD5Util.MD5(sb.toString()).toUpperCase();
	}

	public static String getSignContent(Map<String, String> params) {
		if (params == null) {
			return null;
		}
		params.remove(WXPayConstant.SIGN);
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

	public static boolean checkSign(Map<String, String> params, String key) {
		if (params == null || StringUtils.isBlank(params.get(WXPayConstant.SIGN))) {
			return false;
		}
		String wxSign = params.remove(WXPayConstant.SIGN);
		String respSign = sign(params, key);
		System.out.println(wxSign);
		System.out.println(respSign);
		return wxSign.equals(respSign);
	}

}
