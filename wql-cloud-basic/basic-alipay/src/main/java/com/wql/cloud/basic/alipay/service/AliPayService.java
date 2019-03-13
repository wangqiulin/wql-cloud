package com.wql.cloud.basic.alipay.service;

import com.wql.cloud.basic.alipay.model.CreateOrderModel;
import com.wql.cloud.basic.alipay.model.QueryOrderModel;
import com.wql.cloud.basic.alipay.result.QueryOrderResult;

/**
 * 支付宝
 */
public interface AliPayService {

    /**
     * 统一下单接口
     * @param model
     * @return
     */
	String createOrder(CreateOrderModel model);

    /**
     * 查询订单
     * @param model
     * @return
     */
	QueryOrderResult queryOrder(QueryOrderModel model);

}
