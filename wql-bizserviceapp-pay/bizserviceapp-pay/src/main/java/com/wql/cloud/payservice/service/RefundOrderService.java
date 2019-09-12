package com.wql.cloud.payservice.service;

import com.wql.cloud.payservice.pojo.req.CreateRefundOrderReq;
import com.wql.cloud.payservice.pojo.req.QueryRefundOrderReq;
import com.wql.cloud.payservice.pojo.res.CreateRefundOrderRes;
import com.wql.cloud.payservice.pojo.res.QueryRefundOrderRes;

public interface RefundOrderService {

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
