package com.wql.cloud.basic.alipay.service;

import java.util.Map;

import com.wql.cloud.basic.alipay.model.CreateOrderModel;
import com.wql.cloud.basic.alipay.model.RefundOrderModel;
import com.wql.cloud.basic.alipay.result.CreateRefundOrderResult;
import com.wql.cloud.basic.alipay.result.PayNotifyResult;
import com.wql.cloud.basic.alipay.result.QueryOrderResult;
import com.wql.cloud.basic.alipay.result.QueryRefundOrderResult;

public interface AliPayService {

    /**
     * app下单接口-app
     * 
     * @param createOrderModel
     * @return
     */
	String createOrderForApp(CreateOrderModel createOrderModel);
	
	 /**
     * 手机网站支付下单接口-H5
     * 
     * 	vue移动端h5调用支付宝支付接口： 
     * 		https://blog.csdn.net/weixin_39361971/article/details/83186341
     * 
     * 	重点关键代码：
     *  	const div = document.createElement('div')
            div.innerHTML = form //此处form就是后台返回接收到的数据
            document.body.appendChild(div)
            document.forms[0].submit()
     * 
     * @param createOrderModel
     * @return
     */
	String createOrderForH5(CreateOrderModel createOrderModel);

	/**
	 * 电脑网站支付下单
	 * 
	 * @param createOrderModel
	 * @return
	 */
	String createOrderForPC(CreateOrderModel createOrderModel);
	
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
	 * @param refundOrderModel
	 * @return
	 */
	CreateRefundOrderResult refundOrder(RefundOrderModel refundOrderModel);
	
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
	 * @param dataMap
	 * @return
	 */
	PayNotifyResult paySuccessNotify(Map<String, String> dataMap);
	
}
