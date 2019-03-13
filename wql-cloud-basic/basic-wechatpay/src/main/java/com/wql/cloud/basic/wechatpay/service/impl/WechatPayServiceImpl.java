package com.wql.cloud.basic.wechatpay.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wql.cloud.basic.wechatpay.config.WechatPayConfig;
import com.wql.cloud.basic.wechatpay.model.PlaceOrderModel;
import com.wql.cloud.basic.wechatpay.model.QueryOrderModel;
import com.wql.cloud.basic.wechatpay.result.PlaceOrderResult;
import com.wql.cloud.basic.wechatpay.result.QueryOrderResult;
import com.wql.cloud.basic.wechatpay.service.WechatPayService;
import com.wql.cloud.basic.wechatpay.util.DateUtil;
import com.wql.cloud.basic.wechatpay.util.HttpUtil;
import com.wql.cloud.basic.wechatpay.util.MapUtil;
import com.wql.cloud.basic.wechatpay.util.SignUtil;
import com.wql.cloud.basic.wechatpay.util.UUIDUtil;
import com.wql.cloud.basic.wechatpay.util.WXPayConstont;
import com.wql.cloud.basic.wechatpay.util.XmlUtil;

@Service
public class WechatPayServiceImpl implements WechatPayService {
	
	private static final Logger logger = LoggerFactory.getLogger(WechatPayServiceImpl.class);
	
	@Autowired(required=false)
	private WechatPayConfig wechatPayConfig;
	
	@Override
	public PlaceOrderResult placeOrder(PlaceOrderModel model) {
		try {
			/**1.组装参数*/
			TreeMap<String, String> bizParams = new TreeMap<String, String>();
			bizParams.put(WXPayConstont.APPID, wechatPayConfig.getAppId());
		    bizParams.put(WXPayConstont.MCH_ID, wechatPayConfig.getMchId());
		    bizParams.put(WXPayConstont.TOTAL_FEE, String.valueOf(model.getTotalFee().multiply(new BigDecimal(100)))); // 整数，单位为分
			bizParams.put(WXPayConstont.SPBILL_CREATE_IP, model.getCreateIp());
			bizParams.put(WXPayConstont.BODY, model.getBody());
			bizParams.put(WXPayConstont.OUT_TRADE_NO, model.getOutTradeNo());
			bizParams.put(WXPayConstont.NOTIFY_URL, wechatPayConfig.getPayNotifyUrl());
			bizParams.put(WXPayConstont.NONCE_STR, UUIDUtil.getShortUuid()); // 随机码
			bizParams.put(WXPayConstont.TRADE_TYPE, "APP");	// 非必输，暂写死
			bizParams.put(WXPayConstont.FEE_TYPE, "CNY");	// 非必输，暂写死
			bizParams.put(WXPayConstont.DEVICE_INFO, "WEB");	// 非必输，暂写死
			//TODO 失效时间, 默认30分钟
			Date now = new Date();
			bizParams.put(WXPayConstont.TIME_START, DateUtil.transferDateToString(now, DateUtil.DATE_FORMAT_2));
			bizParams.put(WXPayConstont.TIME_EXPIRE, DateUtil.getDateStr(now, Calendar.MINUTE, WXPayConstont.TIME_OUT, DateUtil.DATE_FORMAT_2));
			//签名
			bizParams.put(WXPayConstont.SIGN, SignUtil.sign(bizParams, wechatPayConfig.getPrivateKey()));  
			
			/**2.发起请求*/
			String reqXml = XmlUtil.mapToXml(bizParams, WXPayConstont.XML_ROOT);
			String respXml = HttpUtil.doPost(wechatPayConfig.getPlaceOrderServerUrl(), reqXml, true);
			Map<String, String> respMap = MapUtil.objMap2StrMap(XmlUtil.xml2map(respXml));
			if(!WXPayConstont.SUCCESS_CODE.equals(respMap.get(WXPayConstont.RETURN_CODE))){
				return new PlaceOrderResult(false, respMap.get(WXPayConstont.RETURN_MSG));
			}
			if (!SignUtil.checkSign(respMap, wechatPayConfig.getPrivateKey())) {
				return new PlaceOrderResult(false, "签名校验不通过");
			}
			
			/**3.下单成功*/
			Map<String, String> returnMap = new HashMap<String, String>();
			returnMap.put(WXPayConstont.APPID, wechatPayConfig.getAppId());
			returnMap.put(WXPayConstont.PARTNERID, wechatPayConfig.getMchId());
			returnMap.put(WXPayConstont.PREPAYID, respMap.get("prepay_id"));
			returnMap.put(WXPayConstont.PACKAGE_KEY, "Sign=WXPay");
			returnMap.put(WXPayConstont.noncestr, UUIDUtil.getShortUuid());
			returnMap.put(WXPayConstont.timestamp, DateUtil.getTimeMillis(10));
			returnMap.put(WXPayConstont.SIGN, SignUtil.sign(returnMap, wechatPayConfig.getPrivateKey()));
			return new PlaceOrderResult(true, "微信下单成功", returnMap);
		} catch (Exception e) {
			logger.error("微信支付-下单，出现异常", e);
		}
		return new PlaceOrderResult(false, "微信下单异常");
	}
	
	
	@Override
	public QueryOrderResult queryOrder(QueryOrderModel model) {
  		try {
  			/**1.组装参数*/
  			HashMap<String, String> bizParams = new HashMap<String, String>();
  	        bizParams.put(WXPayConstont.APPID, wechatPayConfig.getAppId());
  	        bizParams.put(WXPayConstont.MCH_ID, wechatPayConfig.getMchId());
  	        bizParams.put(WXPayConstont.OUT_TRADE_NO, model.getOutTradeNo());
  	        bizParams.put(WXPayConstont.NONCE_STR, UUIDUtil.getShortUuid());// 随机码
  			bizParams.put(WXPayConstont.SIGN, SignUtil.sign(bizParams, wechatPayConfig.getPrivateKey()));  //签名
  			
  			/**2.发起请求，微信支付查询*/
  			String reqXml = XmlUtil.mapToXml(bizParams, WXPayConstont.XML_ROOT);
  			String respXml = HttpUtil.doPost(wechatPayConfig.getQueryOrderServerUrl(), reqXml, true);
  			Map<String, String> respMap = MapUtil.objMap2StrMap(XmlUtil.xml2map(respXml));
  			if(!WXPayConstont.SUCCESS_CODE.equals(respMap.get(WXPayConstont.RETURN_CODE))){
  	  			return new QueryOrderResult(false, "查询微信支付订单结果失败");
  			}
  			
  			/**3.查询需要返回的字段*/
			String returnCode = respMap.get("return_code");
			if ("SUCCESS".equals(returnCode)) {
				String tradeState = respMap.get("trade_state");
			    switch (tradeState) {
			        case "SUCCESS":
			        	Date payDate = DateUtil.parseDate(respMap.get("time_end"), "yyyyMMddHHmmss");
			        	return new QueryOrderResult(true, "支付成功", tradeState, payDate);
			        case "NOTPAY":
			        	return new QueryOrderResult(true, "未支付", tradeState);
			        case "CLOSED":
			        	return new QueryOrderResult(true, "已关闭", tradeState);
			        case "USERPAYING":
			        	return new QueryOrderResult(true, "支付中", tradeState);
			        case "PAYERROR":
			        	return new QueryOrderResult(true, "支付失败", tradeState);
			        default:
			            break;
			    }
			}
  		} catch (Exception e) {
  			logger.error("调用微信支付结果接口异常", e);
  		}
  		return new QueryOrderResult(false, "查询微信支付订单结果异常");
	}
	

}
