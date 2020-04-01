package com.wql.cloud.basic.wxpay.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.wql.cloud.basic.wxpay.config.WechatPayConfig;
import com.wql.cloud.basic.wxpay.enums.TradeTypeEnum;
import com.wql.cloud.basic.wxpay.model.PlaceOrderModel;
import com.wql.cloud.basic.wxpay.model.RefundOrderModel;
import com.wql.cloud.basic.wxpay.refundutil.DecodeUtil;
import com.wql.cloud.basic.wxpay.result.PayNotifyResult;
import com.wql.cloud.basic.wxpay.result.PlaceOrderResult;
import com.wql.cloud.basic.wxpay.result.QueryOrderResult;
import com.wql.cloud.basic.wxpay.result.RefundNotifyResult;
import com.wql.cloud.basic.wxpay.service.WxPayService;
import com.wql.cloud.basic.wxpay.util.HttpUtil;
import com.wql.cloud.basic.wxpay.util.SignUtil;
import com.wql.cloud.basic.wxpay.util.WXPayConstant;
import com.wql.cloud.basic.wxpay.util.WXPayUtil;

import cn.hutool.core.util.XmlUtil;

@SuppressWarnings("deprecation")
@Service
public class WxPayServiceImpl implements WxPayService {
	
	private static final Logger logger = LoggerFactory.getLogger(WxPayServiceImpl.class);
	
	public static final String SUCCESS = "SUCCESS";
	public static final String NOTPAY = "NOTPAY";
	public static final String CLOSED = "CLOSED";
	public static final String USERPAYING = "USERPAYING";
	public static final String PAYERROR = "PAYERROR";
	
	@Autowired
	private WechatPayConfig wxPayConfig;
	
	@Override
	public PlaceOrderResult placeOrderForApp(PlaceOrderModel model) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			/**1.组装参数*/
			TreeMap<String, String> bizParams = new TreeMap<String, String>();
			bizParams.put(WXPayConstant.APPID, wxPayConfig.getAppId());
		    bizParams.put(WXPayConstant.MCH_ID, wxPayConfig.getMchId());
		    bizParams.put(WXPayConstant.OUT_TRADE_NO, model.getOutTradeNo());
			bizParams.put(WXPayConstant.NONCE_STR, UUID.randomUUID().toString().replaceAll("-", "")); 
		    bizParams.put(WXPayConstant.TOTAL_FEE, String.valueOf(model.getTotalFee().multiply(new BigDecimal(100)))); // 整数，单位为分
		    bizParams.put(WXPayConstant.TRADE_TYPE, TradeTypeEnum.APP.getTradeType()); //App支付
			bizParams.put(WXPayConstant.SPBILL_CREATE_IP, model.getCreateIp());
			bizParams.put(WXPayConstant.BODY, model.getBody());
			bizParams.put(WXPayConstant.FEE_TYPE, "CNY");
			bizParams.put(WXPayConstant.DEVICE_INFO, "WEB");
			bizParams.put(WXPayConstant.NOTIFY_URL, wxPayConfig.getPayNotifyUrl());
			bizParams.put(WXPayConstant.TIME_START, sdf.format(model.getTimeStart()));
			bizParams.put(WXPayConstant.TIME_EXPIRE, sdf.format(model.getTimeExpire()));
			bizParams.put(WXPayConstant.SIGN, SignUtil.sign(bizParams, wxPayConfig.getPrivateKey()));  
			
			/**2.发起请求*/
			String reqXml = XmlUtil.mapToXmlStr(bizParams, WXPayConstant.XML_ROOT);
			String respXml = HttpUtil.doPost(wxPayConfig.PLACE_ORDER_URL, reqXml, true);
			Map<String, Object> respMap = XmlUtil.xmlToMap(respXml);
			if(!WXPayConstant.SUCCESS_CODE.equals(respMap.get(WXPayConstant.RETURN_CODE))){
				return new PlaceOrderResult(false, String.valueOf(respMap.get(WXPayConstant.RETURN_MSG)));
			}
			if (!SignUtil.checkSign(respMap, wxPayConfig.getPrivateKey())) {
				return new PlaceOrderResult(false, "签名校验不通过");
			}
			
