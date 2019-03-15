package com.wql.cloud.basic.alipay.service;

import java.util.Map;

import com.wql.cloud.basic.alipay.model.CreateOrderModel;
import com.wql.cloud.basic.alipay.result.PayNotifyResult;
import com.wql.cloud.basic.alipay.result.QueryOrderResult;

public interface AliPayService {

    /**
     * 统一下单接口
     * 
     * @param model
     * @return
     */
	String createOrder(CreateOrderModel model);

    /**
     * 查询订单
     * 
     * @param outTradeNo
     * @return
     */
	QueryOrderResult queryOrderByTradeNo(String outTradeNo);

	/**
	 * 支付回调校验（校验成功返回对象，失败返回null）
	 * 
	 * @param model
	 * @return
	 */
	PayNotifyResult paySuccessNotify(Map<String, String> dataMap);
	
}
