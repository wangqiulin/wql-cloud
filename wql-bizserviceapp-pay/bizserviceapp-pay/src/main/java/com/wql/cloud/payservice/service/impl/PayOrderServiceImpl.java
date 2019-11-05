package com.wql.cloud.payservice.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.wql.cloud.payservice.biz.payroute.PayRouteFactory;
import com.wql.cloud.payservice.biz.payroute.PayRouteFactory.CreatePayReq;
import com.wql.cloud.payservice.mapper.AppPaymentMapper;
import com.wql.cloud.payservice.mapper.PayOrderMapper;
import com.wql.cloud.payservice.pojo.domain.AppPayment;
import com.wql.cloud.payservice.pojo.domain.PayOrder;
import com.wql.cloud.payservice.pojo.req.CreatePayOrderReq;
import com.wql.cloud.payservice.pojo.req.QueryPayOrderReq;
import com.wql.cloud.payservice.pojo.res.CreatePayOrderRes;
import com.wql.cloud.payservice.pojo.res.QueryPayOrderRes;
import com.wql.cloud.payservice.service.PayOrderService;
import com.wql.cloud.tool.bean.BeanUtils;
import com.wql.cloud.tool.string.StringUtils;

@Service
public class PayOrderServiceImpl implements PayOrderService {
	
	@Autowired
	private List<PayRouteFactory> payRouteFactoryList;
	@Autowired
	private AppPaymentMapper appPaymentMapper;
	@Autowired
	private PayOrderMapper payOrderMapper;
	
	@Override
	@Transactional
	public CreatePayOrderRes createPayOrder(CreatePayOrderReq createPayOrderReq) {
//		Assert.notNull(createPayOrderReq, "请求对象不能为空");
//		Assert.isTrue(StringUtils.isNotBlank(createPayOrderReq.getAppId()), "appId不能为空");
//		Assert.isTrue(StringUtils.isNotBlank(createPayOrderReq.getUserCode()), "userCode不能为空");
//		Assert.isTrue(StringUtils.isNotBlank(createPayOrderReq.getOrderNo()), "orderNo不能为空");
//		Assert.isTrue(StringUtils.isNotBlank(createPayOrderReq.getPaymentWay()), "paymentWay不能为空");
//		Assert.notNull(createPayOrderReq.getPayAmount(), "payAmount不能为空");
//		Assert.isTrue(createPayOrderReq.getPayAmount().compareTo(BigDecimal.ZERO) == 1, "payAmount不能小于0");
//		//查询支付渠道
//		AppPayment appPayment = new AppPayment();
//		appPayment.setAppId(createPayOrderReq.getAppId());
//		appPayment.setPaymentWay(createPayOrderReq.getPaymentWay());
//		appPayment.setState(1);
//		appPayment = appPaymentMapper.selectOne(appPayment);
//		Assert.notNull(appPayment, "支付方式已更新，请重新尝试");
//		//支付下单
//		String outTradeNo = ""; //TODO
//		String channelWay = appPayment.getChannelWay();
//		PayRouteFactory payRouteFactory = payRouteFactoryList.stream().filter(o -> o.getChannelRoute().equals(channelWay))
//				.findFirst().orElseThrow(() -> new IllegalArgumentException("支付方式已更新，请重新尝试"));
//		CreatePayReq createPayReq = new CreatePayReq();
//		createPayReq.setOutTradeNo(outTradeNo);
//		createPayReq.setPayAmount(createPayOrderReq.getPayAmount());
//		createPayReq.setCreateIp(createPayOrderReq.getCreateIp());
//		createPayReq.setGoodsDesc(createPayOrderReq.getGoodsDesc());
//		createPayReq.setReturnUrl(createPayOrderReq.getReturnUrl());
//		String data = payRouteFactory.createPayOrder(createPayReq);
		//创建订单
		PayOrder order = new PayOrder();
		BeanUtils.copy(createPayOrderReq, order);
//		order.setOutTradeNo(outTradeNo);
		order.setPayState(1);
		order.setPayDesc("待支付");
		order.setNotifyState(0);
		order.setNotifyCount(0);
		order.setDataFlag(1);
		order.setId(null);
		payOrderMapper.insertSelective(order);
		//响应对象
		CreatePayOrderRes res = new CreatePayOrderRes();
		res.setOrderNo(createPayOrderReq.getOrderNo());
//		res.setPayData(data);
		return res;
	}
	
	
	@Override
	public QueryPayOrderRes queryPayOrder(QueryPayOrderReq req) {
		String appId = req.getAppId();
		String orderNo = req.getOrderNo();
		Assert.isTrue(StringUtils.isNotBlank(appId), "appId不能为空");
		Assert.isTrue(StringUtils.isNotBlank(orderNo), "orderNo不能为空");
		//响应对象
		QueryPayOrderRes res = new QueryPayOrderRes();
		//查询订单结果
		PayOrder payOrder = new PayOrder();
		payOrder.setAppId(appId);
		payOrder.setOrderNo(orderNo);
		payOrder = payOrderMapper.selectOne(payOrder);
		Assert.notNull(payOrder, "订单不存在");
		//TODO
		//支付结果查询
		String channelWay = payOrder.getChannelWay();
		PayRouteFactory payRouteFactory = payRouteFactoryList.stream().filter(o -> o.getChannelRoute().equals(channelWay))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("支付方式不存在"));
		payRouteFactory.queryPayOrder(payOrder);
		//TODO
		return res;
	}

	
	@Override
	public void payCallback(String channelWay, String data) {
		PayRouteFactory payRouteFactory = payRouteFactoryList.stream()
				.filter(o -> o.getChannelRoute().equals(channelWay))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("支付方式不存在"));
		payRouteFactory.payCallback(data);
	}
	
}
