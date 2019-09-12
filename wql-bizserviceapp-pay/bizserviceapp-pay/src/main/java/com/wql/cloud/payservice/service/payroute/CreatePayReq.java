package com.wql.cloud.payservice.service.payroute;

import java.math.BigDecimal;

public class CreatePayReq {

	/**
	 * 支付交易订单号
	 */
	private String outTradeNo;
	
	/**
	 * 商品描述
	 */
	private String goodsDesc;

	/**
	 * 实际支付金额
	 */
	private BigDecimal payAmount;

	/**
	 * 微信支付-下单ip
	 */
	private String createIp;
	
	/**
	 * 支付宝H5支付-同步通知地址
	 */
	private String returnUrl;

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

}
