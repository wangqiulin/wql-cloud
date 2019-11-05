package com.wql.cloud.payservice.biz.payroute.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.wql.cloud.basic.wxpay.model.PlaceOrderModel;
import com.wql.cloud.basic.wxpay.model.RefundOrderModel;
import com.wql.cloud.basic.wxpay.result.PayNotifyResult;
import com.wql.cloud.basic.wxpay.result.PlaceOrderResult;
import com.wql.cloud.basic.wxpay.result.QueryOrderResult;
import com.wql.cloud.basic.wxpay.result.RefundNotifyResult;
import com.wql.cloud.basic.wxpay.service.WxPayService;
import com.wql.cloud.payservice.biz.payroute.PayRouteFactory;
import com.wql.cloud.payservice.mapper.PayOrderMapper;
import com.wql.cloud.payservice.mapper.RefundOrderMapper;
import com.wql.cloud.payservice.pojo.domain.PayOrder;
import com.wql.cloud.payservice.pojo.domain.RefundOrder;
import com.wql.cloud.tool.executor.TaskExecutorService;
import com.wql.cloud.tool.httpclient.HttpUtil;

@Service
public class WxH5PayServiceImpl implements PayRouteFactory {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());  
	
	@Autowired
	private WxPayService wxPayService;
	@Autowired
	private HttpUtil httpUtil;
	@Autowired
	private TaskExecutorService taskExecutorService;
	@Autowired
	private PayOrderMapper payOrderMapper;
	@Autowired
	private RefundOrderMapper refundOrderMapper;
	
	@Override
	public String getChannelRoute() {
		return H5_WXPAY;
	}

	@Override
	public String createPayOrder(CreatePayReq createPayReq) {
		Assert.isTrue(StringUtils.isNotBlank(createPayReq.getCreateIp()), "createIp不能为空");
		Assert.isTrue(StringUtils.isNotBlank(createPayReq.getOutTradeNo()), "outTradeNo不能为空");
		Assert.notNull(createPayReq.getPayAmount(), "payAmount不能为空");
		Assert.isTrue(createPayReq.getPayAmount().compareTo(BigDecimal.ZERO) == 1, "payAmount不能小于0");
		//支付下单
		PlaceOrderModel placeOrderModel = new PlaceOrderModel();
		placeOrderModel.setOutTradeNo(createPayReq.getOutTradeNo());
		placeOrderModel.setTotalFee(createPayReq.getPayAmount());
		placeOrderModel.setCreateIp(createPayReq.getCreateIp());
		placeOrderModel.setTimeStart(new Date());
		placeOrderModel.setTimeExpire(DateUtils.addMinutes(placeOrderModel.getTimeStart(), 30));
		placeOrderModel.setBody(StringUtils.isBlank(createPayReq.getGoodsDesc()) ? "购买" : createPayReq.getGoodsDesc());
		PlaceOrderResult data = wxPayService.placeOrderForH5(placeOrderModel);
		Assert.isTrue(data.getResultCode(), "微信支付下单失败");
		return data.getMwebUrl();
	}

	
	@Override
	public void queryPayOrder(PayOrder payOrder) {
		String outTradeNo = payOrder.getOutTradeNo();
		Assert.isTrue(StringUtils.isNotBlank(outTradeNo), "outTradeNo不能为空");
		QueryOrderResult result = wxPayService.queryOrderByTradeNo(outTradeNo);
		switch (result.getTradeState()) {
			case "SUCCESS":
				payOrder.setPayState(2);
				payOrder.setPayedTime(result.getPayedTime());
				payOrder.setPayDesc("支付成功");
				payOrder.setUpdateDate(new Date());
				payOrderMapper.updateByPrimaryKeySelective(payOrder);
				//异步回调通知
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
						logger.error("微信支付成功，通知出现异常【不影响主流程，故抓住】", e);
					}
				}
				break;
			case "NOTPAY":
				payOrder.setPayState(3);
				payOrder.setPayedTime(result.getPayedTime());
				payOrder.setPayDesc("未支付");
				payOrderMapper.updateByPrimaryKeySelective(payOrder);
				break;
			case "CLOSED":
				payOrder.setPayState(3);
				payOrder.setPayedTime(result.getPayedTime());
				payOrder.setPayDesc("支付关闭");
				payOrderMapper.updateByPrimaryKeySelective(payOrder);
				break;
			case "PAYERROR":
				payOrder.setPayState(3);
				payOrder.setPayedTime(result.getPayedTime());
				payOrder.setPayDesc("支付失败");
				payOrderMapper.updateByPrimaryKeySelective(payOrder);
				break;
			case "UNKONW":
			case "USERPAYING":
				break;
		default:
			break;
		}
	}
	
	
	@Override
	public void payCallback(String data) {
		PayNotifyResult result = wxPayService.paySuccessNotify(data);
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
	public void createRefundOrder(String outRefundNo, String outTradeNo, BigDecimal payAmount, BigDecimal refundAmount) {
		Assert.isTrue(StringUtils.isNotBlank(outTradeNo), "outTradeNo不能为空");
		Assert.isTrue(StringUtils.isNotBlank(outRefundNo), "outRefundNo不能为空");
		Assert.notNull(payAmount, "支付金额不能为空");
		Assert.notNull(refundAmount, "退款金额不能为空");
		Assert.isTrue(refundAmount.compareTo(BigDecimal.ZERO) == 1, "退款金额不能小于0");
		Assert.isTrue(refundAmount.compareTo(payAmount) <= 0, "退款金额不能大于支付金额");
		//退款下单
		RefundOrderModel refundOrderModel = new RefundOrderModel();
		refundOrderModel.setOutRefundNo(outRefundNo);
		refundOrderModel.setOutTradeNo(outTradeNo);
		refundOrderModel.setTotalFee(payAmount);
		refundOrderModel.setRefundFee(refundAmount);
		String refundOrder = wxPayService.refundOrder(refundOrderModel);
		Assert.isTrue(StringUtils.isNotBlank(refundOrder), "微信退款请求失败");
	}
	
	
	@Override
	public void queryRefundOrder(RefundOrder refundOrder) {
		String outTradeNo = refundOrder.getOutTradeNo();
		Assert.isTrue(StringUtils.isNotBlank(outTradeNo), "outTradeNo不能为空");
		//查询退款结果
		QueryOrderResult result = wxPayService.queryRefundOrderByTradeNo(outTradeNo);
		switch (result.getTradeState()) {
			case "SUCCESS":
				//退款成功
				refundOrder.setRefundState(2);
				refundOrder.setRefundDesc("退款成功");
				refundOrder.setRefundTime(result.getPayedTime());
				refundOrder.setUpdateDate(new Date());
				refundOrderMapper.updateByPrimaryKeySelective(refundOrder);
				//异步回调通知
				if(StringUtils.isNotBlank(refundOrder.getNotifyUrl())) {
					try {
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("orderNo", refundOrder.getOrderNo());
						paramMap.put("refundTime", result.getPayedTime());
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
						logger.error("微信退款成功，通知出现异常【不影响主流程，故抓住】", e);
					}
				}
				break;
			case "FAIL":
				refundOrder.setRefundState(3);
				refundOrder.setRefundDesc("退款失败");
				refundOrder.setUpdateDate(new Date());
				refundOrderMapper.updateByPrimaryKeySelective(refundOrder);
				break;
			case "REFUNDCLOSE":
				refundOrder.setRefundState(3);
				refundOrder.setRefundDesc("退款关闭,不可退款");
				refundOrder.setUpdateDate(new Date());
				refundOrderMapper.updateByPrimaryKeySelective(refundOrder);
				break;
			case "CHANGE":
				refundOrder.setRefundState(3);
				refundOrder.setRefundDesc("退款异常");
				refundOrder.setUpdateDate(new Date());
				refundOrderMapper.updateByPrimaryKeySelective(refundOrder);
				break;
			case "PROCESSING":
			case "UNKNOW":
				break;
			default:
				break;
		}
	}
	
	
	@Override
	public void refundCallback(String data) {
		RefundNotifyResult result = wxPayService.refundNotify(data);
		if(result == null) {
			return ;
		}
		//退款验签成功后，处理业务逻辑
		RefundOrder refundOrder = new RefundOrder();
		refundOrder.setOutTradeNo(result.getOutTradeNo());
		refundOrder = refundOrderMapper.selectOne(refundOrder);
		if(refundOrder != null) {
			this.queryRefundOrder(refundOrder);
		}
	}
	
	
}
