package com.wql.cloud.basic.wechatpay.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 下单请求参数
 * 
 * @author wangqiulin
 *
 */
public class PlaceOrderModel {

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

	/**
	 * 订单生效时间
	 */
	private Date timeStart;

	/**
	 * 订单失效时间
	 */
	private Date timeExpire;

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

	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public Date getTimeExpire() {
		return timeExpire;
	}

	public void setTimeExpire(Date timeExpire) {
		this.timeExpire = timeExpire;
	}

}
