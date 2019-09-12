package com.wql.cloud.payservice.service.payroute;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.Callable;

import com.wql.cloud.payservice.pojo.domain.PayOrder;
import com.wql.cloud.payservice.pojo.domain.RefundOrder;
import com.wql.cloud.tool.httpclient.HttpUtil;

public interface PayRouteFactory {

	static final String APP_ALIPAY = "app-alipay";
	static final String H5_ALIPAY = "h5-alipay";
	static final String APP_WXPAY = "app-wxpay";
	static final String H5_WXPAY = "h5-wxpay";
	
	String getChannel();
	
	/**
	 * 创建支付订单
	 * 
	 * @param createPayReq
	 * @return
	 */
	String createPayOrder(CreatePayReq createPayReq);
	
	/**
	 * 查询支付订单结果
	 * 
	 * @param payOrder
	 */
	void queryPayOrder(PayOrder payOrder);
	
	/**
	 * 支付结果回调
	 * 
	 * @param data
	 */
	void payCallback(String data);
	
	/**
	 *  创建退款订单
	 * 
	 * @param outRefundNo
	 * @param outTradeNo
	 * @param payAmount
	 * @param refundAmount
	 */
	void createRefundOrder(String outRefundNo, String outTradeNo, BigDecimal payAmount, BigDecimal refundAmount);
	
	/**
	 * 查询退款订单结果
	 * 
	 * @param refundOrder
	 */
	void queryRefundOrder(RefundOrder refundOrder);

	/**
	 * 退款结果回调
	 * 
	 * @param data
	 */
	void refundCallback(String data);
	
	class NotifyCallable implements Callable<String> {

		private String notifyUrl;
		private Map<String, Object> paramMap;
		private HttpUtil httpUtil;
		
		public NotifyCallable(String notifyUrl, Map<String, Object> paramMap, HttpUtil httpUtil) {
			this.notifyUrl = notifyUrl;
			this.paramMap = paramMap;
			this.httpUtil = httpUtil;
		}

		@Override
		public String call() throws Exception {
			String data = httpUtil.jsonPost(notifyUrl, paramMap);
			if("SUCCESS".equals(data)) {
				return "SUCCESS";
			}
			return "FAIL";
		}
	}
	
}
