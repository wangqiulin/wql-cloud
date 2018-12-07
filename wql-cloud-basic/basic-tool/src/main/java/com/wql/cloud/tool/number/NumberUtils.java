package com.wql.cloud.tool.number;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 数字工具类
 * @author wangqiulin
 *
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

	/**
	 * 格式化number 默认去掉尾数0
	 *
	 * @param number
	 * @return
	 */
	public static String formatNumber(Number number) {
		return formatNumber(number, "#,###.##");
	}

	/**
	 * 格式化number,自定义pattern 
	 * <a href="https://blog.csdn.net/thunder4393/article/details/1739911">pattern定义</>
	 *
	 * @param number
	 * @param pattern
	 * @return
	 */
	public static String formatNumber(Number number, String pattern) {
		if (number == null) {
			return null;
		}
		return new DecimalFormat(pattern).format(number);
	}

	/**
	 * 两个BigDecimal进行比较
	 *
	 * @param v1 值1
	 * @param a 比较符号
	 * @param v2  值2
	 * @return
	 */
	public static boolean compare(BigDecimal v1, Aperator a, BigDecimal v2) {
		if (v1 == null || v2 == null || a == null) {
			throw new NullPointerException("比较方法参数存在null值");
		}
		return ArrayUtils.contains(a.getCode(), v1.compareTo(v2));
	}

	/**
	 * 计算运算符
	 */
	public enum Aperator {
		EQ(new int[] { 0 }, "等于"), 
		LT(new int[] { -1 }, "小于"), 
		GT(new int[] { 1 }, "大于"), 
		LT_EQ(new int[] { -1, 0 }, "小于等于"), 
		GT_EQ(new int[] { 0, 1 }, "大于等于");
		
		private int[] code;
		private String desc;

		Aperator(int[] code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		public int[] getCode() {
			return code;
		}
		public String getDesc() {
			return desc;
		}
	}
	
	/**
	 * 生成{size}位随机数字
	 * @return
	 */
	public static String generateRandom(int size) {
		return String.valueOf((Math.random() * 9 + 1) * 100000).substring(0, size);
	}

}
