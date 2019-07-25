package com.wql.cloud.basic.wechatpay.refundutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 退款请求调用方法
 * 使用流程：
   RefundRequest refundRequest = new RefundRequest();
   String httpsRequest = refundRequest.httpsRequest(url, reqXml, certPath, mchId);
 * 
 * @author wangqiulin
 */
@SuppressWarnings("deprecation")
public class RefundRequest {

	private static final Logger logger = LoggerFactory.getLogger(RefundRequest.class);
	
	// 连接超时时间，默认10秒
	private int socketTimeout = 10000;
	// 传输超时时间，默认30秒
	private int connectTimeout = 30000;
	// 请求器的配置
	private RequestConfig requestConfig;
	// HTTP请求器
	private CloseableHttpClient httpClient;

	/**
	 * 加载证书
	 * 
	 * @param path： 证书路径
	 * @param mchId ： 商户号
	 * @throws IOException
	 */
	@SuppressWarnings("all")
	private void initCert(String path, String mchId) throws Exception {
		// 拼接证书的路径
		KeyStore keyStore = KeyStore.getInstance("PKCS12");

		// 加载本地的证书进行https加密传输
		FileInputStream instream = new FileInputStream(new File(path));
		try {
			keyStore.load(instream, mchId.toCharArray()); // 加载证书密码，默认为商户ID
		} finally {
			instream.close();
		}

		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom()
				.loadKeyMaterial(keyStore, mchId.toCharArray()) // 加载证书密码，默认为商户ID
				.build();
		
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext, 
				new String[] { "TLSv1" }, 
				null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

		httpClient = HttpClients.custom()
				.setSSLSocketFactory(sslsf)
				.build();

		// 根据默认超时限制初始化requestConfig
		requestConfig = RequestConfig.custom()
				.setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout)
				.build();
	}

	
	/**
	 * 通过Https往API post xml数据
	 * 
	 * @param url    API地址
	 * @param xmlObj 要提交的XML数据对象
	 * @param path   证书存放的目录
	 * @param mchId  商户号
	 * @return
	 */
	public String httpsRequest(String url, String xmlObj, String path, String mchId) {
		String result = null;
		// 加载证书
		try {
			initCert(path, mchId);
		} catch (Exception e1) {
			logger.error("证书加载异常");
			return result;
		}

		HttpPost httpPost = new HttpPost(url);
		// 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
		StringEntity postEntity = new StringEntity(xmlObj, "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.setEntity(postEntity);
		// 设置请求器的配置
		httpPost.setConfig(requestConfig);
		
		try {
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
		} catch (ConnectionPoolTimeoutException e) {
			logger.trace("http get throw ConnectionPoolTimeoutException(wait time out)");
		} catch (ConnectTimeoutException e) {
			logger.trace("http get throw ConnectTimeoutException");
		} catch (SocketTimeoutException e) {
			logger.trace("http get throw SocketTimeoutException");
		} catch (Exception e) {
			logger.trace("http get throw Exception");
		} finally {
			httpPost.abort();
		}
		return result;
	}

}
