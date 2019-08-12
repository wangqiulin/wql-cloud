package com.wql.cloud.basic.wxpay.result;

import java.util.Date;

/**
 * 退款回调对象
 * @author wangqiulin
 *
 */
public class RefundNotifyResult {
	
	/**
	 * 交易订单号
	 */
	private String outTradeNo;
	
	/**
	 * 退款订单号
	 */
	private String outRefundNo;
	
	/**
	 * 退款状态：
	 * 	SUCCESS-退款成功
		CHANGE-退款异常
		REFUNDCLOSE—退款关闭
	 */
	private String refundStatus;
	
	/**
	 * 退款完成时间
	 */
	private Date successTime;

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public Date getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}

}
