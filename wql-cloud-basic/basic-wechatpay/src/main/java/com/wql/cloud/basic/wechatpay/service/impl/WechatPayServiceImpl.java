package com.wql.cloud.basic.wechatpay.service.impl;

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

import com.wql.cloud.basic.wechatpay.config.WechatPayConfig;
import com.wql.cloud.basic.wechatpay.enums.TradeTypeEnum;
import com.wql.cloud.basic.wechatpay.model.PlaceOrderModel;
import com.wql.cloud.basic.wechatpay.model.RefundOrderModel;
import com.wql.cloud.basic.wechatpay.refundutil.DecodeUtil;
import com.wql.cloud.basic.wechatpay.result.PayNotifyResult;
import com.wql.cloud.basic.wechatpay.result.PlaceOrderResult;
import com.wql.cloud.basic.wechatpay.result.QueryOrderResult;
import com.wql.cloud.basic.wechatpay.result.RefundNotifyResult;
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

@SuppressWarnings("deprecation")
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
	public PlaceOrderResult placeOrderForApp(PlaceOrderModel model) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			/**1.组装参数*/
			TreeMap<String, String> bizParams = new TreeMap<String, String>();
			bizParams.put(WXPayConstant.APPID, wechatPayConfig.getAppId());
		    bizParams.put(WXPayConstant.MCH_ID, wechatPayConfig.getMchId());
		    bizParams.put(WXPayConstant.TOTAL_FEE, String.valueOf(model.getTotalFee().multiply(new BigDecimal(100)))); // 整数，单位为分
		    bizParams.put(WXPayConstant.TRADE_TYPE, TradeTypeEnum.APP.getTradeType()); //App支付
			bizParams.put(WXPayConstant.SPBILL_CREATE_IP, model.getCreateIp());
			bizParams.put(WXPayConstant.BODY, model.getBody());
			bizParams.put(WXPayConstant.OUT_TRADE_NO, model.getOutTradeNo());
			bizParams.put(WXPayConstant.NONCE_STR, UUIDUtil.getShortUuid()); 
			bizParams.put(WXPayConstant.FEE_TYPE, "CNY");
			bizParams.put(WXPayConstant.DEVICE_INFO, "WEB");
			bizParams.put(WXPayConstant.NOTIFY_URL, wechatPayConfig.getPayNotifyUrl());
			bizParams.put(WXPayConstant.TIME_START, sdf.format(model.getTimeStart()));
			bizParams.put(WXPayConstant.TIME_EXPIRE, sdf.format(model.getTimeExpire()));
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
			bizParams.put(WXPayConstant.APPID, wechatPayConfig.getAppId());
		    bizParams.put(WXPayConstant.MCH_ID, wechatPayConfig.getMchId());
		    bizParams.put(WXPayConstant.TOTAL_FEE, String.valueOf(model.getTotalFee().multiply(new BigDecimal(100)))); // 整数，单位为分
		    bizParams.put(WXPayConstant.TRADE_TYPE, TradeTypeEnum.MWEB.getTradeType()); //H5支付
			bizParams.put(WXPayConstant.SPBILL_CREATE_IP, model.getCreateIp());
			bizParams.put(WXPayConstant.BODY, model.getBody());
			bizParams.put(WXPayConstant.OUT_TRADE_NO, model.getOutTradeNo());
			bizParams.put(WXPayConstant.NONCE_STR, UUIDUtil.getShortUuid()); 
			bizParams.put(WXPayConstant.FEE_TYPE, "CNY");
			bizParams.put(WXPayConstant.DEVICE_INFO, "WEB");
			bizParams.put(WXPayConstant.NOTIFY_URL, wechatPayConfig.getPayNotifyUrl());
			bizParams.put(WXPayConstant.TIME_START, sdf.format(model.getTimeStart()));
			bizParams.put(WXPayConstant.TIME_EXPIRE, sdf.format(model.getTimeExpire()));
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
			return new PlaceOrderResult(true, "微信H5支付-下单成功", respMap.get("mweb_url"));
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
  			logger.error("查询微信支付结果异常", e);
  		}
  		return new QueryOrderResult("未知", "unknow");
	}
	
	
	@Override
	@SuppressWarnings("all")
	public String refundOrder(RefundOrderModel refundOrderModel) {
        String result = "";//这里用于返回处理返回结果 
        try {
        	/**1.组合业务参数（xml格式）*/
            TreeMap<String, Object> bizParams = new TreeMap<String, Object>();
            bizParams.put(WXPayConstant.APPID, wechatPayConfig.getAppId());
		    bizParams.put(WXPayConstant.MCH_ID, wechatPayConfig.getMchId());
		    bizParams.put(WXPayConstant.NONCE_STR, UUIDUtil.getShortUuid());
			bizParams.put("out_trade_no", refundOrderModel.getOutTradeNo());
		    bizParams.put("out_refund_no", refundOrderModel.getOutRefundNo()); // 我们自己设定的退款申请号，约束为UK
		    long totalFee = refundOrderModel.getTotalFee().multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).longValue();
		    long refundFee = refundOrderModel.getRefundFee().multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).longValue();
		    bizParams.put("total_fee", totalFee); // 单位为分
		    bizParams.put("refund_fee", refundFee); // 单位为分
		    bizParams.put("op_user_id", wechatPayConfig.getMchId());
		    bizParams.put(WXPayConstant.SIGN, SignUtil.sign2(bizParams, wechatPayConfig.getPrivateKey())); 
			String reqXml = XmlUtil.mapToXml2(bizParams, WXPayConstant.XML_ROOT);
			
			/**2.发起请求*/
			//加载证书，进行https加密传输
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(new File(wechatPayConfig.getCertLocalPath()));// 放退款证书的路径
			try {
				//加载证书密码，默认为商户ID
				keyStore.load(instream, wechatPayConfig.getMchId().toCharArray());
			} finally {
				instream.close();
			}
			// Trust own CA and all self-signed certs
			SSLContext sslcontext = SSLContexts.custom()
					.loadKeyMaterial(keyStore, wechatPayConfig.getMchId().toCharArray())  //加载证书密码，默认为商户ID
					.build();
			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslcontext, 
					new String[] { "TLSv1" }, 
					null, 
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			try {
				HttpPost httpPost = new HttpPost(wechatPayConfig.REFUND_ORDER_URL);// 退款接口
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
						Map<String, String> xmlResultMap = MapUtil.objMap2StrMap(XmlUtil.xml2map(sbf.toString()));
                	  	if("FAIL".equals(xmlResultMap.get("return_code"))) {
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
  	        bizParams.put(WXPayConstant.APPID, wechatPayConfig.getAppId());
  	        bizParams.put(WXPayConstant.MCH_ID, wechatPayConfig.getMchId());
  	        bizParams.put(WXPayConstant.OUT_TRADE_NO, outTradeNo);
  	        bizParams.put(WXPayConstant.NONCE_STR, UUIDUtil.getShortUuid());
  			bizParams.put(WXPayConstant.SIGN, SignUtil.sign(bizParams, wechatPayConfig.getPrivateKey()));
  			
  			/**2.发起请求，微信支付查询*/
  			String reqXml = XmlUtil.mapToXml(bizParams, WXPayConstant.XML_ROOT);
  			String respXml = HttpUtil.doPost(WechatPayConfig.QUERY_REFUND_ORDER_URL, reqXml, true);
  			Map<String, String> respMap = MapUtil.objMap2StrMap(XmlUtil.xml2map(respXml));
  			if(!WXPayConstant.SUCCESS_CODE.equals(respMap.get(WXPayConstant.RETURN_CODE))){
  	  			return new QueryOrderResult("查询微信支付订单结果失败", "fail");
  			}
			if (SUCCESS.equals(respMap.get("return_code"))) {
				int n = 0;
				for(int i = 0; i <= 50; i++) {
					if(null != respMap.get("refund_status_"+i)) {
						n = i;
						break;
					}
				}
				String refundStatus = respMap.get("refund_status_"+n);
			    switch (refundStatus) {
			        case SUCCESS:
			        	Date payDate = DateUtil.parseDate(respMap.get("refund_success_time_"+n), "yyyyMMddHHmmss");
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
			}
  		} catch (Exception e) {
  			logger.error("查询微信退款结果异常", e);
  		}
  		return new QueryOrderResult("未知", "unknow");
	}
	
	
	@Override
	public PayNotifyResult paySuccessNotify(String xmlStr) {
		try {
			boolean signatureValid = WXPayUtil.isSignatureValid(xmlStr, wechatPayConfig.getPrivateKey());
			if(!signatureValid) {
				logger.warn("微信支付回调，验签失败");
				return null;
			}
			
			//解析返回内容
			Map<String, Object> xml2map = XmlUtil.xml2map(xmlStr);
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
			Map<String, Object> xml2map = XmlUtil.xml2map(xmlStr);
			if("SUCCESS".equals(xml2map.get("return_code"))){
				//获得加密信息 （req_info）
                String passMap = DecodeUtil.decryptData(String.valueOf(xml2map.get("req_info")));
                //拿到解密信息
                Map<String, Object> decodeMap = XmlUtil.xml2map(passMap);
                logger.info("退款-微信回调返回，拿到解密后：" + decodeMap);
                
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
