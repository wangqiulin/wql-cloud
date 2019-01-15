package com.wql.cloud.userservice.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.basic.response.constant.DataResponse;
import com.wql.cloud.userservice.service.WechatAppPayService;
import com.wql.cloud.userservice.util.WXPayConstont;
import com.wql.cloud.userservice.util.XmlUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "微信原生app支付")
@RestController
public class WechatAppPayController {
	
	private static final Logger logger = LoggerFactory.getLogger(WechatAppPayController.class);

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private WechatAppPayService wechatAppPayService;
	
	@ApiOperation(value = "微信支付下单")
	@PostMapping("/wechatPay/createOrder")
	public DataResponse createOrder() {
		wechatAppPayService.unifiedOrder();
		return null;
	}
	
	
	@ApiOperation(value = "微信支付成功后的回调通知地址")
	@PostMapping("/wechatPay/payResultNotify")
	public void payResultNotify() {
		HashMap<String, String> respMap = new HashMap<String, String>();
		try {
			String reqBody = getXmlReqBody(request);
			logger.info("微信回调原始报文：" + reqBody);
			//业务逻辑处理
			wechatAppPayService.updatePayResultNotify(reqBody);
			respMap.put("return_code", WXPayConstont.SUCCESS_CODE);
			respMap.put("return_msg", "OK");
		} catch (Exception e) {
			logger.error("PayResultNotifyController.payResultNotify-异常", e);
			respMap.put("return_code", WXPayConstont.FAIL_CODE);
			respMap.put("return_msg", "参数格式校验错误");
		}
		String reqXml = XmlUtil.mapToXml(respMap, WXPayConstont.XML_ROOT);
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(reqXml);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("响应异常:", e);
		}
	}
	
	public String getXmlReqBody(HttpServletRequest request) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			logger.error("getXmlReqBody-获取请求报文错误！",e);
			throw e;
		}
		return sb.toString();
	}
	
	
}
