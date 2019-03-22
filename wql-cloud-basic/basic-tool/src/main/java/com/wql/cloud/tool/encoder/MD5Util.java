package com.wql.cloud.tool.encoder;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

	/**
	 * md5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		return DigestUtils.md5Hex(str);
	}
	
}
