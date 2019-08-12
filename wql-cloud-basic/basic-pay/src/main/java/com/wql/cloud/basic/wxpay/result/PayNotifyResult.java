package com.wql.cloud.basic.wxpay.result;

import java.util.Date;

/**
 * 支付成功回调对象
 * @author wangqiulin
 *
 */
public class PayNotifyResult {
	
	/**
	 * 交易订单号
	 */
	private String outTradeNo;
	
	/**
	 * 支付完成时间
	 */
	private Date payedTime;

	public Date getPayedTime() {
		return payedTime;
	}

	public void setPayedTime(Date payedTime) {
		this.payedTime = payedTime;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	
}
