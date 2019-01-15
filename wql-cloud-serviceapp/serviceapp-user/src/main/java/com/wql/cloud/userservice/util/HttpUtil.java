package com.wql.cloud.userservice.util;

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
import java.util.HashMap;
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
import org.apache.log4j.Logger;

public class HttpUtil {

	private static final Logger logger = Logger.getLogger(HttpUtil.class);

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
		logger.info("HttpUtil.doPost-请求URL: " + uri +";请求参数:"+JsonUtil.buildNonNullBinder().toJson(params));
		long start = new Date().getTime();
		String res = execute(get);
		logger.info("HTTP Response context: \n" + res);
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
		logger.info("HttpUtil.doPost-请求URL: " + url);
		logger.info("HttpUtil.doPost-请求参数: " + params);
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
		logger.info("HttpUtil.doPost-请求URL: " + url);
		logger.info("HttpUtil.doPost-请求参数: " + params);
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
		logger.info("HttpUtil.doPost-请求URL: " + url);
		logger.info("HttpUtil.doPost-请求参数: " + params);
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

	public static void main(String[] args) {
		String url = "http://127.0.0.1:8080/creditapp/outer/pbccrc/report";
		Map<String, String> params = new HashMap<String, String>();
		params.put("orgcode", "20310115201608001");
		params.put("hash", "5ec2b80e31c9bac94093bb658dc206a8");
		params.put(
				"data",
				"{\"report_extend\":\"{\"cusName\":\"张君\",\"cusIdNo\":\"120104198703284316\","
						+ "\"binderName\":\"张君\",\"binderIdNo\":\"120104198703284316\","
						+ "\"operatorId\":\"300111000145566430\",\"extend_1\":null,"
						+ "\"extend_2\":null,\"extend_3\":null}\","
						+ "\"report_risk\":\"{\"personalinfo\":{\"marital\":\"已婚\",\"IDnumber\":\"120104198703284316\","
						+ "\"IDtype\":\"身份证\",\"name\":\"张君\"},\"reportinfo\":{\"reportSN\":\"2016091400003193182887\","
						+ "\"reporttime\":\"20160914173643\",\"querytime\":\"20160914112405\"},"
						+ "\"overdues\":{\"creditOrgCounts\":0,\"creditOrgCounts200\":0,\"creditAmts\":0,\"creditAmts200\":0,"
						+ "\"creditCountsM60\":0,\"creditCountsM60D90\":0,\"loanCounts\":0,\"loanAmts\":0,\"loanCountsM60\":0,"
						+ "\"loanCountsM60D90\":0,\"countsM60\":0,\"countsM60D90\":0},\"debts\":{\"creditLimitMax\":41000,"
						+ "\"creditLimitTotal\":94685,\"creditOrgCounts\":0,\"creditLimitUsed\":2750,\"creditLimitUseRate\":"
						+ "\"2.90%\",\"loanAmts\":0,\"loanAmtsNoSettle\":0,\"loanCounts\":0,\"loanBalances\":0,\"loanBalanceCounts\":0,"
						+ "\"loanBalancesMortgage\":0,\"loanBalancesCar\":0,\"loanBalancesBiz\":0,\"loanBalancesOther\":0,"
						+ "\"loanBalancesMonth\":0,\"loanBalancesMortgageMonth\":0,\"loanBalancesCarMonth\":0,\"loanBalancesBizMonth\":0,"
						+ "\"loanBalancesOtherMonth\":0,\"loanBalanceInfos\":[],\"loanBalancesMonthOther\":0},\"creditLoanHis\":{\"creditMOB\":129,\"loanMOB\":0},\"creditLoanNeeds\":{\"creditOrgCountsM3\":0,\"creditLimitTotalM3\":0,\"loanCountsM3\":0,\"loanAmtsM3\":0,\"loanQueriesM3\":1,\"selfQueriesM3\":1},\"others\":{\"guarantees\":0,\"guaranteeAmts\":0,\"month6TaxAmts\":0}}\",\"report\":\"{\"reportinfo\":{\"reportSN\":\"2016091400003193182887\",\"reporttime\":\"20160914173643\",\"querytime\":\"20160914112405\"},\"personalinfo\":{\"marital\":\"已婚\",\"IDnumber\":\"120104198703284316\",\"IDtype\":\"身份证\",\"name\":\"张君\"},\"creditRecord\":{\"summary\":{\"otherLoan\":{\"overdueTotal\":\"0\",\"activeTotal\":\"0\",\"accountTotal\":\"0\",\"overdue90Total\":\"0\",\"guarantee\":\"0\"},\"mortgage\":{\"overdueTotal\":\"0\",\"activeTotal\":\"0\",\"accountTotal\":\"0\",\"overdue90Total\":\"0\",\"guarantee\":\"0\"},\"creditCard\":{\"overdueTotal\":\"0\",\"activeTotal\":\"11\",\"accountTotal\":\"15\",\"overdue90Total\":\"0\",\"guarantee\":\"0\"}},\"intro\":\"?这部分包含您的信用卡、贷款和其他信贷记录。金额类数据均以人民币计算，精确到元。\",\"detail\":{\"otherLoan\":{\"noOverdueDetails\":[],\"overdueDetails\":[]},\"mortgage\":{\"noOverdueDetails\":[],\"overdueDetails\":[]},\"creditCard\":{\"noOverdueDetails\":[\"2014年10月27日中国工商银行上海市分行发放的贷记卡（人民币账户）。截至2016年8月，信用额度500，已使用额度0。\",\"2011年12月30日中国农业银行发放的贷记卡（美元账户）。截至2016年8月,信用额度折合人民币8,292，已使用额度0。\",\"2011年12月30日中国农业银行发放的贷记卡（人民币账户）。截至2016年8月，信用额度10,000，已使用额度0。\",\"2010年10月25日招商银行发放的贷记卡（美元账户）。截至2016年8月,信用额度折合人民币25,000，已使用额度0。\",\"2010年10月25日招商银行发放的贷记卡（人民币账户）。截至2016年8月，信用额度25,000，已使用额度2,750。\",\"2010年10月16日中国建设银行上海市分行发放的贷记卡（人民币账户）。截至2016年8月，信用额度2,000，已使用额度0。\",\"2010年10月16日中国建设银行上海市分行发放的贷记卡（美元账户）。截至2016年8月,信用额度折合人民币1,893，已使用额度0。\",\"2010年9月18日交通银行发放的贷记卡（人民币账户）。截至2016年8月，信用额度41,000，已使用额度0。\",\"2007年6月5日中国银行上海市分行发放的贷记卡（人民币账户）。截至2016年7月，信用额度4,500，已使用额度0。\",\"2005年12月7日兴业银行发放的贷记卡（人民币账户）。截至2016年8月，信用额度1,500，已使用额度0。\",\"2005年12月7日兴业银行发放的贷记卡（美元账户）。截至2016年8月,信用额度折合人民币1,500，已使用额度0。\",\"2009年8月5日浦发银行信用卡中心发放的贷记卡（人民币账户），截至2016年8月已销户。\",\"2007年11月26日中国建设银行上海市分行发放的贷记卡（人民币账户），截至2011年8月已销户。\",\"2007年11月26日中国建设银行上海市分行发放的贷记卡（美元账户），截至2011年8月已销户。\",\"2007年6月5日中国银行上海市分行发放的贷记卡（美元账户），截至2009年6月已销户。\"],\"overdueDetails\":[]}}},\"publicRecord\":{\"summary\":{\"enforcement\":\"0\",\"tax\":\"0\",\"punishment\":\"0\",\"judgment\":\"0\",\"telecom\":\"0\"},\"intro\":\" 系统中没有您最近5年内的欠税记录、民事判决记录、强制执行记录、行政处罚记录及电信欠费记录。 \",\"detail\":{\"enforcement\":[],\"tax\":[],\"punishment\":[],\"judgment\":[],\"telecom\":[]}},\"queryRecord\":{\"summary\":{\"individual\":\"1\",\"organization\":\"13\"},\"intro\":\"这部分包含您的信用报告最近2年被查询的记录。\",\"detail\":{\"individual\":[{\"date\":\"2016年8月3日\",\"reason\":\"本人查询（互联网个人信用信息服务平台）\",\"operator\":\"本人\"}],\"organization\":[{\"date\":\"2016年9月3日\",\"reason\":\"贷后管理\",\"operator\":\"招商银行/CMBU*ER0*3\"},{\"date\":\"2016年7月12日\",\"reason\":\"贷后管理\",\"operator\":\"交通银行太平洋信用卡中心/d*anjx\"},{\"date\":\"2016年7月8日\",\"reason\":\"贷后管理\",\"operator\":\"招商银行/C*BUSE*003\"},{\"date\":\"2016年6月27日\",\"reason\":\"贷款审批\",\"operator\":\"南京银行股份有限公司/*rb26\"},{\"date\":\"2016年5月7日\",\"reason\":\"贷后管理\",\"operator\":\"交通银行太平洋信用卡中心/*uanjx\"},{\"date\":\"2016年1月5日\",\"reason\":\"贷后管理\",\"operator\":\"招商银行/CM*USER0*4\"},{\"date\":\"2015年11月27日\",\"reason\":\"贷后管理\",\"operator\":\"交通银行太平洋信用卡中心/*uanjx\"},{\"date\":\"2015年10月28日\",\"reason\":\"贷后管理\",\"operator\":\"招商银行/*MBUSE*003\"},{\"date\":\"2015年8月19日\",\"reason\":\"贷后管理\",\"operator\":\"浦发银行信用卡中心/*20067\"},{\"date\":\"2015年4月9日\",\"reason\":\"贷后管理\",\"operator\":\"上海浦东发展银行/*kc*-xyk\"},{\"date\":\"2015年2月14日\",\"reason\":\"贷后管理\",\"operator\":\"上海浦东发展银行/jk*x-x*k\"},"
						+ "{\"date\":\"2015年1月29日\",\"reason\":\"贷后管理\",\"operator\":\"交通银行/*re*itreport\"},{\"date\":\"2014年10月24日\"," + "\"reason\":\"信用卡审批\",\"operator\":\"中国工商银行/icbc*jk*xyh\"}]}}\"}");
		try {
			String aaa=HttpUtil.doPost(url, params, false);
			System.out.println(aaa);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
