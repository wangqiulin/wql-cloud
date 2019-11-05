package com.wql.cloud.payservice.pojo.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_pay_payorder")
public class PayOrder extends BaseDO implements Serializable {

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
	 * 订单类型
	 */
	private Integer orderType;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 商品名称
	 */
	private String goodsName;

	/**
	 * 商品描述
	 */
	private String goodsDesc;

	/**
	 * 业务类型
	 */
	private String businessType;

	/**
	 * 业务描述
	 */
	private String businessDesc;

	/**
	 * 交易订单号
	 */
	private String outTradeNo;

	/**
	 * 实际支付金额
	 */
	private BigDecimal payAmount;

	/**
	 * 支付状态：1待支付，2支付成功，3支付失败
	 */
	private Integer payState;

	/**
	 * 支付结果描述
	 */
	private String payDesc;

	/**
	 * 支付完成时间
	 */
	private Date payedTime;

	/**
	 * 支付异步通知地址
	 */
	private String notifyUrl;

	/**
	 * 通知状态：0未通知，1通知成功，2通知失败
	 */
	private Integer notifyState;

	/**
	 * 通知结果的次数
	 */
	private Integer notifyCount;

	/**
	 * 支付同步通知地址
	 */
	private String returnUrl;

	/**
	 * 支付方式
	 */
	private String paymentWay;

	/**
	 * 支付通道简称
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

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getBusinessDesc() {
		return businessDesc;
	}

	public void setBusinessDesc(String businessDesc) {
		this.businessDesc = businessDesc;
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

	public Integer getPayState() {
		return payState;
	}

	public void setPayState(Integer payState) {
		this.payState = payState;
	}

	public String getPayDesc() {
		return payDesc;
	}

	public void setPayDesc(String payDesc) {
		this.payDesc = payDesc;
	}

	public Date getPayedTime() {
		return payedTime;
	}

	public void setPayedTime(Date payedTime) {
		this.payedTime = payedTime;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public Integer getNotifyState() {
		return notifyState;
	}

	public void setNotifyState(Integer notifyState) {
		this.notifyState = notifyState;
	}

	public Integer getNotifyCount() {
		return notifyCount;
	}

	public void setNotifyCount(Integer notifyCount) {
		this.notifyCount = notifyCount;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
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