package com.wql.cloud.basic.alipay.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayObject;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.wql.cloud.basic.alipay.config.AliPayConfig;
import com.wql.cloud.basic.alipay.model.CreateOrderModel;
import com.wql.cloud.basic.alipay.model.RefundOrderModel;
import com.wql.cloud.basic.alipay.result.PayNotifyResult;
import com.wql.cloud.basic.alipay.result.QueryOrderResult;
import com.wql.cloud.basic.alipay.result.QueryRefundOrderResult;
import com.wql.cloud.basic.alipay.service.AliPayService;

@Service
public class AliPayServiceImpl implements AliPayService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 销售产品码，商家和支付宝签约的产品码
	private static final String QUICK_MSECURITY_PAY = "QUICK_MSECURITY_PAY"; //app
	private static final String QUICK_WAP_PAY = "QUICK_WAP_PAY"; //H5

	private static final String TRADE_SUCCESS = "TRADE_SUCCESS";
	private static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
	private static final String TRADE_CLOSED = "TRADE_CLOSED";
	private static final String TRADE_FINISHED = "TRADE_FINISHED";
	private static final String TRADE_FAIL = "TRADE_FAIL";
	private static final String TRADE_UNKNOW = "TRADE_UNKNOW";

	@Autowired
	private AliPayConfig config;

	/**
	 * Android需要编码， IOS编码后需要再处理下
	 */
	@Override
	public String createOrderForApp(CreateOrderModel createOrderModel) {
		String orderStr = null;
		try {
			// 设置参数
			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
			model.setOutTradeNo(createOrderModel.getOutTradeNo());
			model.setTotalAmount(createOrderModel.getTotalAmount());
			model.setSubject(createOrderModel.getBody());
			model.setTimeoutExpress(createOrderModel.getTimeoutExpress());
			model.setProductCode(QUICK_MSECURITY_PAY);
			// 发起预下单请求
			AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
			request.setBizModel(model);
			request.setNotifyUrl(config.getPayNotifyUrl());
			AlipayTradeAppPayResponse response = getClient().sdkExecute(request);
			orderStr = response.getBody();
			orderStr = URLEncoder.encode(orderStr, "utf-8");
		} catch (AlipayApiException e) {
			logger.error("支付宝APP下单异常", e);
		} catch (UnsupportedEncodingException e) {
			logger.error("支付宝APP下单-URLEncoder编码异常", e);
		}
		return orderStr;
	}

	
	@Override
	public String createOrderForH5(CreateOrderModel createOrderModel) {
		String orderStr = null;
		try {
			// 设置参数
			AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
			model.setOutTradeNo(createOrderModel.getOutTradeNo());
			model.setTotalAmount(createOrderModel.getTotalAmount());
			model.setSubject(createOrderModel.getBody());
			model.setTimeoutExpress(createOrderModel.getTimeoutExpress());
			model.setProductCode(QUICK_WAP_PAY);
			// 发起预下单请求
			AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
			request.setBizModel(model);
			request.setNotifyUrl(config.getPayNotifyUrl());
			if(StringUtils.isNotBlank(createOrderModel.getReturnUrl())) {
				request.setReturnUrl(createOrderModel.getReturnUrl());
			}
			AlipayTradeWapPayResponse response = getClient().pageExecute(request);
			orderStr = response.getBody(); //返回的是一个form表单格式数据
		} catch (AlipayApiException e) {
			logger.error("支付宝H5下单异常", e);
		}
		return orderStr;
	}
	
	
	@Override
	public QueryOrderResult queryOrderByTradeNo(String outTradeNo) {
		try {
			AlipayTradeQueryModel model = new AlipayTradeQueryModel();
			model.setOutTradeNo(outTradeNo);
			AlipayTradeQueryResponse resp = execAliPayClient(model, new AlipayTradeQueryRequest());
			if (resp != null) {
				if(resp.isSuccess()) {
					String tradeStatus = resp.getTradeStatus();
					switch (tradeStatus) {
						case TRADE_SUCCESS:
							return new QueryOrderResult("支付成功", tradeStatus, resp.getSendPayDate());
						case WAIT_BUYER_PAY:
							return new QueryOrderResult("待支付", tradeStatus);
						case TRADE_CLOSED:
							return new QueryOrderResult("未付款交易超时关闭，或支付完成后全额退款", tradeStatus);
						case TRADE_FINISHED:
							return new QueryOrderResult("交易结束，不可退款", tradeStatus);
						default:
							break;
					}
				} else {
					String msg = resp.getSubCode() + "-" + resp.getSubMsg();
					return new QueryOrderResult(msg, TRADE_FAIL);
				}
			}
		} catch (Exception e) {
			logger.error("支付宝支付，查询支付结果异常", e);
		}
		return new QueryOrderResult("交易结果未知", TRADE_UNKNOW);
	}

	
	@Override
	public String refundOrder(RefundOrderModel refundOrderModel) {
		String strResponse = null;
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(config.SERVER_URL, 
					config.getAppId(), config.getPrivateKey(), config.FORMAT, 
					config.CHARSET, config.getPublicKey(), config.getSignType());
			// 设置参数
			AlipayTradeRefundModel model = new AlipayTradeRefundModel();
			model.setOutTradeNo(refundOrderModel.getOutTradeNo());
			model.setRefundAmount(refundOrderModel.getRefundAmount());
			// 发起请求
			AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
			request.setBizModel(model);
			AlipayTradeRefundResponse response = alipayClient.execute(request);
			strResponse = response.getCode();
			if ("10000".equals(response.getCode())) {
				strResponse = "success";
			} else {
				logger.info("【支付宝退款】请求失败：" + response.getSubMsg());
				strResponse = "fail";
			}
		} catch (Exception e) {
			logger.error("支付宝退款, 出现异常", e);
			strResponse = "fail";
		}
		return strResponse;
	}
	
	
	@Override
	public QueryRefundOrderResult queryRefundOrderByTradeNo(String outTradeNo, String outRequestNo) {
		QueryRefundOrderResult res = new QueryRefundOrderResult();
	    try {
	    	Map<String, String> map = new HashMap<>();
	    	map.put("out_trade_no", outTradeNo);
	    	map.put("out_request_no", outRequestNo);
	        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
	        request.setBizContent(JSON.toJSONString(map));
	        //退款查询
	        AlipayClient alipayClient = new DefaultAlipayClient(config.SERVER_URL, 
					config.getAppId(), config.getPrivateKey(), config.FORMAT, 
					config.CHARSET, config.getPublicKey(), config.getSignType());
	        AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
	        if (response.isSuccess()) {
	            res.setGmtRefundPay(response.getGmtRefundPay());
	            res.setRefundStatus(response.getRefundStatus());
	            return res;
	        }
	    } catch (Exception e) {
	    	logger.error("支付宝退款查询, 出现异常", e);
	    }
	    return null;
	}
	
	

	@Override
	public PayNotifyResult paySuccessNotify(Map<String, String> dataMap) {
		try {
			// 首先进行签名验证
			boolean signVerified = AlipaySignature.rsaCheckV1(dataMap, config.getPublicKey(), config.CHARSET, config.getSignType());
			if (!signVerified) {
				logger.warn("支付宝支付回调，签名失败");
				return null;
			}
			// 数据转换
			PayNotifyResult param = JSON.parseObject(JSON.toJSONString(dataMap), PayNotifyResult.class);
			if (StringUtils.equalsAny(param.getTradeStatus(), TRADE_SUCCESS, TRADE_FINISHED)) {
				return param;
			}
		} catch (AlipayApiException e) {
			logger.error("支付宝回调处理异常", e);
		}
		return null;
	}

	/**
	 * 从request域中读取出支付宝回调的内容
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<String, String>();
		Set<Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String name = entry.getKey();
			String[] values = entry.getValue();
			int valLen = values.length;
			if (valLen == 1) {
				retMap.put(name, values[0]);
			} else if (valLen > 1) {
				StringBuilder sb = new StringBuilder();
				for (String val : values) {
					sb.append(",").append(val);
				}
				retMap.put(name, sb.toString().substring(1));
			} else {
				retMap.put(name, "");
			}
		}
		return retMap;
	}

	/***************************** 以下为公共方法 **********************************/

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
		return new DefaultAlipayClient(config.SERVER_URL, config.getAppId(), config.getPrivateKey(), config.FORMAT,
				config.CHARSET, config.getPublicKey(), config.getSignType());
	}

}
