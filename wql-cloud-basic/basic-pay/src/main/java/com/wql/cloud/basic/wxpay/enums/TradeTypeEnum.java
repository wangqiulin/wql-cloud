package com.wql.cloud.basic.wxpay.enums;

/**
 * 微信支付的类型
 * 
 * @author wangqiulin
 *
 */
public enum TradeTypeEnum {

	APP("APP", "微信APP支付"), MWEB("MWEB", "微信H5支付"),;

	private String tradeType;

	private String tradeDesc;

	TradeTypeEnum(String tradeType, String tradeDesc) {
		this.tradeType = tradeType;
		this.tradeDesc = tradeDesc;
	}

	public String getTradeType() {
		return tradeType;
	}

	public String getTradeDesc() {
		return tradeDesc;
	}

}
