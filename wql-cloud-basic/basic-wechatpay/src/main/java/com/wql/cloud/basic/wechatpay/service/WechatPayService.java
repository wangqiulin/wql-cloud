package com.wql.cloud.basic.wechatpay.service;

import com.wql.cloud.basic.wechatpay.model.PlaceOrderModel;
import com.wql.cloud.basic.wechatpay.model.QueryOrderModel;
import com.wql.cloud.basic.wechatpay.result.PlaceOrderResult;
import com.wql.cloud.basic.wechatpay.result.QueryOrderResult;

public interface WechatPayService {
	
	/**
	 * 下单
	 * @param model
	 * @return
	 */
	PlaceOrderResult placeOrder(PlaceOrderModel model);
	
	/**
	 * 
	 * 查询订单
	 * @param model
	 * @return
	 */
	QueryOrderResult queryOrder(QueryOrderModel model);
	
}
