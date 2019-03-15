package com.wql.cloud.basic.wechatpay.service;

import com.wql.cloud.basic.wechatpay.model.PlaceOrderModel;
import com.wql.cloud.basic.wechatpay.result.PayNotifyResult;
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
	QueryOrderResult queryOrderByTradeNo(String outTradeNo);
	
	/**
	 * 回调验签
	 * @param xmlStr
	 * @return
	 */
	PayNotifyResult paySuccessNotify(String xmlStr);
	
}
