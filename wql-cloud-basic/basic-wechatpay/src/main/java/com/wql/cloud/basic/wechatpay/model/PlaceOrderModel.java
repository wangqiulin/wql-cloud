package com.wql.cloud.basic.wechatpay.model;

import java.math.BigDecimal;

import com.wql.cloud.basic.wechatpay.enums.TradeTypeEnum;

/**
 * 下单请求参数
 * @author wangqiulin
 *
 */
public class PlaceOrderModel {

	/**
	 * 支付方式
	 */
	private TradeTypeEnum tradeTypeEnum;
	
	/**
	 * 下单订单号
	 */
	private String outTradeNo;
	
	/**
	 * 支付金额（元）
	 */
	private BigDecimal totalFee;
	
	/**
	 * 下单ip
	 */
	private String createIp;
	
	/**
	 * 商品描述
	 */
	private String body;
	
	public TradeTypeEnum getTradeTypeEnum() {
		return tradeTypeEnum;
	}

	public void setTradeTypeEnum(TradeTypeEnum tradeTypeEnum) {
		this.tradeTypeEnum = tradeTypeEnum;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	
}
