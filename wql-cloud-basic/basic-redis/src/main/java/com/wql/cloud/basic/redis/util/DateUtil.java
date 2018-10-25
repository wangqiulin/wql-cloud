package com.wql.cloud.basic.redis.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

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
     * 获取当天最后23：59:59点毫秒数
     */
    public static Date currentDayLastDate() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, 0);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }
    
    
    /**
     * 获取 当天位于一年中的第几天
     * @return
     */
    public static Integer getDayOfYear(){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	Integer dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
    	System.out.println(dayOfYear);
    	return dayOfYear;
    }
    
    
    /**
     * 获取当天是哪一年
     * @return
     */
    public static Integer getCurrentYear(){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	Integer year = cal.get(Calendar.YEAR);
    	return year;
    }
    
    
    /**
     * 获取当天的日期：yyyy-MM-dd
     */
    public static String getCurrentDate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());  
        return date;
    }
    
    /**
     * 将yyyy-MM-dd HH:mm:ss格式的字符串类型时间转Date类型
     */
    public static Date transString2Date(String dataStr) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
        Date date = sdf.parse(dataStr);
        return date;
    }
    
    
    /**
     * 日期之间相差的数
     * @param date1
     * @param date2
     * @return
     */
    public static long between(Date date1, Date date2) {
        return Math.abs(date2.getTime() - date1.getTime());
    }
    
    
    /**
     * 将Date类型转成yyyy-MM-dd HH:mm:ss格式的字符串类型
     * @return
     * @throws Exception
     */
    public static String tranDateToStr(Date date) throws Exception {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);  
    }


    /** 当天最后一天到此刻之间相差的毫秒数 */
    public static long betweenDayMillis(Date nowDate) throws ParseException {
        // 当天23:59:59
        String nowDayLast = new SimpleDateFormat("yyyy-MM-dd").format(nowDate) + " 23:59:59";
        Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(nowDayLast);
        // 当天最后一刻 到 此刻之间的毫秒数
        return Math.abs(date2.getTime() - nowDate.getTime());
    }

    /** 当月最后一天到此刻之间相差的毫秒数 */
    public static long betweenMonthMillis(Date nowDate) throws ParseException {
        // 当月23:59:59
        String nowDayLast = lastDate(nowDate) + " 23:59:59";
        Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(nowDayLast);
        // 当月最后一刻 到 此刻之间的毫秒数
        return Math.abs(date2.getTime() - nowDate.getTime());
    }

    /** 获取当前月的最后一天 */
    public static String lastDate(Date nowDate) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(nowDate);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        ca.add(Calendar.MONTH, 1);
        ca.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDate = ca.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(lastDate);
    }


}
