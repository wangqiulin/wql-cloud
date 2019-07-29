package com.wql.cloud.basic.redis.generateno;

/**
 * 单号生成类型枚举
 * 
 * @author wangqiulin
 *
 */
public enum FormNoTypeEnum {

	/**
     * 应付单单号：
     * 固定前缀：YF
     * 时间格式：yyyyMMdd
     * 流水号长度：7(当单日单据较多时可根据业务适当增加流水号长度)
     * 随机数长度：3
     */
    YF_ORDER("YF", FormNoConstants.SERIAL_YYYYMMDD_PREFIX, 7, 3),

    /**
     * 付款单单号：
     * 固定前缀：FK
     * 时间格式：yyyyMMdd
     * 流水号长度：7
     * 随机数长度：3
     */
    FK_ORDER("FK", FormNoConstants.SERIAL_YYYYMMDD_PREFIX, 7, 3),

    /**
     * 测试单单号：
     * 固定前缀：""
     * 时间格式：yyyyMMdd
     * 流水号长度：10
     * 随机数长度：0
     */
    TEST_ORDER("te", FormNoConstants.SERIAL_YYYYMMDD_PREFIX, 10, 0),
    ;

    /**
     * 单号前缀
     * 为空时填""
     */
    private String prefix;

    /**
     * 时间格式表达式
     * 例如：yyyyMMdd
     */
    private String datePattern;

    /**
     * 流水号长度
     */
    private Integer serialLength;
    
    /**
     * 随机数长度
     */
    private Integer randomLength;


    FormNoTypeEnum(String prefix, String datePattern, Integer serialLength, Integer randomLength) {
        this.prefix = prefix;
        this.datePattern = datePattern;
        this.serialLength = serialLength;
        this.randomLength = randomLength;
    }


	public String getPrefix() {
		return prefix;
	}


	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}


	public String getDatePattern() {
		return datePattern;
	}


	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}


	public Integer getSerialLength() {
		return serialLength;
	}


	public void setSerialLength(Integer serialLength) {
		this.serialLength = serialLength;
	}


	public Integer getRandomLength() {
		return randomLength;
	}


	public void setRandomLength(Integer randomLength) {
		this.randomLength = randomLength;
	}

	
}
