package com.wql.cloud.userservice.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.wql.cloud.userservice.service.IModelFactory;
import com.wql.cloud.userservice.util.DateUtil;
import com.wql.cloud.userservice.util.RandomUtil;
import com.wql.cloud.userservice.util.UUIDUtil;
import com.wql.cloud.userservice.util.WXPayConstont;


@Repository("wxpayModelFactory")
public class WXPayModelFactory implements IModelFactory {
	
	/**
	 * 微信开放平台审核通过的应用APPID
	 */
	@Value("${wxpay.appId}")
	private String appId;

	/**
	 * 微信支付分配的商户号
	 */
	@Value("${wxpay.mchId}")
	private String mchId;

	/**
	 * 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
	 */
	@Value("${wxpay.notifyUrl}")
	private String notifyUrl;

	/**
	 * 创建订单请求
	 */
	public TreeMap<String, String> buildPlaceOrderRequset(Map<String, String> bizParams) {
		TreeMap<String, String> placeOrderRequset = new TreeMap<String, String>(bizParams);
		placeOrderRequset.put(WXPayConstont.APPID, appId);
		placeOrderRequset.put(WXPayConstont.MCH_ID, mchId);
		placeOrderRequset.put(WXPayConstont.NONCE_STR, UUIDUtil.getShortUuid());// 随机码
		placeOrderRequset.put(WXPayConstont.OUT_TRADE_NO, RandomUtil.transactionCode(3));// 交易号：精确到毫秒的时间+3位随机码组成
		placeOrderRequset.put(WXPayConstont.NOTIFY_URL, notifyUrl);
		placeOrderRequset.put(WXPayConstont.TRADE_TYPE, "APP");// 非必输，暂写死
		placeOrderRequset.put(WXPayConstont.LIMIT_PAY, "no_credit");// 非必输，暂写死
		placeOrderRequset.put(WXPayConstont.FEE_TYPE, "CNY");// 非必输，暂写死
		placeOrderRequset.put(WXPayConstont.DEVICE_INFO, "WEB");// 非必输，暂写死
		placeOrderRequset.put(WXPayConstont.DETAIL, "充值费");// 非必输，写死
		placeOrderRequset.put(WXPayConstont.ATTACH, "附加数据");// 非必输，写死。支付结果中会原样返回
		placeOrderRequset.put(WXPayConstont.GOODS_TAG, "WXG");// 非必输，写死
		Date now = new Date();
		placeOrderRequset.put(WXPayConstont.TIME_START, DateUtil.transferDateToString(now, DateUtil.DATE_FORMAT_2));
		placeOrderRequset.put(WXPayConstont.TIME_EXPIRE, DateUtil.getDateStr(now, Calendar.MINUTE, WXPayConstont.TIME_OUT, DateUtil.DATE_FORMAT_2));
		return placeOrderRequset;
	}

	/*
	 * 订单查询和订单关闭同时使用，如需修改需注意
	 */
	@Override
	public TreeMap<String, String> buildQueryTradeRequset(Map<String, String> bizParams) {
		TreeMap<String, String> queryTradeRequset = new TreeMap<String, String>(bizParams);
		queryTradeRequset.put(WXPayConstont.APPID, appId);
		queryTradeRequset.put(WXPayConstont.MCH_ID, mchId);
		queryTradeRequset.put(WXPayConstont.NONCE_STR, UUIDUtil.getShortUuid());// 随机码
		return queryTradeRequset;
	}

	/*
	 * 退款请求
	 */
	@Override
	public TreeMap<String, String> buildRefundRequset(Map<String, String> bizParams) {
		TreeMap<String, String> refundRequset = new TreeMap<String, String>(bizParams);
		refundRequset.put(WXPayConstont.APPID, appId);
		refundRequset.put(WXPayConstont.MCH_ID, mchId);
		refundRequset.put(WXPayConstont.NONCE_STR, UUIDUtil.getShortUuid());// 随机码
		refundRequset.put(WXPayConstont.OP_USER_ID, mchId);// 操作员帐号, 默认为商户号
		refundRequset.put(WXPayConstont.DEVICE_INFO, "WEB");// 非必输，暂写死
		refundRequset.put(WXPayConstont.FEE_TYPE, "CNY");// 非必输，暂写死
		return refundRequset;
	}

	/**
	 * 退款结果查询请求
	 */
	@Override
	public TreeMap<String, String> buildRefundQueryRequset(Map<String, String> bizParams) {
		TreeMap<String, String> refundRequset = new TreeMap<String, String>(bizParams);
		refundRequset.put(WXPayConstont.APPID, appId);
		refundRequset.put(WXPayConstont.MCH_ID, mchId);
		refundRequset.put(WXPayConstont.NONCE_STR, UUIDUtil.getShortUuid());// 随机码
		return refundRequset;
	}

}
