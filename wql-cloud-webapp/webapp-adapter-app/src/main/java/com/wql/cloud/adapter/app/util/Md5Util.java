package com.wql.cloud.adapter.app.util;

import java.security.MessageDigest;
import java.util.Random;

public class Md5Util {
	public static String md5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 加盐MD5
	 */
	public static String saltMD5(String password, String salt) {
		password = md5(password + salt);
		char[] cs = new char[48];
		int num48 = 48;
		int num3 = 3;
		for (int i = 0; i < num48; i += num3) {
			cs[i] = password.charAt(i / 3 * 2);
			char c = salt.charAt(i / 3);
			cs[i + 1] = c;
			cs[i + 2] = password.charAt(i / 3 * 2 + 1);
		}
		return new String(cs);
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String genSalt() {
		Random r = new Random();
		StringBuilder sb = new StringBuilder(16);
		sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
		int len = sb.length();
		int num16 = 16;
		if (len < num16) {
			for (int i = 0; i < num16 - len; i++) {
				sb.append("0");
			}
		}
		String salt = sb.toString();
		return salt;
	}

	/**
	 * 校验
	 */
	public static boolean saltMD5Verify(String password, String md5) {
		char[] cs1 = new char[32];
		char[] cs2 = new char[16];
		int num48 = 48;
		int num3 = 3;
		for (int i = 0; i < num48; i += num3) {
			cs1[i / 3 * 2] = md5.charAt(i);
			cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
			cs2[i / 3] = md5.charAt(i + 1);
		}
		String salt = new String(cs2);
		return md5(password + salt).equals(new String(cs1));
	}
	
}
