package com.wql.cloud.tool.idcard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 身份证的严格校验工具类
 */
public class IdcardValidUtil {

    private static final String REGULAR_NUM = "[0-9]+";

    private static final String REGULAR_DATE = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))"
            + "|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))"
            + "(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";

    private static volatile Map<String, String> areaCodeMap;

    /**
     * 身份证前两位地区编码
     */
    private static Map<String, String> getAreaCode() {
        if (areaCodeMap == null) {
            synchronized (IdcardValidUtil.class) {
                if (areaCodeMap == null) {
                    areaCodeMap = new HashMap<String, String>();
                    areaCodeMap.put("11", "北京");
                    areaCodeMap.put("12", "天津");
                    areaCodeMap.put("13", "河北");
                    areaCodeMap.put("14", "山西");
                    areaCodeMap.put("15", "内蒙古");
                    areaCodeMap.put("21", "辽宁");
                    areaCodeMap.put("22", "吉林");
                    areaCodeMap.put("23", "黑龙江");
                    areaCodeMap.put("31", "上海");
                    areaCodeMap.put("32", "江苏");
                    areaCodeMap.put("33", "浙江");
                    areaCodeMap.put("34", "安徽");
                    areaCodeMap.put("35", "福建");
                    areaCodeMap.put("36", "江西");
                    areaCodeMap.put("37", "山东");
                    areaCodeMap.put("41", "河南");
                    areaCodeMap.put("42", "湖北");
                    areaCodeMap.put("43", "湖南");
                    areaCodeMap.put("44", "广东");
                    areaCodeMap.put("45", "广西");
                    areaCodeMap.put("46", "海南");
                    areaCodeMap.put("50", "重庆");
                    areaCodeMap.put("51", "四川");
                    areaCodeMap.put("52", "贵州");
                    areaCodeMap.put("53", "云南");
                    areaCodeMap.put("54", "西藏");
                    areaCodeMap.put("61", "陕西");
                    areaCodeMap.put("62", "甘肃");
                    areaCodeMap.put("63", "青海");
                    areaCodeMap.put("64", "宁夏");
                    areaCodeMap.put("65", "新疆");
                    areaCodeMap.put("71", "台湾");
                    areaCodeMap.put("81", "香港");
                    areaCodeMap.put("82", "澳门");
                    areaCodeMap.put("91", "国外");
                }
            }
        }
        return areaCodeMap;
    }

    /**
     * 验证是否为合法的中国身份证号码
     *
     * @param cardNo 身份证号码
     * @return 是否合法
     */
    public static boolean isValidCardNo(String cardNo) {
        if (!StringUtils.isNoneBlank(cardNo)) {
            return false;
        }
        cardNo = cardNo.toUpperCase();
        // 长度必须为18位
        if (cardNo.trim().length() != 18) {
            return false;
        }
        final String ai = cardNo.substring(0, 17);
        // 必须全部是数字
        if (!isDigit(ai)) {
            return false;
        }
        // 年份
        String year = ai.substring(6, 10);
        // 月份
        String month = ai.substring(10, 12);
        // 日期
        String day = ai.substring(12, 14);
        // 验证日期格式
        if (!isDate(year + "-" + month + "-" + day)) {
            return false;
        }
        if (Integer.parseInt(year) < 1890 || Integer.parseInt(year) > Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()))) {
            return false;
        }
        // 验证地区号码
        if (getAreaCode().get(cardNo.substring(0, 2)) == null) {
            return false;
        }
        // 最后一位验证位
        String[] valCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        int totalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            totalmulAiWi = totalmulAiWi + Integer.parseInt(String.valueOf(ai.charAt(i))) * Integer.parseInt(wi[i]);
        }
        int modValue = totalmulAiWi % 11;
        String strVerifyCode = valCodeArr[modValue];
        if (!strVerifyCode.equals(cardNo.substring(17))) {
            return false;
        }
        return true;
    }


    /**
     * 是否合法时间格式 yyyy-MM-dd
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern.compile(REGULAR_DATE);
        Matcher m = pattern.matcher(strDate);
        return m.matches();
    }

    public static boolean isDigit(String input) {
        Pattern p = Pattern.compile(REGULAR_NUM);
        Matcher m = p.matcher(input);
        return m.matches();
    }

}
