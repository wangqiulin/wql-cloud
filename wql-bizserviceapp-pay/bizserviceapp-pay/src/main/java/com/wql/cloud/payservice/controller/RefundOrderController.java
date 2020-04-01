package com.wql.cloud.payservice.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.basic.datasource.response.DataResponse;
import com.wql.cloud.basic.wxpay.util.WXPayUtil;
import com.wql.cloud.payservice.pojo.req.CreateRefundOrderReq;
import com.wql.cloud.payservice.pojo.req.QueryRefundOrderReq;
import com.wql.cloud.payservice.pojo.res.CreateRefundOrderRes;
import com.wql.cloud.payservice.pojo.res.QueryRefundOrderRes;
import com.wql.cloud.payservice.service.RefundOrderService;

import io.swagger.annotations.ApiOperation;

@RestController
public class RefundOrderController {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());  

    @Autowired
    private RefundOrderService refundOrderService;
    @Autowired
	private HttpServletRequest request;
    @Autowired
	private HttpServletResponse response;

	@ApiOperation(value = "创建退款订单")
	@PostMapping("/pay/refundOrder/create")
	public DataResponse<CreateRefundOrderRes> createRefundOrder(@RequestBody CreateRefundOrderReq req) {
		return DataResponse.success(refundOrderService.createRefundOrder(req));
	}
	
	
	@ApiOperation(value = "查询退款订单结果")
	@PostMapping("/pay/refundOrder/query")
	public DataResponse<QueryRefundOrderRes> queryRefundOrder(@RequestBody QueryRefundOrderReq req) {
		return DataResponse.success(refundOrderService.queryRefundOrder(req));
	}
	
	
	@ApiOperation(value = "微信退款结果回调")
	@RequestMapping(value = "/pay/wxpay/refundCallback", method = RequestMethod.POST)
	public void wxpayRefundCallback() {
		logger.info("--------------微信退款,开始回调--------------");
		PrintWriter writer = null;
   		try {
   			writer = response.getWriter();
   			String xml = "";
   			try {
   				String inputLine = "";
                while((inputLine = request.getReader().readLine()) != null){
                	xml += inputLine;
                } 
                refundOrderService.refundCallback("app-wxpay", xml);
   			} finally {
                request.getReader().close();
			}
   			writer.print(WXPayUtil.successXml());
   			writer.flush();
   		} catch (Exception e) {
   			logger.error("微信退款结果回调,处理异常: ", e);
   		} finally {
   			if(writer != null) {
   				writer.close();
   			}
   		}
	}
	
}
