package com.wql.cloud.tool.pinyin;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @Description: 中文拼音工具类
 */
public class ChinesePinyinUtil {

	/**
	 * 得到 汉字的全拼
	 * 
	 * @param src 中文字符串
	 * @return
	 */
	public static String getPingYin(String src) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		StringBuilder sb = new StringBuilder();
		char[] srcArray = src.toCharArray();
		try {
			for (int i = 0; i < srcArray.length; i++) {
				// 判断是否为汉字字符
				if (java.lang.Character.toString(srcArray[i]).matches("[\\u4E00-\\u9FA5]+")) {
					String[] targetArray = PinyinHelper.toHanyuPinyinStringArray(srcArray[i], format);
					sb.append(targetArray[0]);
				} else {
					sb.append(java.lang.Character.toString(srcArray[i]));
				}
			}
			return sb.toString();
		} catch (BadHanyuPinyinOutputFormatCombination e1) {
			e1.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 得到中文首字母,例如"专科"得到zk返回
	 * 
	 * @param str 中文字符串
	 * @return
	 */
	public static String getPinYinHeadChar(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char word = str.charAt(i);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				sb.append(pinyinArray[0].charAt(0));
			} else {
				sb.append(word);
			}
		}
		return convert(sb.toString());
	}

	/**
	 * 将字符串转移为ASCII码
	 * 
	 * @param cnStr 中文字符串
	 * @return
	 */
	public static String getCnASCII(String cnStr) {
		StringBuilder sb = new StringBuilder();
		byte[] bGBK = cnStr.getBytes();
		for (int i = 0; i < bGBK.length; i++) {
			sb.append(Integer.toHexString(bGBK[i] & 0xff));
		}
		return sb.toString();
	}

	public static String convert(String str) {
		String after = "";
		char[] split = str.toCharArray();
		for (int i = 0; i < split.length; i++) {
			char dataChar = split[i];
			if (dataChar < 91 && dataChar > 64) {// 判断如果为大写字母则进入此方法
				dataChar = (char) ((int) dataChar + 32);// 加上相差值，变为大写字母
			} else if (dataChar > 96 && dataChar < 123) {// 判断如果为小写字母，则进入此方法
				dataChar = (char) ((int) dataChar - 32);// 减去相差值，变为小写字母
			}
			after += dataChar;
		}
		return after.trim();
	}

}
