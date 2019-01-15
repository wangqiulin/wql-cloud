package com.wql.cloud.userservice.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wql.cloud.basic.response.constant.BusinessEnum;
import com.wql.cloud.basic.response.constant.BusinessException;
import com.wql.cloud.userservice.service.IModelFactory;
import com.wql.cloud.userservice.service.WechatAppPayService;
import com.wql.cloud.userservice.util.DateUtil;
import com.wql.cloud.userservice.util.HttpUtil;
import com.wql.cloud.userservice.util.MapUtil;
import com.wql.cloud.userservice.util.SignUtil;
import com.wql.cloud.userservice.util.UUIDUtil;
import com.wql.cloud.userservice.util.WXPayConstont;
import com.wql.cloud.userservice.util.XmlUtil;

@Service
public class WechatAppPayServiceImpl implements WechatAppPayService {

	private static final Logger logger = LoggerFactory.getLogger(WechatAppPayServiceImpl.class);
	
	@Value("${wxpay.signKey}")
	private String signKey;
	
	@Value("${wxpay.unifiedOrderUrl}")
	private String unifiedOrderUrl;
	
	@Autowired
	private IModelFactory wxpayModelFactory;
	
	@Override
	public void unifiedOrder() {
		Map<String, String> returnMap = new HashMap<String, String>();
		
		//TODO 本地订单落库
		
		HashMap<String, String> bizParams = new HashMap<String, String>();
		bizParams.put(WXPayConstont.TOTAL_FEE, String.valueOf("支付金额，单位分"));// 整数，单位为分
		bizParams.put(WXPayConstont.SPBILL_CREATE_IP, "ip地址");// 用户ip从request中获取
		bizParams.put(WXPayConstont.BODY, "商品描述");
		TreeMap<String, String> placeOrderReq = wxpayModelFactory.buildPlaceOrderRequset(bizParams);
		Map<String, String> respMap = null;
		try {
			String reqSign = SignUtil.sign(placeOrderReq, signKey);
			placeOrderReq.put(WXPayConstont.SIGN, reqSign);
			String reqXml = XmlUtil.mapToXml(placeOrderReq, WXPayConstont.XML_ROOT);
			String respXml = HttpUtil.doPost(unifiedOrderUrl, reqXml, true);
			respMap = MapUtil.objMap2StrMap(XmlUtil.xml2map(respXml));
			logger.info("[微信]响应结果：" + respMap.get(WXPayConstont.RETURN_CODE) + "==响应信息：" + respMap.get(WXPayConstont.RETURN_MSG) + "==业务结果:" + respMap.get(WXPayConstont.RESULT_CODE));
		} catch (Exception e) {
			logger.error("WechatAppPayController.createOrder-调用微信下单接口异常", e);
			throw new RuntimeException("微信下单异常！");
		}
		if(!WXPayConstont.SUCCESS_CODE.equals(respMap.get(WXPayConstont.RETURN_CODE))){
			logger.error("WechatAppPayController.createOrder-微信下单失败！"+respMap.get(WXPayConstont.RETURN_CODE)+respMap.get(WXPayConstont.RETURN_MSG));
			throw new RuntimeException(respMap.get(WXPayConstont.RETURN_CODE)+respMap.get(WXPayConstont.RETURN_MSG));
		}
		
		if (!SignUtil.checkSign(respMap, signKey)) {
			logger.error("WechatAppPayController.createOrder-微信响应结果签名校验不符");
			logger.info("[微信]响应结果：" + respMap.get(WXPayConstont.RETURN_CODE) + "==响应信息：" + respMap.get(WXPayConstont.RETURN_MSG) + "==业务结果:" + respMap.get(WXPayConstont.RESULT_CODE));
			throw new RuntimeException("微信下单异常-相应结果签名校验不通过");
		}
		
		if (null != respMap  && WXPayConstont.SUCCESS_CODE.equals(respMap.get(WXPayConstont.RESULT_CODE))) {
			returnMap.put(WXPayConstont.APPID, "");
			returnMap.put(WXPayConstont.PARTNERID, "");
			returnMap.put(WXPayConstont.PREPAYID, "");
			returnMap.put(WXPayConstont.PACKAGE_KEY, "Sign=WXPay");
			returnMap.put("noncestr", UUIDUtil.getShortUuid());
			returnMap.put("timestamp", DateUtil.getTimeMillis(10));
			String sign = SignUtil.sign(returnMap, signKey);
			returnMap.put(WXPayConstont.SIGN, sign);
			returnMap.put("orderNo", "");// 必须在加签完成后才能放入该字段，本字段不是交易字段
			returnMap.put("needToPay", "");// 响应前端是否需要去支付的标志位
			logger.debug("下单返回前台结果："+returnMap);
		}
	}
	
	
	/**
	 * 接收到微信的支付结果通知处理
	 * @param xmlContent
	 */
	public boolean updatePayResultNotify(String xmlContent) {
		Map<String, String> notifyMap = null;
		try {
			notifyMap = MapUtil.objMap2StrMap(XmlUtil.xml2map(xmlContent));
			logger.debug("WechatAppPayServiceImpl.updatePayResultNotify-转换出来的map:"+notifyMap);
			if(!SignUtil.checkSign(notifyMap, signKey)){
				logger.error("WechatAppPayServiceImpl.updatePayResultNotify-签名校验不符！");
				throw new BusinessException(BusinessEnum.FAIL.getCode(), "签名校验不符！");
			}
		} catch (DocumentException e1) {
			logger.error("WechatAppPayServiceImpl.updatePayResultNotify-通知报文解析出错！", e1);
			throw new BusinessException(BusinessEnum.FAIL.getCode(), "通知报文解析出错！");
		}
		//TODO 
		return false;
	}
	
	
}
