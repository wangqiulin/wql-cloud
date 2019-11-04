package com.wql.cloud.payservice.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.wql.cloud.payservice.biz.payroute.PayRouteFactory;
import com.wql.cloud.payservice.mapper.PayOrderMapper;
import com.wql.cloud.payservice.mapper.RefundOrderMapper;
import com.wql.cloud.payservice.pojo.domain.PayOrder;
import com.wql.cloud.payservice.pojo.domain.RefundOrder;
import com.wql.cloud.payservice.pojo.req.CreateRefundOrderReq;
import com.wql.cloud.payservice.pojo.req.QueryRefundOrderReq;
import com.wql.cloud.payservice.pojo.res.CreateRefundOrderRes;
import com.wql.cloud.payservice.pojo.res.QueryRefundOrderRes;
import com.wql.cloud.payservice.service.RefundOrderService;
import com.wql.cloud.tool.string.StringUtils;

@Service
public class RefundOrderServiceImpl implements RefundOrderService {
	
	@Autowired
	private List<PayRouteFactory> payRouteFactoryList;
	@Autowired
	private PayOrderMapper payOrderMapper;
	@Autowired
	private RefundOrderMapper refundOrderMapper;
	
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
		PayRouteFactory payRouteFactory = payRouteFactoryList.stream().filter(o -> o.getChannelRoute().equals(channelWay)).findFirst().orElseGet(null);
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
		PayRouteFactory payRouteFactory = payRouteFactoryList.stream().filter(o -> o.getChannelRoute().equals(channelWay))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("支付方式不存在"));
		payRouteFactory.queryRefundOrder(refundOrder);
		//TODO
		return res;
	}

	
	@Override
	public void refundCallback(String channelWay, String data) {
		PayRouteFactory payRouteFactory = payRouteFactoryList.stream()
				.filter(o -> o.getChannelRoute().equals(channelWay))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("支付方式不存在"));
		payRouteFactory.refundCallback(data);
	}

}
