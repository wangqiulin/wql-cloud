package com.wql.cloud.payservice.pojo.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_pay_refundorder")
public class RefundOrder extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 应用id
	 */
	private String appId;

	/**
	 * 用户code
	 */
	private String userCode;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 退款订单号
	 */
	private String outRefundNo;
	
	/**
	 * 交易订单号
	 */
	private String outTradeNo;

	/**
	 * 订单金额
	 */
	private BigDecimal payAmount;
	
	/**
	 * 退款金额
	 */
	private BigDecimal refundAmount;

	/**
	 * 退款状态：1待退款，2退款成功，3退款失败
	 */
	private Integer refundState;

	/**
	 * 退款结果描述
	 */
	private String refundDesc;

	/**
	 * 退款完成时间
	 */
	private Date refundTime;

	/**
	 * 退款异步通知地址
	 */
	private String notifyUrl;

	/**
	 * 通知状态：0未通知，1通知成功，2通知失败
	 */
	private Integer nofityState;

	/**
	 * 通知结果的次数
	 */
	private Integer nofityCount;

	/**
	 * 退款方式
	 */
	private String paymentWay;

	/**
	 * 退款通道简称
	 */
	private String channelWay;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Integer getRefundState() {
		return refundState;
	}

	public void setRefundState(Integer refundState) {
		this.refundState = refundState;
	}

	public String getRefundDesc() {
		return refundDesc;
	}

	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public Integer getNofityState() {
		return nofityState;
	}

	public void setNofityState(Integer nofityState) {
		this.nofityState = nofityState;
	}

	public Integer getNofityCount() {
		return nofityCount;
	}

	public void setNofityCount(Integer nofityCount) {
		this.nofityCount = nofityCount;
	}

	public String getPaymentWay() {
		return paymentWay;
	}

	public void setPaymentWay(String paymentWay) {
		this.paymentWay = paymentWay;
	}

	public String getChannelWay() {
		return channelWay;
	}

	public void setChannelWay(String channelWay) {
		this.channelWay = channelWay;
	}

}