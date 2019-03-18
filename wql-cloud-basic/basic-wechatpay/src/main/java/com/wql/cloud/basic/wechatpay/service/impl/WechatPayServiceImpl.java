package com.wql.cloud.basic.wechatpay.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wql.cloud.basic.wechatpay.config.WechatPayConfig;
import com.wql.cloud.basic.wechatpay.model.PlaceOrderModel;
import com.wql.cloud.basic.wechatpay.result.PayNotifyResult;
import com.wql.cloud.basic.wechatpay.result.PlaceOrderResult;
import com.wql.cloud.basic.wechatpay.result.QueryOrderResult;
import com.wql.cloud.basic.wechatpay.service.WechatPayService;
import com.wql.cloud.basic.wechatpay.util.DateUtil;
import com.wql.cloud.basic.wechatpay.util.HttpUtil;
import com.wql.cloud.basic.wechatpay.util.MapUtil;
import com.wql.cloud.basic.wechatpay.util.SignUtil;
import com.wql.cloud.basic.wechatpay.util.StreamUtils;
import com.wql.cloud.basic.wechatpay.util.UUIDUtil;
import com.wql.cloud.basic.wechatpay.util.WXPayConstant;
import com.wql.cloud.basic.wechatpay.util.WXPayUtil;
import com.wql.cloud.basic.wechatpay.util.XmlUtil;

@Service
public class WechatPayServiceImpl implements WechatPayService {
	
	private static final Logger logger = LoggerFactory.getLogger(WechatPayServiceImpl.class);
	
	public static final String SUCCESS = "SUCCESS";
	public static final String NOTPAY = "NOTPAY";
	public static final String CLOSED = "CLOSED";
	public static final String USERPAYING = "USERPAYING";
	public static final String PAYERROR = "PAYERROR";
	
	@Autowired
	private WechatPayConfig wechatPayConfig;
	
