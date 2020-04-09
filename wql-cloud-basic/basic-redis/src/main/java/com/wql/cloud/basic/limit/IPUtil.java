package com.wql.cloud.basic.limit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * IP地址工具类
 */
public class IPUtil {

	/**
	 * 获取Ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAdrress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		
		if(StringUtils.isNotBlank(ip)) {
			// 多个路由时，取第一个非unknown的ip
			final String[] arr = ip.split(",");
			for (final String str : arr) {
				if (!"unknown".equalsIgnoreCase(str)) {
					ip = str;
					break;
				}
			}
		}
		return ip;
	}
	
}
