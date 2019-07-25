package com.wql.cloud.basic.wechatpay.service;

import com.wql.cloud.basic.wechatpay.model.PlaceOrderModel;
import com.wql.cloud.basic.wechatpay.model.RefundOrderModel;
import com.wql.cloud.basic.wechatpay.result.PayNotifyResult;
import com.wql.cloud.basic.wechatpay.result.PlaceOrderResult;
import com.wql.cloud.basic.wechatpay.result.QueryOrderResult;
import com.wql.cloud.basic.wechatpay.result.RefundNotifyResult;

public interface WechatPayService {
	
	/**
	 * 下单-App支付
	 * 
	 * @param model
	 * @return
	 */
	PlaceOrderResult placeOrderForApp(PlaceOrderModel model);
	
	/**
	 * 下单-H5支付
	 * 
	 * @param model
	 * @return
	 */
	PlaceOrderResult placeOrderForH5(PlaceOrderModel model);
	
	/**
	 * 查询订单
	 * 
	 * @param model
	 * @return
	 */
	QueryOrderResult queryOrderByTradeNo(String outTradeNo);
	
	/**
	 * 退款请求
	 * 
	 * @param RefundOrderModel
	 * @return
	 */
	String refundOrder(RefundOrderModel refundOrderModel);
	
	/**
	 * 查询退款结果
	 * 
	 * @param outTradeNo
	 * @return
	 */
	QueryOrderResult queryRefundOrderByTradeNo(String outTradeNo);
	
	/**
	 * 支付完成回调验签
	 * 
	 * @param xmlStr
	 * @return
	 */
	PayNotifyResult paySuccessNotify(String xmlStr);
	
	/**
	 * 退款回调验签
	 *
	 * @param xmlStr
	 * @return
	 */
	RefundNotifyResult refundNotify(String xmlStr);
	
}
