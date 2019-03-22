package com.wql.cloud.tool.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	
	/**
	 * 往前或往后，推迟多少天
	 * @param date
	 * @param day
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static Date plusDay(Date date, int day) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date); 
	    //把日期往后增加n天.正数往后推,负数往前移动 
	    calendar.add(calendar.DATE, day);
	    return calendar.getTime();  
	}
	
	/**
     * 获取指定年月的第一天， 返回格式：yyyy-MM-dd
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int year, int month) {     
        Calendar cal = Calendar.getInstance();   
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份 
        cal.set(Calendar.MONTH, month-1); 
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数 
        cal.set(Calendar.DAY_OF_MONTH, firstDay);  
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());  
    }
	
    
    /**
	 * 获取指定年月的最后一天； 返回格式：yyyy-MM-dd
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}
    

	/**
     * 获取第二天凌晨0点毫秒数
     */
    public static Date nextDayFirstDate() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    
    /**
     * 获取时间当天的开始时间(yyyy-MM-dd 00:00:00)
     * @param date
     * @return
     */
    public static final Date getDayDateStart(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
    
    
    /**
	 * 获取时间当天的结束时间 (yyyy-MM-dd 23:59:59)
	 * 
	 * @param date
	 * @return
	 */
	public static final Date getDayDateEnd(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 59);
		return cal.getTime();
	}
    
    /**
     * 获取当天的日期：yyyy-MM-dd
     */
    public static String getCurrentDate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());  
    }
    
    /**
     * 将yyyy-MM-dd HH:mm:ss格式的字符串类型时间转Date类型
     */
    public static Date transString2Date(String dataStr) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
        return sdf.parse(dataStr);
    }
    
    /**
     * 日期之间相差的数
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static long between(Date date1, Date date2) {
        return Math.abs(date2.getTime() - date1.getTime());
    }
    
    /**
     * 将Date类型转成yyyy-MM-dd HH:mm:ss格式的字符串类型
     * 
     * @return
     * @throws Exception
     */
    public static String tranDateToStr(Date date) throws Exception {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);  
    }


    /**
     * 当天最后一天到此刻之间相差的毫秒数
     * 
     * @param nowDate
     * @return
     * @throws ParseException
     */
    public static long betweenDayMillis(Date nowDate) throws ParseException {
        // 当天23:59:59
        String nowDayLast = new SimpleDateFormat("yyyy-MM-dd").format(nowDate) + " 23:59:59";
        Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(nowDayLast);
        // 当天最后一刻 到 此刻之间的毫秒数
        return Math.abs(date2.getTime() - nowDate.getTime());
    }

    /**
     * 当月最后一天到此刻之间相差的毫秒数
     * 
     * @param nowDate
     * @return
     * @throws ParseException
     */
    public static long betweenMonthMillis(Date nowDate) throws ParseException {
        // 当月23:59:59
        String nowDayLast = getLastDayByDate(nowDate) + " 23:59:59";
        Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(nowDayLast);
        // 当月最后一刻 到 此刻之间的毫秒数
        return Math.abs(date2.getTime() - nowDate.getTime());
    }

    /**
     * 获取当前月的最后一天
     * 
     * @param nowDate
     * @return
     */
    public static String getLastDayByDate(Date nowDate) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(nowDate);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        ca.add(Calendar.MONTH, 1);
        ca.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDate = ca.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(lastDate);
    }


}
