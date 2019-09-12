package com.wql.cloud.payservice.service;

import com.wql.cloud.payservice.pojo.req.CreatePayOrderReq;
import com.wql.cloud.payservice.pojo.req.CreateRefundOrderReq;
import com.wql.cloud.payservice.pojo.req.QueryPayOrderReq;
import com.wql.cloud.payservice.pojo.req.QueryRefundOrderReq;
import com.wql.cloud.payservice.pojo.res.CreatePayOrderRes;
import com.wql.cloud.payservice.pojo.res.CreateRefundOrderRes;
import com.wql.cloud.payservice.pojo.res.QueryPayOrderRes;
import com.wql.cloud.payservice.pojo.res.QueryRefundOrderRes;

/**
 * Author wangqiulin
 * Date  2019-04-13
 */
public interface PayOrderService {

	/**
	 * 创建支付订单
	 * 
	 * @param req
	 * @return
	 */
	CreatePayOrderRes createPayOrder(CreatePayOrderReq req);

	/**
	 * 查询支付订单结果
	 * 
	 * @param req
	 * @return
	 */
	QueryPayOrderRes queryPayOrder(QueryPayOrderReq req);

	/**
	 * 支付回调
	 * 
	 * @param channelWay
	 * @param data
	 */
	void payCallback(String channelWay, String data);
	
	/**
	 * 创建退款订单
	 * 
	 * @param req
	 * @return
	 */
	CreateRefundOrderRes createRefundOrder(CreateRefundOrderReq req);

	/**
	 * 查询退款订单结果
	 * 
	 * @param req
	 * @return
	 */
	QueryRefundOrderRes queryRefundOrder(QueryRefundOrderReq req);

	/**
	 * 退款回调
	 * 
	 * @param channelWay
	 * @param data
	 */
	void refundCallback(String channelWay, String data);

}
