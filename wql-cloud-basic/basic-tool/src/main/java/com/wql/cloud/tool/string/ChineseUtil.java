package com.wql.cloud.tool.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class ChineseUtil {

	/**
	 * 判断中文字符
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c){
	    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	    if(ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
	       ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ||
	       ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A ||
	       ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B ||
	       ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ||
	       ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS ||
	       ub == Character.UnicodeBlock.GENERAL_PUNCTUATION){
	        return true;
	    }
	    return false;
	}

	/**
	 * 包含中文字符
	 * @param str
	 * @return
	 */
	public static boolean containsChinese(String str){
	    char[] ch = str.toCharArray();
	    for(char c : ch){
	        if(isChinese(c)){
	            return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * 是否中文（非标点符号）
	 * @param c
	 * @return
	 */
	public static boolean isChineseWord(char c) {
		char[] ch = {c};
		Pattern pattern = Pattern.compile("([\u4E00-\uFA29]|[\uE7C7-\uE7F3])");
		Matcher matcher = pattern.matcher(new String(ch));
		return matcher.find();
	}
	
	/**
	 * 包含汉字（非标点符号）
	 * @param name
	 * @return
	 */
	public static boolean containsChineseWords(String name) {
		Pattern pattern = Pattern.compile("^.*([\u4E00-\uFA29]|[\uE7C7-\uE7F3])+.*$");
		Matcher matcher = pattern.matcher(name);
		return matcher.find();
	}
	
	/**
	 * 转换成拼音字母缩写
	 * @param str
	 * @return abbr
	 */
	public static String toPinyinAbbr(String str){
		String abbr = "";
		for(char c : str.toCharArray()){
			if(ChineseUtil.isChineseWord(c)){
				abbr += PinyinHelper.toHanyuPinyinStringArray(c)[0].substring(0, 1).toUpperCase();
			}
		}
		return abbr;
	}
	
	
	// 名字长度
    private static int NAME_LENGTH = 3;
    
    // 将汉字转换为全拼
    public static String getPingYin(String src) {
        char[] name = src.toCharArray();
        String[] newName = new String[name.length];
        HanyuPinyinOutputFormat pyFormat = new HanyuPinyinOutputFormat();
        pyFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        pyFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        pyFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        String account = "";
        int length = name.length;
        try {
            // 名字大于等于3个字的时候，姓取全称,名取首字母。
            if(length>=NAME_LENGTH){
                for (int i = 0; i < length; i++) {
                    // 截取姓
                    if(i==0){
                        // 判断是否为汉字字符
                        if (Character.toString(name[i]).matches("[\\u4E00-\\u9FA5]+")) {
                            newName = PinyinHelper.toHanyuPinyinStringArray(name[i], pyFormat);
                            account += newName[0];
                        } else
                            account += Character.toString(name[i]);
                    }else{
                        account += getPinYinHeadChar(Character.toString(name[i]));
                    }

                }
            }else{
               // 只有2个字的名字,账号是名字的拼音全称
                for (int i = 0; i < length; i++) {
                    // 判断是否为汉字字符
                    if (Character.toString(name[i]).matches("[\\u4E00-\\u9FA5]+")) {
                        newName = PinyinHelper.toHanyuPinyinStringArray(name[i], pyFormat);
                        account += newName[0];
                    } else
                        account += Character.toString(name[i]);
                }
            }
            return account;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return account;
    }

    // 返回中文的首字母
    public static String getPinYinHeadChar(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }
	
}