	@Override
	public PlaceOrderResult placeOrder(PlaceOrderModel model) {
		try {
			/**1.组装参数*/
			TreeMap<String, String> bizParams = new TreeMap<String, String>();
			bizParams.put(WXPayConstant.APPID, wechatPayConfig.getAppId());
		    bizParams.put(WXPayConstant.MCH_ID, wechatPayConfig.getMchId());
		    bizParams.put(WXPayConstant.TOTAL_FEE, String.valueOf(model.getTotalFee().multiply(new BigDecimal(100)))); // 整数，单位为分
		    bizParams.put(WXPayConstant.TRADE_TYPE, model.getTradeTypeEnum().getTradeType());
			bizParams.put(WXPayConstant.SPBILL_CREATE_IP, model.getCreateIp());
			bizParams.put(WXPayConstant.BODY, model.getBody());
			bizParams.put(WXPayConstant.OUT_TRADE_NO, model.getOutTradeNo());
			bizParams.put(WXPayConstant.NONCE_STR, UUIDUtil.getShortUuid()); 
			bizParams.put(WXPayConstant.FEE_TYPE, "CNY");
			bizParams.put(WXPayConstant.DEVICE_INFO, "WEB");
			bizParams.put(WXPayConstant.NOTIFY_URL, wechatPayConfig.getPayNotifyUrl());
			//TODO 失效时间, 默认30分钟
			Date now = new Date();
			bizParams.put(WXPayConstant.TIME_START, DateUtil.transferDateToString(now, DateUtil.DATE_FORMAT_2));
			bizParams.put(WXPayConstant.TIME_EXPIRE, DateUtil.getDateStr(now, Calendar.MINUTE, WXPayConstant.TIME_OUT, DateUtil.DATE_FORMAT_2));
			bizParams.put(WXPayConstant.SIGN, SignUtil.sign(bizParams, wechatPayConfig.getPrivateKey()));  
			
			/**2.发起请求*/
			String reqXml = XmlUtil.mapToXml(bizParams, WXPayConstant.XML_ROOT);
			String respXml = HttpUtil.doPost(wechatPayConfig.PLACE_ORDER_URL, reqXml, true);
			Map<String, String> respMap = MapUtil.objMap2StrMap(XmlUtil.xml2map(respXml));
			if(!WXPayConstant.SUCCESS_CODE.equals(respMap.get(WXPayConstant.RETURN_CODE))){
				return new PlaceOrderResult(false, respMap.get(WXPayConstant.RETURN_MSG));
			}
			if (!SignUtil.checkSign(respMap, wechatPayConfig.getPrivateKey())) {
				return new PlaceOrderResult(false, "签名校验不通过");
			}
			
			/**3.下单成功*/
			Map<String, String> returnMap = new HashMap<String, String>();
			returnMap.put(WXPayConstant.APPID, wechatPayConfig.getAppId());
			returnMap.put(WXPayConstant.PARTNERID, wechatPayConfig.getMchId());
			returnMap.put(WXPayConstant.PREPAYID, respMap.get("prepay_id"));
			returnMap.put(WXPayConstant.PACKAGE_KEY, "Sign=WXPay");
			returnMap.put(WXPayConstant.noncestr, UUIDUtil.getShortUuid());
			returnMap.put(WXPayConstant.timestamp, DateUtil.getTimeMillis(10));
			returnMap.put(WXPayConstant.SIGN, SignUtil.sign(returnMap, wechatPayConfig.getPrivateKey()));
			return new PlaceOrderResult(true, "微信下单成功", returnMap);
		} catch (Exception e) {
			logger.error("微信支付-下单，出现异常", e);
		}
		return new PlaceOrderResult(false, "微信下单异常");
	}
	
	
	@Override
	public QueryOrderResult queryOrderByTradeNo(String outTradeNo) {
  		try {
  			/**1.组装参数*/
  			HashMap<String, String> bizParams = new HashMap<String, String>();
  	        bizParams.put(WXPayConstant.APPID, wechatPayConfig.getAppId());
  	        bizParams.put(WXPayConstant.MCH_ID, wechatPayConfig.getMchId());
  	        bizParams.put(WXPayConstant.OUT_TRADE_NO, outTradeNo);
  	        bizParams.put(WXPayConstant.NONCE_STR, UUIDUtil.getShortUuid());
  			bizParams.put(WXPayConstant.SIGN, SignUtil.sign(bizParams, wechatPayConfig.getPrivateKey()));
  			
  			/**2.发起请求，微信支付查询*/
  			String reqXml = XmlUtil.mapToXml(bizParams, WXPayConstant.XML_ROOT);
  			String respXml = HttpUtil.doPost(WechatPayConfig.QUERY_ORDER_URL, reqXml, true);
  			Map<String, String> respMap = MapUtil.objMap2StrMap(XmlUtil.xml2map(respXml));
  			if(!WXPayConstant.SUCCESS_CODE.equals(respMap.get(WXPayConstant.RETURN_CODE))){
  	  			return new QueryOrderResult("查询微信支付订单结果失败", "fail");
  			}
  			
  			/**3.查询需要返回的字段*/
			if (SUCCESS.equals(respMap.get("return_code"))) {
				String tradeState = respMap.get("trade_state");
			    switch (tradeState) {
			        case SUCCESS:
			        	Date payDate = DateUtil.parseDate(respMap.get("time_end"), "yyyyMMddHHmmss");
			        	return new QueryOrderResult("支付成功", tradeState, payDate);
			        case NOTPAY:
			        	return new QueryOrderResult("未支付", tradeState);
			        case CLOSED:
			        	return new QueryOrderResult("已关闭", tradeState);
			        case USERPAYING:
			        	return new QueryOrderResult("支付中", tradeState);
			        case PAYERROR:
			        	return new QueryOrderResult("支付失败", tradeState);
			        default:
			            break;
			    }
			}
  		} catch (Exception e) {
  			logger.error("调用微信支付结果接口异常", e);
  		}
  		return new QueryOrderResult("未知", "unknow");
	}
	
	
	@Override
	public PayNotifyResult paySuccessNotify(String xmlStr) {
		PayNotifyResult payNotifyResult = new PayNotifyResult();
		try {
			boolean signatureValid = WXPayUtil.isSignatureValid(xmlStr, wechatPayConfig.getPrivateKey());
			if(!signatureValid) {
				logger.warn("微信支付回调，验签失败");
				return payNotifyResult;
			}
			//解析返回内容
			Map<String, Object> xml2map = XmlUtil.xml2map(xmlStr);
        	String returnCode = String.valueOf(xml2map.get("return_code"));
        	String resultCode = String.valueOf(xml2map.get("result_code"));
        	if(!SUCCESS.equals(returnCode) || !SUCCESS.equals(resultCode)) {
        		logger.warn("微信app支付，支付返回失败");
        		return payNotifyResult;
        	}
        	//支付成功，组装返回对象
        	payNotifyResult.setOutTradeNo(String.valueOf(xml2map.get("out_trade_no")));
        	payNotifyResult.setPayedTime(DateUtils.parseDate(String.valueOf(xml2map.get("time_end")), "yyyyMMddHHmmss"));
		} catch (Exception e) {
			logger.error("微信支付，回调校验异常", e);
		}
		return payNotifyResult;
	}

	/**
     * 从request域中读取出支付宝回调的内容
     * 
     * @param request
     * @return
     */
    public static String convertRequestParamsToXmlStr(HttpServletRequest request) {
    	String xmlStr = null;
		try {
			xmlStr = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        return xmlStr;
    }
	
	
}
