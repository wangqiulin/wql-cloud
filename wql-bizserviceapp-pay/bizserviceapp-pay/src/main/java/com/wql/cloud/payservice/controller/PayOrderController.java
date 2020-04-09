package com.wql.cloud.payservice.controller;

import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.wql.cloud.basic.datasource.response.DataResponse;
import com.wql.cloud.basic.wxpay.util.WXPayUtil;
import com.wql.cloud.payservice.pojo.req.CreatePayOrderReq;
import com.wql.cloud.payservice.pojo.req.QueryPayOrderReq;
import com.wql.cloud.payservice.pojo.res.CreatePayOrderRes;
import com.wql.cloud.payservice.pojo.res.QueryPayOrderRes;
import com.wql.cloud.payservice.service.PayOrderService;

import io.swagger.annotations.ApiOperation;

@RestController
public class PayOrderController {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());  

    @Autowired
    private PayOrderService payOrderService;
    @Autowired
	private HttpServletRequest request;
    @Autowired
	private HttpServletResponse response;
    
    
	@ApiOperation(value = "创建支付订单")
	@PostMapping("/pay/payOrder/create")
	public DataResponse<CreatePayOrderRes> createPayOrder(@RequestBody CreatePayOrderReq req) {
		return DataResponse.success(payOrderService.createPayOrder(req));
	}
	
	
	@ApiOperation(value = "查询支付订单结果")
	@PostMapping("/pay/payOrder/query")
	public DataResponse<QueryPayOrderRes> queryPayOrder(@RequestBody QueryPayOrderReq req) {
		return DataResponse.success(payOrderService.queryPayOrder(req));
	}
	
	
	@ApiOperation(value = "微信支付结果回调")
	@RequestMapping(value = "/pay/wxpay/payCallback", method = RequestMethod.POST)
	public void wxpayCallback() {
		logger.info("--------------微信支付,支付结果开始回调--------------");
		PrintWriter writer = null;
   		try {
   			writer = response.getWriter();
   			String data = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
   			payOrderService.payCallback("app-wxpay", data);
   			writer.print(WXPayUtil.successXml());
   			writer.flush();
   		} catch (Exception e) {
   			logger.error("微信支付回调通知处理失败", e);
   		} finally {
   			if(writer != null) {
   				writer.close();
   			}
   		}
	}
	
	@ApiOperation(value = "支付宝支付结果回调")
	@RequestMapping(value = "/pay/alipay/payCallback", method = RequestMethod.POST)
	public void alipayCallback() {
		logger.info("--------------支付宝,支付结果开始回调--------------");
		PrintWriter writer = null;
   		try {
   			writer = response.getWriter();
   			Map<String, String> params = convertRequestParamsToMap(request);
   			payOrderService.payCallback("app-alipay", JSON.toJSONString(params));
   			writer.print("success");
   			writer.flush();
   		} catch (Exception e) {
   			logger.error("支付宝支付结果回调处理失败: ", e);
   		} finally {
   			if(writer != null) {
   				writer.close();
   			}
   		}
	}
	
	
	 // 将request中的参数转换成Map
    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
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
	
}
