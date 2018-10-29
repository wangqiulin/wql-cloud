package com.wql.cloud.basic.httpclient.config;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author wangqiulin
 * @date 2018年5月13日
 */
@Configuration
public class RestTemplateConfig {

	public static final String UTF8 = "UTF-8";
	public static final String HTTP = "http";
	public static final String HTTPS = "https";
	public static final String ACCEPT_CHARSET = "Accept-Charset";
	
	@Autowired
	private HttpClientProperties httpClientProperty;
	
	@Primary
	@Bean
	public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
		return new RestTemplate(factory);
	}

	
	@Bean
	public ClientHttpRequestFactory httpComponentsClientHttpRequestFactory(HttpClientBuilder httpClientBuilder) 
			throws Exception{

        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
        	@Override
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();
        httpClientBuilder.setSSLContext(sslContext);

        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register(HTTP, PlainConnectionSocketFactory.getSocketFactory())
                .register(HTTPS, sslSocketFactory)
                .build();

        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connMgr.setMaxTotal(httpClientProperty.getMaxConnTotalInt());
        connMgr.setDefaultMaxPerRoute(httpClientProperty.getMaxConnPerRouteInt());
        httpClientBuilder.setConnectionManager(connMgr);

        CloseableHttpClient apacheClient = httpClientBuilder.build();
		return new HttpComponentsClientHttpRequestFactory(apacheClient);
	}
	
	
	@Bean
	public HttpClientBuilder httpClientBuilder(){
		RequestConfig requestConfig = RequestConfig.custom()
	            .setConnectTimeout(httpClientProperty.getConnectTimeout())
	            .setSocketTimeout(httpClientProperty.getSocketTimeout())
	            .setConnectionRequestTimeout(httpClientProperty.getConnectionRequestTimeoutInt())
	            .build();
		
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
		        .setMaxConnTotal(httpClientProperty.getMaxConnTotalInt())
		        .setMaxConnPerRoute(httpClientProperty.getMaxConnPerRouteInt())
		        .setDefaultRequestConfig(requestConfig)
		        .addInterceptorLast(new HttpRequestInterceptor() {
		            @Override
		            public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		                request.setHeader(ACCEPT_CHARSET, UTF8);
		            }
		        })
		        .disableAutomaticRetries()
		        .setRetryHandler(new HttpRequestRetryHandler() {
		        	@Override
		            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
		                if (executionCount >= 0) {
		                    return false;
		                }
		                if (exception instanceof NoHttpResponseException) {
		                    return true;
		                }
		                return false;
		            }
		        });
		return httpClientBuilder;
	}
	
	
}
