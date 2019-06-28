package com.wql.cloud.basic.wechatpay.util;

import java.security.MessageDigest;

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
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加盐MD5
	 */
//	public static String saltMD5(String str) {
//		String salt = genSalt();
//		str = MD5(str + salt);
//		char[] cs = new char[48];
//		for (int i = 0; i < 48; i += 3) {
//			cs[i] = str.charAt(i / 3 * 2);
//			char c = salt.charAt(i / 3);
//			cs[i + 1] = c;
//			cs[i + 2] = str.charAt(i / 3 * 2 + 1);
//		}
//		return new String(cs);
//	}
//
//	private static String genSalt() {
//		Random r = new Random();
//		StringBuilder sb = new StringBuilder(16);
//		sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
//		int len = sb.length();
//		if (len < 16) {
//			for (int i = 0; i < 16 - len; i++) {
//				sb.append("0");
//			}
//		}
//		String salt = sb.toString();
//		return salt;
//	}
//
//	/**
//	 * 校验
//	 */
//	public static boolean saltMD5Verify(String password, String md5) {
//		boolean result;
//		try {
//			char[] cs1 = new char[32];
//			char[] cs2 = new char[16];
//			for (int i = 0; i < 48; i += 3) {
//				cs1[i / 3 * 2] = md5.charAt(i);
//				cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
//				cs2[i / 3] = md5.charAt(i + 1);
//			}
//			String salt = new String(cs2);
//			result = MD5(password + salt).equals(new String(cs1));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		return result;
//	}
	
	/**
     * 十六进制下数字到字符的映射数组
     */
    private final static String[] hexDigits = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"}; 
    
    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }


    /**
     * @Title: byteArrayToHexString
     * @Description: 轮换字节数组为十六进制字符串 
     * @author yihj
     * @param @param b
     * @param @return    参数
     * @return String    返回类型
     * @throws
     */
    private static String byteArrayToHexString(byte[] b){ 
        StringBuffer resultSb = new StringBuffer(); 
        for(int i=0;i<b.length;i++){ 
            resultSb.append(byteToHexString(b[i])); 
        } 
        return resultSb.toString(); 
    } 
    
    /**
     * @Title: byteToHexString
     * @Description: 将一个字节转化成十六进制形式的字符串 
     * @author yihj
     * @param @param b
     * @param @return    参数
     * @return String    返回类型
     * @throws
     */
    private static String byteToHexString(byte b){ 
        int n = b; 
        if(n<0) 
        n=256+n; 
        int d1 = n/16; 
        int d2 = n%16; 
        return hexDigits[d1] + hexDigits[d2]; 
    } 

}
