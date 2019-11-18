package com.wql.cloud.basic.wxpay.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	private static final int timeout = 100000;
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static volatile CloseableHttpClient client;
	private final static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();
	private static volatile CloseableHttpClient httpsClient = null;
	private static volatile CloseableHttpClient httpClient = null;
	private static volatile CloseableHttpClient httpsClientWithCert = null;
	private static CertConfig CERT_CONFIG = null;

	/**
	 * 发送get请求
	 */
	public static String doGet(String url, Map<String, String> params) throws ParseException, IOException {
		URI uri = generateURL(url, params);
		HttpGet get = new HttpGet(uri);
		get.setConfig(requestConfig);
		logger.info("HttpUtil.doPost-请求URL: " + uri +", 请求参数:"+ JSONObject.toJSONString(params));
		long start = new Date().getTime();
		String res = execute(get);
		logger.info("HTTP Response context: " + res);
		logger.info("HTTP costs：" + (new Date().getTime() - start));
		return res;
	}

	/**
	 * @param url
	 * @param params
	 * @param certConfig
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, String params, CertConfig certConfig) throws Exception {
		logger.info("HttpUtil.doPost-请求URL: {}, 请求参数: {}", url, params);
		String result = new String();
		try {
			buildHttpsClientWithCert(certConfig);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			httpPost.setEntity(new StringEntity(params, DEFAULT_CHARSET));
			long start = new Date().getTime();
			CloseableHttpResponse response = null;
			response = httpsClientWithCert.execute(httpPost);
			logger.info("HttpUtil.doPost-请求用时:" + (new Date().getTime() - start));
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, DEFAULT_CHARSET);
			logger.info("HttpUtil.doPost-响应结果" + result);
			response.close();
		} catch (UnsupportedEncodingException e) {
			logger.error("HttpUtil.doPost- occur an UnsupportedEncodingException:", e);
		} catch (ClientProtocolException e) {
			logger.error("HttpUtil.doPost- occur an ClientProtocolException:", e);
		} catch (Exception e) {
			logger.error("HttpUtil.doPost- occur an IOException:", e);
		}
		return result;
	}

	/**
	 * @param url
	 * @param params
	 * @param https
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, String params, boolean https) throws Exception {
		logger.info("HttpUtil.doPost-请求URL: {}, 请求参数: {}", url, params);
		String result = new String();
		try {
			buildHttpClient(https);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			httpPost.setEntity(new StringEntity(params, DEFAULT_CHARSET));
			long start = new Date().getTime();
			CloseableHttpResponse response = null;
			if (https) {
				response = httpsClient.execute(httpPost);
			} else {
				response = httpClient.execute(httpPost);
			}
			logger.info("HttpUtil.doPost-请求用时:" + (new Date().getTime() - start));
			// url = URLDecoder.decode(url, DEFAULT_CHARSET);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, DEFAULT_CHARSET);
			logger.info("HttpUtil.doPost-响应结果" + result);
			response.close();
		} catch (UnsupportedEncodingException e) {
			logger.error("HttpUtil.doPost- occur an UnsupportedEncodingException:", e);
		} catch (ClientProtocolException e) {
			logger.error("HttpUtil.doPost- occur an ClientProtocolException:", e);
		} catch (IOException e) {
			logger.error("HttpUtil.doPost- occur an IOException:", e);
		}
		return result;
	}

	/**
	 * 发送POST请求,用于报文正文为key-value格式的post请求
	 * 
	 * @param url
	 * @param params
	 * @param https
	 * @return
	 */
	public static String doPost(String url, Map<String, String> params, boolean https) {
		logger.info("HttpUtil.doPost-请求URL: {}, 请求参数: {}", url, params);
		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			pairList.add(new BasicNameValuePair(key, params.get(key)));
		}
		String result = new String();
		try {
			buildHttpClient(https);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			httpPost.setEntity(new UrlEncodedFormEntity(pairList, DEFAULT_CHARSET));
			httpPost.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"));
			long start = new Date().getTime();
			CloseableHttpResponse response = null;
			if (https) {
				response = httpsClient.execute(httpPost);
			} else {
				response = httpClient.execute(httpPost);
			}
			logger.info("HttpUtil.doPost-请求用时:" + (new Date().getTime() - start));
			// url = URLDecoder.decode(url, DEFAULT_CHARSET);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, DEFAULT_CHARSET);
			logger.info("HttpUtil.doPost-响应结果" + result);
			response.close();
		} catch (Exception e) {
			logger.error("调用http服务失败:", e);
		}
		return result;
	}

	// ---------------华丽的分割线，以下为私有方法--------------------------------

	/**
	 * 根据证书配置创建httpsClientWithCert
	 * 
	 * @param certConfig
	 * @throws Exception
	 */
	private static void buildHttpsClientWithCert(CertConfig certConfig) throws Exception {
		if (certConfig == null) {
			throw new RuntimeException("HttpUtil.buildHttpsClientWithCert-certConfig为空!");
		}
		if (certConfig.equals(CERT_CONFIG))
			return;// 如果是正式配置相同则返回

		KeyStore keyStore = null;
		FileInputStream instream = null;
		try {
			keyStore = KeyStore.getInstance(certConfig.getKeyStoreType());// 获取指定类型的KeyStore
			instream = new FileInputStream(certConfig.getCertPath());// 证书文件流
			keyStore.load(instream, certConfig.getKeyStorePwd().toCharArray());// 加载证书
		} catch (Exception e) {
			logger.error("根据类型获取KeyStore异常!",e);
			throw e;
		} finally {
			if(null!=instream){
				instream.close();
			}
		}
		if (httpsClientWithCert == null) {
			synchronized (HttpUtil.class) {
				if (httpsClientWithCert == null) {
					SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, certConfig.getKeyStorePwd().toCharArray()).build();
					// 指定TLS版本
					SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, NoopHostnameVerifier.INSTANCE);
					// 设置httpclient的SSLSocketFactory
					httpsClientWithCert = HttpClients.custom().setSSLSocketFactory(sslsf).build();
					CERT_CONFIG = certConfig;
				}
			}
		}
	}

	private static void buildHttpClient(boolean isHttps) throws NoSuchAlgorithmException, KeyManagementException {
		if (isHttps) {
			if (httpsClient == null) {
				synchronized (HttpUtil.class) {
					if (httpsClient == null) {
						SSLContext ctx = SSLContext.getInstance("TLS");
						X509TrustManager tm = new X509TrustManager() {
							public X509Certificate[] getAcceptedIssuers() {
								return null;
							}

							public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
							}

							public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
							}
						};
						ctx.init(null, new TrustManager[] { tm }, null);
						LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
						RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
						registryBuilder.register("https", sslSF);
						Registry<ConnectionSocketFactory> registry = registryBuilder.build();
						PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
						httpsClient = HttpClientBuilder.create().setConnectionManager(connManager).build();
					}
				}
			}
		} else {
			if (httpClient == null) {
				synchronized (HttpUtil.class) {
					if (httpClient == null) {
						httpClient = HttpClientBuilder.create().setMaxConnPerRoute(5).setMaxConnTotal(50).build();
					}
				}
			}
		}
	}

	/**
	 * 初始化httpClient, SSLContext信任密钥
	 * 
	 * @return CloseableHttpClient
	 */
	public static CloseableHttpClient getClient() {
		if (null == client) {
			synchronized (HttpUtil.class) {
				if (client == null) {
					SSLConnectionSocketFactory sslCSF = null;
					try {
						// 信任密钥
						SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
						sslCSF = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					client = HttpClients.custom().setSSLSocketFactory(sslCSF).build();
				}
			}
		}
		return client;
	}

	/**
	 * 发起请求(HttpPost/HttpGet)，关闭资源
	 */
	private static String execute(HttpUriRequest request) throws ParseException, IOException {
		String responseStr = null;
		CloseableHttpResponse httpResponse = null;

		httpResponse = getClient().execute(request);
		responseStr = EntityUtils.toString(httpResponse.getEntity(), DEFAULT_CHARSET);
		httpResponse.close();
		return responseStr;
	}

	/**
	 * 生成请求的url
	 * 
	 * @param url
	 *            不带参数的url字符串
	 * @param params
	 *            请求参数
	 * @return 请求的uri对象
	 */
	private static URI generateURL(String url, Map<String, String> params) {
		URI uri = null;
		try {
			URIBuilder uriBuilder = new URIBuilder(url);
			if (null != params) {
				for (Entry<String, String> entry : params.entrySet()) {
					uriBuilder.addParameter(entry.getKey(), entry.getValue());
				}
			}
			uri = uriBuilder.build();
		} catch (URISyntaxException e) {
			logger.error(e.getMessage(), e);
		}
		return uri;
	}

	/**
	 * 证书配置
	 */
	public static class CertConfig {
		private String keyStoreType;
		private String certPath;
		private String keyStorePwd;

		public String getKeyStoreType() {
			return keyStoreType;
		}

		public void setKeyStoreType(String keyStoreType) {
			this.keyStoreType = keyStoreType;
		}

		public String getCertPath() {
			return certPath;
		}

		public void setCertPath(String certPath) {
			this.certPath = certPath;
		}

		public String getKeyStorePwd() {
			return keyStorePwd;
		}

		public void setKeyStorePwd(String keyStorePwd) {
			this.keyStorePwd = keyStorePwd;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((certPath == null) ? 0 : certPath.hashCode());
			result = prime * result + ((keyStorePwd == null) ? 0 : keyStorePwd.hashCode());
			result = prime * result + ((keyStoreType == null) ? 0 : keyStoreType.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CertConfig other = (CertConfig) obj;
			if (certPath == null) {
				if (other.certPath != null)
					return false;
			} else if (!certPath.equals(other.certPath))
				return false;
			if (keyStorePwd == null) {
				if (other.keyStorePwd != null)
					return false;
			} else if (!keyStorePwd.equals(other.keyStorePwd))
				return false;
			if (keyStoreType == null) {
				if (other.keyStoreType != null)
					return false;
			} else if (!keyStoreType.equals(other.keyStoreType))
				return false;
			return true;
		}
	}

}
