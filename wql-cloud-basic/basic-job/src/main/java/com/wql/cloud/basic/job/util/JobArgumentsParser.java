package com.wql.cloud.basic.job.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.Assert;

import cn.hutool.core.date.DateUtil;

/**
 * 执行job
 * @param args 
 * 	job传入参数格式 ->
 *       1.不传默认统计昨日
 *       2.传入yyyy-MM-dd~yyyy-MM-dd统计开始时间~结束时间
 *       3.传入yyyy-MM-dd,yyyy-MM-dd,yyyy-MM-dd统计指定几个时间数据
 * @return
 * @throws Exception
 */
public interface JobArgumentsParser {

	/***
     * 年月日格式化
     */
    String pattern = "yyyy-MM-dd";

    /**
     * 时间段分隔符
     */
    String timeIntervalSplit = "~";

    /**
     * 多个时间分隔符
     */
    String multipleTimeSplit = ",";

    /**
     * 今日标识符
     */
    String today = "today";

    /**
     * 解析时间(数组)类型参数
     * * @param args job传入参数格式->
     * 1.不传默认统计昨日
     * 2.传入yyyy-MM-dd~yyyy-MM-dd统计开始时间~结束时间
     * 3.传入yyyy-MM-dd,yyyy-MM-dd,yyyy-MM-dd统计指定几个时间数据
     *
     * @param dateListArgs
     * @return
     */
    default List<String> analysisDateListArgs(String dateListArgs) {
        List<String> result = new ArrayList<>();
        dateListArgs = trim(dateListArgs);
        try {
            if (StringUtils.isEmpty(dateListArgs)) {
                result.add(analysisDateArgs(dateListArgs));
            } else if (dateListArgs.contains(timeIntervalSplit)) {
                //传入时间区间 解析
                String[] times = dateListArgs.split(timeIntervalSplit);
                if (times.length != 2) {
                    throw new RuntimeException("传入时间区间格式有误");
                }
                result = getStart2EndDays(times[0], times[1]);
            } else if (dateListArgs.contains(multipleTimeSplit)) {
                //传入时间点 解析
                String[] times = dateListArgs.split(multipleTimeSplit);
                for (String time : times) {
                    if (StringUtils.isNotEmpty(time)) {
                        time = analysisDateArgs(time);
                        if (!result.contains(time)) {
                            result.add(time);
                        }
                    }
                }
            } else {
                result.add(analysisDateArgs(dateListArgs));
            }
        } catch (Exception e) {
            throw new RuntimeException("执行job传入参数格式有误args(" + dateListArgs + ")", e);
        }
        return result;
    }

    /**
     * 获取开始时间到结束时间期间所有时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @throws ParseException
     */
    default List<String> getStart2EndDays(String startTime, String endTime) throws ParseException {
        Date startDate = DateUtils.parseDate(analysisDateArgs(startTime), pattern);
        Date endDate = DateUtils.parseDate(analysisDateArgs(endTime), pattern);
        Assert.isTrue(startDate.compareTo(endDate) <= 0, "结束时间不能大于开始时间");
        List<String> result = new ArrayList<>();
        while (true) {
        	String dateStr = DateUtil.offsetDay(startDate, 0).toDateStr();
            result.add(dateStr);
            startDate = DateUtils.addDays(startDate, 1);
            if (startDate.compareTo(endDate) > 0) {
                break;
            }
        }
        return result;
    }
    
    
    /**
     * 解析时间类型参数
     *
     * @param dateArg
     * @return
     */
    default String analysisDateArgs(String dateArg) {
        dateArg = trim(dateArg);
        if (StringUtils.isEmpty(dateArg)) {
            //没有传递默认返回昨日时间
            return DateUtil.offsetDay(new Date(), -1).toDateStr();
        }
        try {
            int index = dateArg.toLowerCase().indexOf(today);
            if (index == -1) {
                //不存在today字符串,认为是yyyy-MM-dd字符串
                DateUtils.parseDate(dateArg, pattern);
                return dateArg;
            } else if (index == 0) {
                //以today开头认为是计算表达式
                if (dateArg.equalsIgnoreCase(today)) {
                    //仅是today返回今日时间
                    return DateUtil.today();
                } else {
                    //为表达式,需要计算
                    String num = dateArg.substring(today.length());
                    int n;
                    try {
                        n = Integer.parseInt(num.replace(" ", ""));
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("解析含有today的算式(" + dateArg + ")失败");
                    }
                    return DateUtil.offsetDay(new Date(), n).toDateStr();
                }
            } else {
                //中间含有today暂时无法识别,抛出异常
                throw new RuntimeException("参数中间含有today无法解析");
            }
        } catch (Exception e) {
            throw new RuntimeException("执行job传入参数格式有误args(" + dateArg + ")", e);
        }
    }
    

    /**
     * 参数trim
     *
     * @param args
     * @return
     */
    default String trim(String args) {
        if (args != null) {
            args = args.trim();
        }
        return args;
    }
    
	
}
