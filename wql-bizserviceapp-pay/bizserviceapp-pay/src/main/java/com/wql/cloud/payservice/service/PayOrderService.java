package com.wql.cloud.payservice.service;

import com.wql.cloud.payservice.pojo.req.CreatePayOrderReq;
import com.wql.cloud.payservice.pojo.req.QueryPayOrderReq;
import com.wql.cloud.payservice.pojo.res.CreatePayOrderRes;
import com.wql.cloud.payservice.pojo.res.QueryPayOrderRes;

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
	
}
