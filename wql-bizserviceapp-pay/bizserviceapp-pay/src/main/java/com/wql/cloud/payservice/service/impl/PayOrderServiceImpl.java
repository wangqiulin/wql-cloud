package com.wql.cloud.payservice.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.wql.cloud.payservice.mapper.AppPaymentMapper;
import com.wql.cloud.payservice.mapper.PayOrderMapper;
import com.wql.cloud.payservice.mapper.RefundOrderMapper;
import com.wql.cloud.payservice.pojo.domain.AppPayment;
import com.wql.cloud.payservice.pojo.domain.PayOrder;
import com.wql.cloud.payservice.pojo.domain.RefundOrder;
import com.wql.cloud.payservice.pojo.req.CreatePayOrderReq;
import com.wql.cloud.payservice.pojo.req.CreateRefundOrderReq;
import com.wql.cloud.payservice.pojo.req.QueryPayOrderReq;
import com.wql.cloud.payservice.pojo.req.QueryRefundOrderReq;
import com.wql.cloud.payservice.pojo.res.CreatePayOrderRes;
import com.wql.cloud.payservice.pojo.res.CreateRefundOrderRes;
import com.wql.cloud.payservice.pojo.res.QueryPayOrderRes;
import com.wql.cloud.payservice.pojo.res.QueryRefundOrderRes;
import com.wql.cloud.payservice.service.PayOrderService;
import com.wql.cloud.payservice.service.payroute.CreatePayReq;
import com.wql.cloud.payservice.service.payroute.PayRouteFactory;
import com.wql.cloud.tool.bean.BeanUtils;
import com.wql.cloud.tool.string.StringUtils;

@Service
public class PayOrderServiceImpl implements PayOrderService {
	
	@Autowired
	private AppPaymentMapper appPaymentMapper;
	@Autowired
	private PayOrderMapper payOrderMapper;
	@Autowired
	private RefundOrderMapper refundOrderMapper;
	@Autowired
	private List<PayRouteFactory> payRouteFactoryList;
	
