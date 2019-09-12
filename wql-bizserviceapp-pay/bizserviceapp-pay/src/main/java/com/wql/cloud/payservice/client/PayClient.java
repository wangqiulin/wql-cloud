package com.wql.cloud.payservice.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.payservice.pojo.req.CreatePayOrderReq;
import com.wql.cloud.payservice.pojo.req.CreateRefundOrderReq;
import com.wql.cloud.payservice.pojo.req.QueryPayOrderReq;
import com.wql.cloud.payservice.pojo.req.QueryRefundOrderReq;
import com.wql.cloud.payservice.pojo.res.CreatePayOrderRes;
import com.wql.cloud.payservice.pojo.res.CreateRefundOrderRes;
import com.wql.cloud.payservice.pojo.res.QueryPayOrderRes;
import com.wql.cloud.payservice.pojo.res.QueryRefundOrderRes;

import io.swagger.annotations.ApiOperation;

@FeignClient(value = "${feign.serviceId.pay}", fallback = PayClientHystrix.class)
public interface PayClient {

	@ApiOperation(value = "创建支付订单")
	@RequestMapping(value = "/pay/payOrder/create", method = RequestMethod.POST)
	DataResponse<CreatePayOrderRes> createPayOrder(@RequestBody CreatePayOrderReq req);

	@ApiOperation(value = "查询支付订单结果")
	@RequestMapping(value = "/pay/payOrder/query", method = RequestMethod.POST)
	DataResponse<QueryPayOrderRes> queryPayOrder(@RequestBody QueryPayOrderReq req);
	
	@ApiOperation(value = "创建退款订单")
	@RequestMapping(value = "/pay/refundOrder/create", method = RequestMethod.POST)
	DataResponse<CreateRefundOrderRes> createRefundOrder(@RequestBody CreateRefundOrderReq req);
	
	@ApiOperation(value = "查询退款订单结果")
	@RequestMapping(value = "/pay/refundOrder/query", method = RequestMethod.POST)
	DataResponse<QueryRefundOrderRes> queryRefundOrder(@RequestBody QueryRefundOrderReq req);
	
}
