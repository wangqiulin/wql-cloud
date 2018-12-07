package com.wql.cloud.tool.idcard;


import org.apache.commons.lang3.StringUtils;

/**
 * 身份证换算年龄
 */
public class IdcardUtil {

    /**
     * 获取生日
     * 仅支持15位,18位身份证号
     *
     * @param idCard 身份证号
     * @return
     */
    public static String getBirthday(String idCard) {
        if (StringUtils.isNotBlank(idCard)) {
            StringBuffer buffer = new StringBuffer();
            switch (idCard.length()) {
                case 18:
                    buffer.append(idCard.substring(6).substring(0, 4))
                            .append("-")
                            .append(idCard.substring(10).substring(0, 2))
                            .append("-")
                            .append(idCard.substring(12).substring(0, 2));
                    return buffer.toString();
                case 15:
                    buffer.append("19" + idCard.substring(6, 8))
                            .append("-")
                            .append(idCard.substring(8, 10))
                            .append("-")
                            .append(idCard.substring(10, 12));
                    return buffer.toString();
                default:
            }
        }
        throw new RuntimeException("身份证号码不合法");
    }

    /**
     * 根据身份证判断性别
     *
     * @param idCard
     */
    public static Gender toGender(String idCard) {
        String id17 = idCard.substring(16, 17);
        if ((Integer.parseInt(id17) % 2) != 0) {
            return Gender.MEN;
        } else {
            return Gender.WOMEN;
        }
    }

    /**
     * 性别枚举
     */
    public enum Gender {
        MEN(1, "先生", "男"),
        WOMEN(0, "女士", "女");
        private int code;
        private String alias;
        private String sex;

        Gender(int code, String alias, String sex) {
            this.code = code;
            this.alias = alias;
            this.sex = sex;
        }

        public int getCode() {
            return code;
        }

        public String getAlias() {
            return alias;
        }

        public String getSex() {
            return sex;
        }
    }
    
}