package com.wql.cloud.payservice.biz.payroute.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wql.cloud.basic.alipay.model.CreateOrderModel;
import com.wql.cloud.basic.alipay.model.RefundOrderModel;
import com.wql.cloud.basic.alipay.result.CreateRefundOrderResult;
import com.wql.cloud.basic.alipay.result.PayNotifyResult;
import com.wql.cloud.basic.alipay.result.QueryOrderResult;
import com.wql.cloud.basic.alipay.result.QueryRefundOrderResult;
import com.wql.cloud.basic.alipay.service.AliPayService;
import com.wql.cloud.payservice.biz.payroute.PayRouteService;
import com.wql.cloud.payservice.mapper.PayOrderMapper;
import com.wql.cloud.payservice.mapper.RefundOrderMapper;
import com.wql.cloud.payservice.pojo.domain.PayOrder;
import com.wql.cloud.payservice.pojo.domain.RefundOrder;
import com.wql.cloud.tool.executor.TaskExecutorService;
import com.wql.cloud.tool.httpclient.HttpUtil;

@Service("app-alipayService")
public class AliAppPayServiceImpl implements PayRouteService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());  
	
	@Autowired
	private AliPayService aliPayService;
	@Autowired
	private HttpUtil httpUtil;
	@Autowired
	private TaskExecutorService taskExecutorService;
	@Autowired
	private PayOrderMapper payOrderMapper;
	@Autowired
	private RefundOrderMapper refundOrderMapper;
	
	@Override
	public String createPayOrder(CreatePayReq createPayReq) {
		Assert.isTrue(StringUtils.isNotBlank(createPayReq.getOutTradeNo()), "outTradeNo不能为空");
		Assert.notNull(createPayReq.getPayAmount(), "payAmount不能为空");
		Assert.isTrue(createPayReq.getPayAmount().compareTo(BigDecimal.ZERO) == 1, "payAmount不能小于0");
		//支付下单
		CreateOrderModel createOrderModel = new CreateOrderModel();
		createOrderModel.setOutTradeNo(createPayReq.getOutTradeNo());
		createOrderModel.setTotalAmount(createPayReq.getPayAmount());
		createOrderModel.setReturnUrl(createPayReq.getReturnUrl());
		createOrderModel.setBody(StringUtils.isBlank(createPayReq.getGoodsDesc()) ? "购买" : createPayReq.getGoodsDesc());
		createOrderModel.setTimeoutExpress("30m"); 
		String data = aliPayService.createOrderForApp(createOrderModel);
		Assert.isTrue(StringUtils.isNotBlank(data), "支付宝下单失败");
		return data;
	}
	

	@Override
	public void queryPayOrder(PayOrder payOrder) {
		String outTradeNo = payOrder.getOutTradeNo();
		Assert.isTrue(StringUtils.isNotBlank(outTradeNo), "outTradeNo不能为空");
		QueryOrderResult result = aliPayService.queryOrderByTradeNo(outTradeNo);
		switch (result.getTradeStatus()) {
			case "TRADE_SUCCESS":
				payOrder.setPayedTime(result.getPayedTime());
				payOrder.setPayState(2);
				payOrder.setPayDesc("支付成功");
				payOrder.setUpdateDate(new Date());
				payOrderMapper.updateByPrimaryKeySelective(payOrder);
				//异步回调通知, 最好用mq
				if(StringUtils.isNotBlank(payOrder.getNotifyUrl())) {
					try {
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("orderNo", payOrder.getOrderNo());
						paramMap.put("payedTime", result.getPayedTime());
						ArrayList<String> list = taskExecutorService.submit(new NotifyCallable(payOrder.getNotifyUrl(), paramMap, httpUtil), 1);
						if(!CollectionUtils.isEmpty(list)) {
							String successFlag = list.get(0);
							if("SUCCESS".equals(successFlag)) {
								payOrder.setNotifyCount(payOrder.getNotifyCount() + 1);
								payOrder.setNotifyState(1);
								payOrderMapper.updateByPrimaryKeySelective(payOrder);
							}
						}
					} catch (Exception e) {
						logger.error("支付宝退款成功，通知出现异常【不影响主流程，故抓住】", e);
					}
				}
				break;
			case "TRADE_CLOSED":
				payOrder.setPayedTime(result.getPayedTime());
				payOrder.setPayState(3);
				payOrder.setPayDesc("未付款交易超时关闭，或支付完成后全额退款");
				payOrder.setUpdateDate(new Date());
				payOrderMapper.updateByPrimaryKeySelective(payOrder);
				break;
			case "TRADE_FINISHED":
				payOrder.setPayedTime(result.getPayedTime());
				payOrder.setPayState(2);
				payOrder.setPayDesc("交易结束，不可退款");
				payOrder.setUpdateDate(new Date());
				payOrderMapper.updateByPrimaryKeySelective(payOrder);
				break;
			case "TRADE_FAIL":
				payOrder.setPayedTime(result.getPayedTime());
				payOrder.setPayState(3);
				payOrder.setPayDesc("支付失败");
				payOrder.setUpdateDate(new Date());
				payOrderMapper.updateByPrimaryKeySelective(payOrder);
				break;
			case "TRADE_UNKNOW":
			case "WAIT_BUYER_PAY":	
				break;
			default:
				break;
		}
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public void payCallback(String data) {
		JSONObject jsonObject = JSON.parseObject(data);//String转json
		Map<String, String> dataMap = JSONObject.toJavaObject(jsonObject, Map.class);
		PayNotifyResult result = aliPayService.paySuccessNotify(dataMap);
		if(result == null) {
			return ;
		}
		//验签成功后的处理逻辑
        PayOrder payOrder = new PayOrder();
        payOrder.setOutTradeNo(result.getOutTradeNo());
        payOrder = payOrderMapper.selectOne(payOrder);
        if(payOrder != null) {
        	this.queryPayOrder(payOrder);
        }
	}
	
	
	
	@Override
	public void createRefundOrder(String outRequestNo, String outTradeNo, BigDecimal payAmount, BigDecimal refundAmount) {
		Assert.isTrue(StringUtils.isNotBlank(outTradeNo), "outTradeNo不能为空");
		Assert.notNull(payAmount, "支付金额不能为空");
		Assert.notNull(refundAmount, "退款金额不能为空");
		Assert.isTrue(refundAmount.compareTo(BigDecimal.ZERO) == 1, "退款金额不能小于0");
		Assert.isTrue(refundAmount.compareTo(payAmount) <= 0, "退款金额不能大于支付金额");
		Assert.isTrue(StringUtils.isNotBlank(outRequestNo), "outRequestNo不能为空");
		//退款下单
		RefundOrderModel refundOrderModel = new RefundOrderModel();
		refundOrderModel.setOutTradeNo(outTradeNo);
		refundOrderModel.setOutRequestNo(outRequestNo);
		refundOrderModel.setRefundAmount(refundAmount);
		CreateRefundOrderResult createRefundOrderResult = aliPayService.refundOrder(refundOrderModel);
		Assert.isTrue(createRefundOrderResult.getResult(), "支付宝退款请求失败");
	}
	

	@Override
	public void queryRefundOrder(RefundOrder refundOrder) {
		String outTradeNo = refundOrder.getOutTradeNo();
		String outRequestNo = refundOrder.getOutRefundNo();
		Assert.isTrue(StringUtils.isNotBlank(outTradeNo), "outTradeNo不能为空");
		Assert.isTrue(StringUtils.isNotBlank(outTradeNo), "outRequestNo不能为空");
		//查询
		QueryRefundOrderResult result = aliPayService.queryRefundOrderByTradeNo(outTradeNo, outRequestNo);
		if(result != null) {
			refundOrder.setRefundTime(result.getGmtRefundPay());
			refundOrder.setRefundState(2);
			refundOrder.setRefundDesc("退款成功");
			refundOrder.setUpdateDate(new Date());
			refundOrderMapper.updateByPrimaryKeySelective(refundOrder);
			//异步回调通知, 最好用mq
			if(StringUtils.isNotBlank(refundOrder.getNotifyUrl())) {
				try {
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("orderNo", refundOrder.getOrderNo());
					paramMap.put("refundTime", result.getGmtRefundPay());
					ArrayList<String> list = taskExecutorService.submit(new NotifyCallable(refundOrder.getNotifyUrl(), paramMap, httpUtil), 1);
					if(!CollectionUtils.isEmpty(list)) {
						String successFlag = list.get(0);
						if("SUCCESS".equals(successFlag)) {
							refundOrder.setNofityCount(refundOrder.getNofityCount() + 1);
							refundOrder.setNofityState(1);
							refundOrderMapper.updateByPrimaryKeySelective(refundOrder);
						}
					}
				} catch (Exception e) {
					logger.error("支付宝退款成功，通知出现异常【不影响主流程，故抓住】", e);
				}
			}
		}
	}

	
	@Override
	public void refundCallback(String data) {
		
	}
	
	
}
