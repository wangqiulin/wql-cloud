package com.wql.cloud.tool.string;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
	
    /**
     * 格式化字符串
     *
     * @param format      格式
     * @param argNullable 参数是否可以为空
     * @param args        参数列表
     * @return 格式化后的字符串
     */
    private static String format(String format, boolean argNullable, Object... args) {
        if (args == null || args.length == 0) {
            return format;
        }
        String[] strArgs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            String argStr = "";
            if (args[i] != null) {
                argStr = String.valueOf(args[i]);
            } else {
                if (argNullable == false) {
                    throw new IllegalArgumentException(MessageFormat.format(
                            "StringUtils.format arg cannot be null。 format:{0}, args:{1}",
                            format, JsonUtils.toJsonString(args)));
                }
            }
            strArgs[i] = argStr;
        }
        return MessageFormat.format(format, strArgs);
    }

    /**
     * 格式化字符串
     * <p>
     * 注意: 直接用MessageFormat.format数字会带逗号, 用此方法不会
     *
     * @param format 格式, 例如book-{0}-{1}, {0}和{1}分别是第一个和第二个参数
     * @param args   参数列表，必须和format里面的参数标识符个数一致， 参数值不能为null, 否则会抛出异常
     * @return 格式化后的字符串
     * @throws IllegalArgumentException 参数为空时抛出
     */
    public static String format(String format, Object... args) {
        return format(format, false, args);
    }

    /**
     * 手机号去敏(隐藏中间四位,若传入参数格式有误原样返回)
     *
     * @param mobile 待去敏手机号必须11位
     * @return
     */
    public static String mobileEncrpty(String mobile) {
        if (mobile == null || mobile.length() != 11) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 真实姓名去敏(隱藏)
     *
     * @param realName 待去敏姓名
     * @return
     */
    public static String nameEncrpty(String realName) {
        return realName == null ? null :
                realName.replaceAll("([\\u4e00-\\u9fa5]{1}).*({1})", "$1*" + (realName.length() > 2 ? realName.charAt(realName.length() - 1) : ""));
    }

    /**
     * 身份证去敏(隐藏中间八位,若传入参数格式有误原样返回)
     *
     * @param idCard 待去敏身份证
     * @return
     */
    public static String idcardEncrpty(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return idCard;
        }
        return idCard.replaceAll("(\\d{6})\\d{8}(\\w{4})", "$1********$2");
    }

    /**
     * 银行卡去敏(前四后三)
     *
     * @param bankCard
     * @return
     */
    public static String bankCardEncrpty(String bankCard) {
        if (bankCard != null) {
            int length = bankCard.length();
            if (length >= 16 && length <= 19) {
                return bankCard.replaceAll("(\\d{4})\\d{" + (length - 4 - 3) + "}(\\d{3})", "$1************$2");
            }
        }
        return bankCard;
    }

    /**
     * 银行卡,保留后四位
     * @param bankCard
     * @return
     */
    public static String bankCardLast4(String bankCard) {
        if (bankCard != null) {
            int length = bankCard.length();
            if (length >= 16 && length <= 19) {
                return bankCard.replaceAll("\\d{" + (length - 4) + "}(\\d{4})", "************$1");
            }
        }
        return bankCard;
    }
    
    
    /**
     * 修改字符串编码 iso-8859-1 to utf-8
     *
     * @param s 待转换字符串
     * @return
     */
    public static String iso2utf8(String s) {
        return coverCharset(s, "iso-8859-1", "utf-8");
    }

    /**
     * 转换字符编码
     *
     * @param s           待转换字符串
     * @param fromCharset 源字符集
     * @param toCharset   目标字符集
     * @return
     */
    public static String coverCharset(String s, String fromCharset, String toCharset) {
        if (isNotEmpty(s)) {
            try {
                return new String(s.getBytes(fromCharset), toCharset);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return s;
    }

}
