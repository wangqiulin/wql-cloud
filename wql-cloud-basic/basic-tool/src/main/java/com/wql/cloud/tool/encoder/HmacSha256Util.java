package com.wql.cloud.tool.encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HmacSha256Util {

	private static final Logger logger = LoggerFactory.getLogger(HmacSha256Util.class);
	
	// SECRET KEY
//	private final static String secret_key = "ndE2jdZNFixH9G6Aidsfyf7lYT3PxW";


	/**
	 * sha256_HMAC加密
	 * 
	 * @param message 消息
	 * @param secret  秘钥
	 * @return 加密后字符串
	 */
	public static String sha256_HMAC(String message, String secret) {
		String hash = "";
		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
			hash = byteArrayToHexString(bytes);
		} catch (Exception e) {
			logger.error("Error HmacSHA256 ===========" + e);
		}
		return hash;
	}
	
	/**
	 * 将加密后的字节数组转换成字符串
	 * 
	 * @param b 字节数组
	 * @return 字符串
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append('0');
			hs.append(stmp);
		}
		return hs.toString().toLowerCase();
	}

}