			/**3.下单成功*/
			Map<String, String> returnMap = new HashMap<String, String>();
			returnMap.put(WXPayConstant.APPID, wxPayConfig.getAppId());
			returnMap.put(WXPayConstant.PARTNERID, wxPayConfig.getMchId());
			returnMap.put(WXPayConstant.PREPAYID, String.valueOf(respMap.get("prepay_id")));
			returnMap.put(WXPayConstant.PACKAGE_KEY, "Sign=WXPay");
			returnMap.put(WXPayConstant.noncestr, UUID.randomUUID().toString().replaceAll("-", ""));
			returnMap.put(WXPayConstant.timestamp, String.valueOf(System.currentTimeMillis()).substring(0, 10));
			returnMap.put(WXPayConstant.SIGN, SignUtil.sign(returnMap, wxPayConfig.getPrivateKey()));
			return new PlaceOrderResult(true, "微信App支付-下单成功", returnMap);
		} catch (Exception e) {
			logger.error("微信App支付-下单，出现异常", e);
		}
		return new PlaceOrderResult(false, "微信App下单异常");
	}
	
	
	@Override
	public PlaceOrderResult placeOrderForH5(PlaceOrderModel model) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			/**1.组装参数*/
			TreeMap<String, String> bizParams = new TreeMap<String, String>();
			bizParams.put(WXPayConstant.APPID, wxPayConfig.getAppId());
		    bizParams.put(WXPayConstant.MCH_ID, wxPayConfig.getMchId());
		    bizParams.put(WXPayConstant.OUT_TRADE_NO, model.getOutTradeNo());
			bizParams.put(WXPayConstant.NONCE_STR, UUID.randomUUID().toString().replaceAll("-", "")); 
		    bizParams.put(WXPayConstant.TOTAL_FEE, String.valueOf(model.getTotalFee().multiply(new BigDecimal(100)))); // 整数，单位为分
		    bizParams.put(WXPayConstant.TRADE_TYPE, TradeTypeEnum.MWEB.getTradeType()); //H5支付
			bizParams.put(WXPayConstant.SPBILL_CREATE_IP, model.getCreateIp());
			bizParams.put(WXPayConstant.BODY, model.getBody());
			bizParams.put(WXPayConstant.FEE_TYPE, "CNY");
			bizParams.put(WXPayConstant.DEVICE_INFO, "WEB");
			bizParams.put(WXPayConstant.NOTIFY_URL, wxPayConfig.getPayNotifyUrl());
			bizParams.put(WXPayConstant.TIME_START, sdf.format(model.getTimeStart()));
			bizParams.put(WXPayConstant.TIME_EXPIRE, sdf.format(model.getTimeExpire()));
			bizParams.put(WXPayConstant.SIGN, SignUtil.sign(bizParams, wxPayConfig.getPrivateKey()));  
			
			/**2.发起请求*/
			String reqXml = XmlUtil.mapToXmlStr(bizParams, WXPayConstant.XML_ROOT);
			String respXml = HttpUtil.doPost(wxPayConfig.PLACE_ORDER_URL, reqXml, true);
			Map<String, Object> respMap = XmlUtil.xmlToMap(respXml);
			if(!WXPayConstant.SUCCESS_CODE.equals(respMap.get(WXPayConstant.RETURN_CODE))){
				return new PlaceOrderResult(false, String.valueOf(respMap.get(WXPayConstant.RETURN_MSG)));
			}
			if (!SignUtil.checkSign(respMap, wxPayConfig.getPrivateKey())) {
				return new PlaceOrderResult(false, "签名校验不通过");
			}
			
			/**3.下单成功*/
			return new PlaceOrderResult(true, "微信H5支付-下单成功", String.valueOf(respMap.get("mweb_url")));
		} catch (Exception e) {
			logger.error("微信H5支付-下单，出现异常", e);
		}
		return new PlaceOrderResult(false, "微信H5下单异常");
	}
	
	
	@Override
	public QueryOrderResult queryOrderByTradeNo(String outTradeNo) {
  		try {
  			/**1.组装参数*/
  			HashMap<String, String> bizParams = new HashMap<String, String>();
  	        bizParams.put(WXPayConstant.APPID, wxPayConfig.getAppId());
  	        bizParams.put(WXPayConstant.MCH_ID, wxPayConfig.getMchId());
  	        bizParams.put(WXPayConstant.OUT_TRADE_NO, outTradeNo);
  	        bizParams.put(WXPayConstant.NONCE_STR, UUID.randomUUID().toString().replaceAll("-", ""));
  			bizParams.put(WXPayConstant.SIGN, SignUtil.sign(bizParams, wxPayConfig.getPrivateKey()));
  			
  			/**2.发起请求，微信支付查询*/
  			String reqXml = XmlUtil.mapToXmlStr(bizParams, WXPayConstant.XML_ROOT);
  			String respXml = HttpUtil.doPost(WechatPayConfig.QUERY_ORDER_URL, reqXml, true);
  			Map<String, Object> respMap = XmlUtil.xmlToMap(respXml);
  			if(!WXPayConstant.SUCCESS_CODE.equals(respMap.get(WXPayConstant.RETURN_CODE))){
  	  			return new QueryOrderResult("查询微信支付订单结果失败", "fail");
  			}
  			
  			/**3.查询需要返回的字段*/
			if (SUCCESS.equals(respMap.get("return_code"))) {
				String tradeState = String.valueOf(respMap.get("trade_state"));
			    switch (tradeState) {
			        case SUCCESS:
			        	Date payDate = DateUtils.parseDate(String.valueOf(respMap.get("time_end")), "yyyyMMddHHmmss");
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
  			logger.error("查询微信支付结果异常", e);
  		}
  		return new QueryOrderResult("未知", "UNKONW");
	}
	
	
	@Override
	@SuppressWarnings("all")
	public String refundOrder(RefundOrderModel refundOrderModel) {
        String result = "";//这里用于返回处理返回结果 
        try {
        	/**1.组合业务参数（xml格式）*/
            TreeMap<String, Object> bizParams = new TreeMap<String, Object>();
            bizParams.put(WXPayConstant.APPID, wxPayConfig.getAppId());
		    bizParams.put(WXPayConstant.MCH_ID, wxPayConfig.getMchId());
		    bizParams.put(WXPayConstant.NONCE_STR, UUID.randomUUID().toString().replaceAll("-", ""));
			bizParams.put(WXPayConstant.OUT_TRADE_NO, refundOrderModel.getOutTradeNo());
		    bizParams.put(WXPayConstant.OUT_REFUND_NO, refundOrderModel.getOutRefundNo()); // 我们自己设定的退款申请号，约束为UK
		    long totalFee = refundOrderModel.getTotalFee().multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).longValue();
		    long refundFee = refundOrderModel.getRefundFee().multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).longValue();
		    bizParams.put(WXPayConstant.TOTAL_FEE, totalFee); // 单位为分
		    bizParams.put(WXPayConstant.REFUND_FEE, refundFee); // 单位为分
		    bizParams.put(WXPayConstant.NOTIFY_URL, wxPayConfig.getRefundNotifyUrl());
		    bizParams.put(WXPayConstant.OP_USER_ID, wxPayConfig.getMchId());
		    bizParams.put(WXPayConstant.SIGN, SignUtil.sign2(bizParams, wxPayConfig.getPrivateKey())); 
			String reqXml = XmlUtil.mapToXmlStr(bizParams, WXPayConstant.XML_ROOT);
			
			/**2.发起请求*/
			//加载证书，进行https加密传输
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(new File(wxPayConfig.getCertLocalPath()));// 放退款证书的路径
			try {
				//加载证书密码，默认为商户ID
				keyStore.load(instream, wxPayConfig.getMchId().toCharArray());
			} finally {
				instream.close();
			}
			// Trust own CA and all self-signed certs
			SSLContext sslcontext = SSLContexts.custom()
					.loadKeyMaterial(keyStore, wxPayConfig.getMchId().toCharArray())  //加载证书密码，默认为商户ID
					.build();
			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslcontext, 
					new String[] { "TLSv1" }, 
					null, 
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			try {
				HttpPost httpPost = new HttpPost(wxPayConfig.REFUND_ORDER_URL);// 退款接口
				StringEntity reqEntity = new StringEntity(reqXml);
				// 设置类型
				reqEntity.setContentType("application/x-www-form-urlencoded");
				httpPost.setEntity(reqEntity);
				CloseableHttpResponse response = httpclient.execute(httpPost);
				try {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
						String text;
						StringBuffer sbf = new StringBuffer();
						while ((text = bufferedReader.readLine()) != null) {
							sbf.append(text);
						}
						Map<String, Object> xmlResultMap = XmlUtil.xmlToMap(sbf.toString());
                	  	if("FAIL".equals(String.valueOf(xmlResultMap.get("return_code")))) {
                	  		return result;
                	  	}
						if(xmlResultMap.get("result_code") != null) {
							result = xmlResultMap.get("result_code")+"";
                	  	}
					}
					EntityUtils.consume(entity);
				} finally {
					response.close();
				}
			} finally {
				httpclient.close();
			}
        } catch (Exception e) {
        	logger.error("微信退款, 出现异常", e);
        }
        return result;
	}
	
	
	@Override
	public QueryOrderResult queryRefundOrderByTradeNo(String outTradeNo) {
		try {
  			/**1.组装参数*/
  			HashMap<String, String> bizParams = new HashMap<String, String>();
  	        bizParams.put(WXPayConstant.APPID, wxPayConfig.getAppId());
  	        bizParams.put(WXPayConstant.MCH_ID, wxPayConfig.getMchId());
  	        bizParams.put(WXPayConstant.OUT_TRADE_NO, outTradeNo);
  	        bizParams.put(WXPayConstant.NONCE_STR, UUID.randomUUID().toString().replaceAll("-", ""));
  			bizParams.put(WXPayConstant.SIGN, SignUtil.sign(bizParams, wxPayConfig.getPrivateKey()));
  			
  			/**2.发起请求，微信支付查询*/
  			String reqXml = XmlUtil.mapToXmlStr(bizParams, WXPayConstant.XML_ROOT);
  			String respXml = HttpUtil.doPost(WechatPayConfig.QUERY_REFUND_ORDER_URL, reqXml, true);
  			Map<String, Object> respMap = XmlUtil.xmlToMap(respXml);
  			if(!WXPayConstant.SUCCESS_CODE.equals(respMap.get(WXPayConstant.RETURN_CODE))){
  	  			return new QueryOrderResult("查询微信支付订单结果失败", "fail");
  			}
			if (SUCCESS.equals(respMap.get("return_code"))) {
				int n = 0; //第一笔退款
				String refundStatus = String.valueOf(respMap.get("refund_status_"+n));
			    switch (refundStatus) {
			        case SUCCESS:
			        	Date payDate = DateUtils.parseDate(String.valueOf(respMap.get("refund_success_time_"+n)), "yyyyMMddHHmmss");
			        	return new QueryOrderResult("退款成功", refundStatus, payDate);
			        case "REFUNDCLOSE":
			        	return new QueryOrderResult("退款关闭", refundStatus);
			        case "PROCESSING":
			        	return new QueryOrderResult("退款处理中", refundStatus);
			        case "CHANGE":
			        	return new QueryOrderResult("退款异常", refundStatus);
			        default:
			            break;
			    }
			} else {
				return new QueryOrderResult("退款失败", "FAIL");
			}
  		} catch (Exception e) {
  			logger.error("查询微信退款结果异常", e);
  		}
  		return new QueryOrderResult("未知", "UNKNOW");
	}
	
	
	@Override
	public PayNotifyResult paySuccessNotify(String xmlStr) {
		try {
			boolean signatureValid = WXPayUtil.isSignatureValid(xmlStr, wxPayConfig.getPrivateKey());
			if(!signatureValid) {
				logger.warn("微信支付回调，验签失败");
				return null;
			}
			
			//解析返回内容
			Map<String, Object> xml2map = XmlUtil.xmlToMap(xmlStr);
        	String returnCode = String.valueOf(xml2map.get("return_code"));
        	String resultCode = String.valueOf(xml2map.get("result_code"));
        	if(!SUCCESS.equals(returnCode) || !SUCCESS.equals(resultCode)) {
        		logger.warn("微信app支付，支付返回失败");
        		return null;
        	}
        	
        	//响应对象
        	PayNotifyResult payNotifyResult = new PayNotifyResult();
        	payNotifyResult.setOutTradeNo(String.valueOf(xml2map.get("out_trade_no")));
        	payNotifyResult.setPayedTime(DateUtils.parseDate(String.valueOf(xml2map.get("time_end")), "yyyyMMddHHmmss"));
        	return payNotifyResult;
		} catch (Exception e) {
			logger.error("微信支付回调，校验异常", e);
		}
		return null;
	}
	
	
	@Override
	public RefundNotifyResult refundNotify(String xmlStr) {
		try {
			Map<String, Object> xml2map = XmlUtil.xmlToMap(xmlStr);
			if(SUCCESS.equals(xml2map.get("return_code"))){
				//获得加密信息 （req_info）
                String passMap = DecodeUtil.decryptData(String.valueOf(xml2map.get("req_info")));
                //拿到解密信息
                Map<String, Object> decodeMap = XmlUtil.xmlToMap(passMap);
                if(logger.isDebugEnabled()) {
                	logger.debug("退款-微信回调，解密后内容：" + decodeMap);
                }
                //响应对象
                RefundNotifyResult notify = new RefundNotifyResult();
            	notify.setOutRefundNo(String.valueOf(decodeMap.get("out_refund_no")));
            	notify.setRefundStatus(String.valueOf(decodeMap.get("refund_status")));
            	notify.setOutTradeNo(String.valueOf(decodeMap.get("out_trade_no")));
            	notify.setSuccessTime(DateUtils.parseDate(String.valueOf(decodeMap.get("success_time")), "yyyy-MM-dd HH:mm:ss"));
            	return notify;
			} 
		} catch (Exception e) {
			logger.error("微信退款回调，校验异常", e);
		}
		return null;
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