	@Override
	@Transactional
	public CreatePayOrderRes createPayOrder(CreatePayOrderReq createPayOrderReq) {
		Assert.notNull(createPayOrderReq, "请求对象不能为空");
		Assert.isTrue(StringUtils.isNotBlank(createPayOrderReq.getAppId()), "appId不能为空");
		Assert.isTrue(StringUtils.isNotBlank(createPayOrderReq.getUserCode()), "userCode不能为空");
		Assert.isTrue(StringUtils.isNotBlank(createPayOrderReq.getOrderNo()), "orderNo不能为空");
		Assert.isTrue(StringUtils.isNotBlank(createPayOrderReq.getPaymentWay()), "paymentWay不能为空");
		Assert.notNull(createPayOrderReq.getPayAmount(), "payAmount不能为空");
		Assert.isTrue(createPayOrderReq.getPayAmount().compareTo(BigDecimal.ZERO) == 1, "payAmount不能小于0");
		//查询支付渠道
		AppPayment appPayment = new AppPayment();
		appPayment.setAppId(createPayOrderReq.getAppId());
		appPayment.setPaymentWay(createPayOrderReq.getPaymentWay());
		appPayment.setState(1);
		appPayment = appPaymentMapper.selectOne(appPayment);
		Assert.notNull(appPayment, "支付方式已更新，请返回后重新尝试");
		//支付下单
		String outTradeNo = ""; //TODO
		String channelWay = appPayment.getChannelWay();
		PayRouteFactory payRouteFactory = payRouteFactoryList.stream().filter(o -> o.getChannel().equals(channelWay))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("支付方式已更新，请返回后重新尝试"));
		CreatePayReq createPayReq = new CreatePayReq();
		createPayReq.setOutTradeNo(outTradeNo);
		createPayReq.setPayAmount(createPayOrderReq.getPayAmount());
		createPayReq.setCreateIp(createPayOrderReq.getCreateIp());
		createPayReq.setGoodsDesc(createPayOrderReq.getGoodsDesc());
		createPayReq.setReturnUrl(createPayOrderReq.getReturnUrl());
		String data = payRouteFactory.createPayOrder(createPayReq);
		//创建订单
		PayOrder order = new PayOrder();
		BeanUtils.copy(createPayOrderReq, order);
		order.setOutTradeNo(outTradeNo);
		order.setPayState(1);
		order.setPayDesc("待支付");
		order.setNofityState(0);
		order.setNofityCount(0);
		order.setDataFlag(1);
		order.setId(null);
		payOrderMapper.insertSelective(order);
		//响应对象
		CreatePayOrderRes res = new CreatePayOrderRes();
		res.setPayData(data);
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
		PayRouteFactory payRouteFactory = payRouteFactoryList.stream().filter(o -> o.getChannel().equals(channelWay))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("支付方式不存在"));
		payRouteFactory.queryPayOrder(payOrder);
		//TODO
		return res;
	}

	
	@Override
	public void payCallback(String channelWay, String data) {
		PayRouteFactory payRouteFactory = payRouteFactoryList.stream()
				.filter(o -> o.getChannel().equals(channelWay))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("支付方式不存在"));
		payRouteFactory.payCallback(data);
	}
	
	
	//================================退款==============================//
	

	@Override
	@Transactional
	public CreateRefundOrderRes createRefundOrder(CreateRefundOrderReq createRefundOrderReq) {
		Assert.notNull(createRefundOrderReq, "请求对象不能为空");
		Assert.isTrue(StringUtils.isNotBlank(createRefundOrderReq.getAppId()), "appId不能为空");
		Assert.isTrue(StringUtils.isNotBlank(createRefundOrderReq.getOrderNo()), "orderNo不能为空");
		//查询支付订单
		PayOrder payOrder = new PayOrder();
		payOrder.setAppId(createRefundOrderReq.getAppId());
		payOrder.setOrderNo(createRefundOrderReq.getOrderNo());
		payOrder = payOrderMapper.selectOne(payOrder);
		Assert.notNull(payOrder, "支付订单不存在");
		Assert.isTrue(payOrder.getPayState() == 2, "非支付成功订单，不可退款");
		BigDecimal refundAmount = createRefundOrderReq.getRefundAmount() == null ? payOrder.getPayAmount() : createRefundOrderReq.getRefundAmount();
		Assert.isTrue(refundAmount.compareTo(payOrder.getPayAmount()) <= 0, "退款金额不能大于支付金额");
		//需要先创建退款订单
		String outRefundNo = ""; //TODO
		RefundOrder order = new RefundOrder();
		order.setAppId(payOrder.getAppId());
		order.setUserCode(payOrder.getUserCode());
		order.setOrderNo(payOrder.getOrderNo());
		order.setOutRefundNo(outRefundNo);
		order.setOutTradeNo(payOrder.getOutTradeNo());
		order.setPaymentWay(payOrder.getPaymentWay());
		order.setChannelWay(payOrder.getChannelWay());
		order.setPayAmount(payOrder.getPayAmount());
		order.setRefundAmount(refundAmount);
		order.setRefundState(1);
		order.setRefundDesc("待退款");
		order.setNotifyUrl(createRefundOrderReq.getNotifyUrl());
		order.setNofityState(0);
		order.setNofityCount(0);
		order.setDataFlag(1);
		order.setId(null);
		refundOrderMapper.insertSelective(order);
		//再进行退款请求操作
		String channelWay = payOrder.getChannelWay();
		PayRouteFactory payRouteFactory = payRouteFactoryList.stream().filter(o -> o.getChannel().equals(channelWay)).findFirst().orElseGet(null);
		Assert.notNull(payRouteFactory, "支付方式已更新，请返回后重新尝试");
		payRouteFactory.createRefundOrder(outRefundNo, payOrder.getOutTradeNo(), payOrder.getPayAmount(), refundAmount);
		//响应对象
		CreateRefundOrderRes res = new CreateRefundOrderRes();
		return res;
	}


	@Override
	public QueryRefundOrderRes queryRefundOrder(QueryRefundOrderReq req) {
		String appId = req.getAppId();
		String orderNo = req.getOrderNo();
		Assert.isTrue(StringUtils.isNotBlank(appId), "appId不能为空");
		Assert.isTrue(StringUtils.isNotBlank(orderNo), "orderNo不能为空");
		//响应对象
		QueryRefundOrderRes res = new QueryRefundOrderRes();
		//查询退款订单
		RefundOrder refundOrder = new RefundOrder();
		refundOrder.setAppId(appId);
		refundOrder.setOrderNo(orderNo);
		refundOrder = refundOrderMapper.selectOne(refundOrder);
		Assert.notNull(refundOrder, "订单不存在");
		//TODO
		//支付结果查询
		String channelWay = refundOrder.getChannelWay();
		PayRouteFactory payRouteFactory = payRouteFactoryList.stream().filter(o -> o.getChannel().equals(channelWay))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("支付方式不存在"));
		payRouteFactory.queryRefundOrder(refundOrder);
		//TODO
		return res;
	}

	
	@Override
	public void refundCallback(String channelWay, String data) {
		PayRouteFactory payRouteFactory = payRouteFactoryList.stream()
				.filter(o -> o.getChannel().equals(channelWay))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("支付方式不存在"));
		payRouteFactory.refundCallback(data);
	}

}
