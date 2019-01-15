package com.wql.cloud.userservice.util;

import java.security.MessageDigest;
import java.util.Random;

public class MD5Util {
	public static String MD5(String s) {
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
	public static String saltMD5(String str) {
		String salt = genSalt();
		str = MD5(str + salt);
		char[] cs = new char[48];
		for (int i = 0; i < 48; i += 3) {
			cs[i] = str.charAt(i / 3 * 2);
			char c = salt.charAt(i / 3);
			cs[i + 1] = c;
			cs[i + 2] = str.charAt(i / 3 * 2 + 1);
		}
		return new String(cs);
	}

	private static String genSalt() {
		Random r = new Random();
		StringBuilder sb = new StringBuilder(16);
		sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
		int len = sb.length();
		if (len < 16) {
			for (int i = 0; i < 16 - len; i++) {
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
		boolean result;
		try {
			char[] cs1 = new char[32];
			char[] cs2 = new char[16];
			for (int i = 0; i < 48; i += 3) {
				cs1[i / 3 * 2] = md5.charAt(i);
				cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
				cs2[i / 3] = md5.charAt(i + 1);
			}
			String salt = new String(cs2);
			result = MD5(password + salt).equals(new String(cs1));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return result;
	}

	public static void main(String[] args) {
		String b = MD5Util.MD5("appid=wxd930ea5d5a258f4f&attach=传说这个数据会原样返回&body=万达信用公社-个人报告查询费用&detail=学历查询2元,车辆信息4元&device_info=1000&fee_type=CNY&goods_tag=WXG&limit_pay=no_credit&mch_id=10000100&nonce_str=f64720c3ffb6426a965b8fa59396a26e&notify_url=http://www.baidu.com&out_trade_no=20160815130247091945&spbill_create_ip=127.0.0.1&time_expire=20160815131247&time_start=20160815130247&total_fee=600&trade_type=APP&key=192006250b4c09247ec02edce69f6a2d");
		System.out.println(b.toUpperCase());
		// F7F4584053D4E8024495D2C890FF00B5
		// F7F4584053D4E8024495D2C890FF00B5
	}

}
