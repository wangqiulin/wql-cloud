package com.wql.cloud.basic.alipay.service;

import java.util.Map;

import com.wql.cloud.basic.alipay.model.CreateOrderModel;
import com.wql.cloud.basic.alipay.model.RefundOrderModel;
import com.wql.cloud.basic.alipay.result.PayNotifyResult;
import com.wql.cloud.basic.alipay.result.QueryOrderResult;
import com.wql.cloud.basic.alipay.result.QueryRefundOrderResult;

public interface AliPayService {

    /**
     * 统一下单接口-app
     * 
     * @param CreateOrderModel
     * @return
     */
	String createOrderForApp(CreateOrderModel createOrderModel);
	
	 /**
     * 统一下单接口-H5
     * 
     * @param CreateOrderModel
     * @return
     */
	String createOrderForH5(CreateOrderModel createOrderModel);

    /**
     * 查询支付订单结果
     * 
     * @param outTradeNo
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
	 * 查询退款订单结果
	 * 
	 * @param outTradeNo
	 * @param outRequestNo
	 * @return
	 */
	QueryRefundOrderResult queryRefundOrderByTradeNo(String outTradeNo, String outRequestNo);
	
	/**
	 * 支付回调校验（校验成功返回对象，失败返回null）
	 * 
	 * @param model
	 * @return
	 */
	PayNotifyResult paySuccessNotify(Map<String, String> dataMap);
	
}
