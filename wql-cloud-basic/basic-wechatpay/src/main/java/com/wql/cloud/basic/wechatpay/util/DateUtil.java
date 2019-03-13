package com.wql.cloud.basic.wechatpay.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.time.DateUtils;

public class DateUtil extends DateUtils {
	/**
	 * yyyy-MM-dd
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String yyyyMMddHHmm = "yyyy-MM-dd HH:mm";
	
	/**
	 * 时间格式常量：yyyyMMddHHmmssSSS
	 */
	public static final String DATE_FORMAT_1 = "yyyyMMddHHmmssSSS";
	
	/**
	 * 时间格式常量：yyyyMMddHHmmss
	 */
	public static final String DATE_FORMAT_2 = "yyyyMMddHHmmss";

	public static String transferDateToString(Date date, String dateFormat) {
		return new SimpleDateFormat(dateFormat).format(date);
	}

	/**
	 * 获取当前时间指定格式的字符串
	 * 
	 * @param dateFormat
	 * @return
	 */
	public static String getDateStr(String dateFormat) {
		return transferDateToString(new Date(), dateFormat);
	}

	/**
	 * string转Date
	 * 
	 * @param datestr
	 * @param dateFormat
	 * @return
	 */
	public static Date parseStrToDate(String datestr, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			return sdf.parse(datestr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取当前时间加减一段时间的时间，并返回指定格式的字符串
	 *
	 * @param date
	 *            Date对象
	 * @param calendarChangeType
	 *            加减的类型，示例：Calendar.MINUTE
	 * @param changeValue
	 *            加减的具体值，示例：10
	 * @param dateFormat
	 *            格式，示例：yyyyMMddHHmmssSSS
	 * @return
	 */
	public static String getDateStr(Date date, int calendarChangeType, int changeValue, String dateFormat) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);// 设置参数时间
		c.add(calendarChangeType, changeValue);
		return transferDateToString(c.getTime(), dateFormat);
	}

	/**
	 * 获取指定位数的毫秒值
	 * @param len
	 * @return
	 */
	public static String getTimeMillis(int len) {
		String str = String.valueOf(System.currentTimeMillis());
		return str.substring(0, len);
	}
	
	public static void main(String[] args) {
		System.out.println(DateUtil.getDateStr(new Date(),Calendar.MINUTE,-30,DateUtil.DATETIME_FORMAT));
	}
	
}
