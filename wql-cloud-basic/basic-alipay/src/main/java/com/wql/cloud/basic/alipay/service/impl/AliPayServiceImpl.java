package com.wql.cloud.basic.alipay.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayObject;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.wql.cloud.basic.alipay.config.AliPayConfig;
import com.wql.cloud.basic.alipay.model.CreateOrderModel;
import com.wql.cloud.basic.alipay.model.QueryOrderModel;
import com.wql.cloud.basic.alipay.result.QueryOrderResult;
import com.wql.cloud.basic.alipay.service.AliPayService;

@Service
public class AliPayServiceImpl implements AliPayService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired(required=false)
    private AliPayConfig config;

    /**
     * 发起预下单
     */
    @Override
    public String createOrder(CreateOrderModel createOrderModel) {
    	//设置参数
    	AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
    	model.setOutTradeNo(createOrderModel.getOutTradeNo());
        model.setTotalAmount(createOrderModel.getTotalAmount());
        model.setSubject(createOrderModel.getBody());
        model.setTimeoutExpress(createOrderModel.getTimeoutExpress());
        model.setProductCode("QUICK_MSECURITY_PAY"); //销售产品码，商家和支付宝签约的产品码
        String orderStr = null;
        try {
            //发起预下单请求
            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
            request.setBizModel(model);
            request.setNotifyUrl(config.getPayNotifyUrl());
            AlipayTradeAppPayResponse response = getClient().sdkExecute(request);
            orderStr = response.getBody();
        } catch (AlipayApiException e) {
        	logger.error("支付宝下单异常", e);
        }
        return orderStr;
    }

    /**
     * 查询订单结果
     */
    @Override
    public QueryOrderResult queryOrder(QueryOrderModel queryOrderModel) {
    	try {
			//设置订单号参数
			AlipayTradeQueryModel model = new AlipayTradeQueryModel();
			model.setOutTradeNo(queryOrderModel.getOutTradeNo());
			//发起请求
			AlipayTradeQueryResponse queryResponse = execAliPayClient(model, new AlipayTradeQueryRequest());
			if(queryResponse != null) {
				if(!queryResponse.isSuccess()) {
					return new QueryOrderResult(false, "支付宝查询支付结果失败");
				}
				String tradeStatus = queryResponse.getTradeStatus();
				switch (tradeStatus) {
					case "TRADE_SUCCESS":
						return new QueryOrderResult(true, "支付成功", tradeStatus, queryResponse.getSendPayDate());
					case "WAIT_BUYER_PAY":
						return new QueryOrderResult(true, "待支付", tradeStatus);
					case "TRADE_CLOSED":
						return new QueryOrderResult(true, "未付款交易超时关闭，或支付完成后全额退款", tradeStatus);
					case "TRADE_FINISHED":
						return new QueryOrderResult(true, "交易结束，不可退款", tradeStatus);
					default:
						break;
				}
			}
		} catch (Exception e) {
			logger.error("支付宝支付，查询支付结果异常", e);
		}
        return new QueryOrderResult(false, "支付宝查询支付结果异常");
    }

    
    
    /*****************************以下为公共方法**********************************/
    
    private <R extends AlipayResponse> R execAliPayClient(AlipayObject dataModel, AlipayRequest<R> request) {
        try {
            request.setBizModel(dataModel);
            return getClient().execute(request);
        } catch (AlipayApiException e) {
            logger.error("支付宝接口调用失败,", e);
            throw new RuntimeException("支付宝下单失败", e);
        }
    }

    private AlipayClient getClient() {
        return new DefaultAlipayClient(
                config.getServerUrl(),
                config.getAppId(),
                config.getPrivateKey(),
                config.getFormat(),
                config.getCharset(),
                config.getPublicKey(),
                config.getSignType());
    }
    
}
