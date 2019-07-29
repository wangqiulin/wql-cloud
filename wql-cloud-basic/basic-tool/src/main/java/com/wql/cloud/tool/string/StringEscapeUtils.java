package com.wql.cloud.tool.string;

@SuppressWarnings("deprecation")
public class StringEscapeUtils extends org.apache.commons.lang3.StringEscapeUtils{

	/**
	 * Java使用Properties文件存储中文时，会出现中文转成类似 \u5185\u5BB9\u7BA1\u7406\u7CFB\u7EDF的Unicode形式，
	 * 在读取这些字符的时候，就需要将其转回中文。
	 * @param unicodeStr
	 * @return
	 */
	public static String convert(String unicodeStr) {
		return StringEscapeUtils.unescapeJava(unicodeStr);
	}
	
}
