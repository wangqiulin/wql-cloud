package com.wql.cloud.basic.risk.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HtpClient4.0封装
 */
public class HttpClientUtil {

    public static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static final Integer HTTP_TIMEOUT_IN_MS = 10000;
	
	public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    
    private static volatile CloseableHttpClient httpClient;

    private static RequestConfig requestConfig = null;

    private HttpClientUtil() {

    }

    public static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (CloseableHttpClient.class) {
                if (httpClient == null) {
                    ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
                    Registry<ConnectionSocketFactory> socketFactoryRegistry
                        = RegistryBuilder.<ConnectionSocketFactory>create().register("http", plainsf)
                            .register("https", SSLConnectionSocketFactory.getSystemSocketFactory()).build();
                    HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory
                        = new ManagedHttpClientConnectionFactory(DefaultHttpRequestWriterFactory.INSTANCE,
                            DefaultHttpResponseParserFactory.INSTANCE);
                    DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;
                    PoolingHttpClientConnectionManager connManager
                        = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connFactory, dnsResolver);
                    SocketConfig defaultSocketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
                    connManager.setDefaultSocketConfig(defaultSocketConfig);
                    connManager.setMaxTotal(500);
                    connManager.setDefaultMaxPerRoute(100);
                    connManager.setValidateAfterInactivity(1 * 1000);
                    requestConfig = RequestConfig.custom()
                        .setSocketTimeout(240000)
                        .setConnectTimeout(3000)
                        .setConnectionRequestTimeout(3000)
                        .build();
                    httpClient = HttpClients.custom().setConnectionManager(connManager)
                        .setConnectionManagerShared(false).evictExpiredConnections()
                        .evictIdleConnections(10, TimeUnit.SECONDS).setDefaultRequestConfig(requestConfig)
                        .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
                        .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                        // .setRetryHandler(new ExceptionRetryHandler(10))
                        .build();
                    Thread closeThread = new IdleConnectionMonitorThread(connManager);
                    closeThread.setDaemon(true);
                    closeThread.start();
                }
            }
        }
        return httpClient;
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param jsonStr 参数(格式:json)
     */
    public static String sendHttpPost(String httpUrl, String jsonStr, Header... headers) {
        return sendHttpPost(httpUrl, jsonStr, 0L, null, headers);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param jsonStr 参数(格式:json)
     */
    public static String sendHttpPostByRetry(String httpUrl, String jsonStr, int retryCount, Header... headers) {
        return sendHttpPostByRetry(httpUrl, jsonStr, 0L, null, retryCount, headers);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     */
    public static String sendHttpPost(String httpUrl, long timeout, TimeUnit timeUnit, Header... header) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        if (header != null) {
            httpPost.setHeaders(header);
        }
        return sendHttpPost(httpPost, timeout, timeUnit);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     */
    public static String sendHttpPostByRetry(String httpUrl, long timeout, TimeUnit timeUnit, int retryCount,
        Header... header) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        if (header != null) {
            httpPost.setHeaders(header);
        }
        return sendHttpPostByRetry(httpPost, timeout, timeUnit, retryCount);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     */
    public static String sendHttpPost(String httpUrl, Header... headers) {
        return sendHttpPost(httpUrl, 0L, null, headers);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     */
    public static String sendHttpPostByRetry(String httpUrl, int retyCount, Header... headers) {
        return sendHttpPostByRetry(httpUrl, 0L, null, retyCount, headers);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param jsonStr 参数(格式:json)
     */
    public static String sendHttpPost(String httpUrl, String jsonStr, long timeout, TimeUnit timeUnit,
        Header... header) {
        return sendHttpPostByRetry(httpUrl, jsonStr, timeout, timeUnit, 0, header);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param jsonStr 参数(格式:json)
     */
    public static String sendHttpPostByRetry(String httpUrl, String jsonStr, long timeout, TimeUnit timeUnit,
        int retryCount, Header... header) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            // 设置参数
            StringEntity stringEntity = new StringEntity(jsonStr, DEFAULT_CHARSET_NAME);
            stringEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            httpPost.setEntity(stringEntity);
            if (header != null) {
                httpPost.setHeaders(header);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return sendHttpPostByRetry(httpPost, timeout, timeUnit, retryCount);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps 参数
     */
    public static String sendHttpPost(String httpUrl, Map<String, String> maps, Header... headers) {
        return sendHttpPost(httpUrl, maps, 0L, null, headers);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps 参数
     */
    public static String sendHttpPostByRetry(String httpUrl, Map<String, String> maps, int retryCount,
        Header... headers) {
        return sendHttpPostByRetry(httpUrl, maps, 0L, null, retryCount, headers);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param parameters 参数
     */
    public static String sendHttpPost(String httpUrl, Map<String, String> parameters, long timeout, TimeUnit timeUnit,
        Header... header) {
        return sendHttpPostByRetry(httpUrl, parameters, timeout, timeUnit, 0, header);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param parameters 参数
     */
    public static String sendHttpPostByRetry(String httpUrl, Map<String, String> parameters, long timeout,
        TimeUnit timeUnit, int retryCount, Header... header) {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), parameters.get(entry.getKey())));
        }
        try {
            if (header != null) {
                httpPost.setHeaders(header);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, DEFAULT_CHARSET_NAME));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return sendHttpPostByRetry(httpPost, timeout, timeUnit, retryCount);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param parameters 参数
     */
    public static String sendHttpPost(String httpUrl, Map<String, String> parameters) {
        return sendHttpPost(httpUrl, parameters, 0L, null);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param parameters 参数
     */
    public static String sendHttpPostByRetry(String httpUrl, Map<String, String> parameters, int retryCount) {
        return sendHttpPostByRetry(httpUrl, parameters, 0L, null, retryCount);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps 参数
     */
    public static String sendHttpPut(String httpUrl, Map<String, String> maps, Header... headers) {
        return sendHttpPut(httpUrl, maps, 0L, null, headers);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps 参数
     */
    public static String sendHttpPutByRetry(String httpUrl, Map<String, String> maps, int retryCount,
        Header... headers) {
        return sendHttpPutByRetry(httpUrl, maps, 0L, null, retryCount, headers);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param parameters 参数
     */
    public static String sendHttpPut(String httpUrl, Map<String, String> parameters, long timeout, TimeUnit timeUnit,
        Header... headers) {
        return sendHttpPutByRetry(httpUrl, parameters, timeout, timeUnit, 0, headers);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param parameters 参数
     */
    public static String sendHttpPutByRetry(String httpUrl, Map<String, String> parameters, long timeout,
        TimeUnit timeUnit, int retryCount, Header... headers) {
        HttpPut httpPut = new HttpPut(httpUrl);// 创建httpPost
        if (headers != null) {
            httpPut.setHeaders(headers);
        }
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), parameters.get(entry.getKey())));
        }
        try {
            httpPut.setEntity(new UrlEncodedFormEntity(nameValuePairs, DEFAULT_CHARSET_NAME));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return sendHttpRequestByRetry(httpPut, timeout, timeUnit, retryCount);
    }

    /**
     * 发送 put请求
     *
     * @param httpUrl 地址
     * @param jsonStr 参数(格式:json)
     */
    public static String sendHttpPut(String httpUrl, String jsonStr, Header... header) {
        return sendHttpPut(httpUrl, jsonStr, 0L, null, header);
    }

    /**
     * 发送 put请求
     *
     * @param httpUrl 地址
     * @param jsonStr 参数(格式:json)
     */
    public static String sendHttpPutByRetry(String httpUrl, String jsonStr, int retryCount, Header... header) {
        return sendHttpPutByRetry(httpUrl, jsonStr, 0L, null, retryCount, header);
    }

    /**
     * 发送 put请求
     *
     * @param httpUrl 地址
     * @param jsonStr 参数 json
     */
    public static String sendHttpPut(String httpUrl, String jsonStr, long timeout, TimeUnit timeUnit,
        Header... header) {
        return sendHttpPutByRetry(httpUrl, jsonStr, timeout, timeUnit, 0, header);
    }

    /**
     * 发送 put请求
     *
     * @param httpUrl 地址
     * @param jsonStr 参数 json
     */
    public static String sendHttpPutByRetry(String httpUrl, String jsonStr, long timeout, TimeUnit timeUnit,
        int retryCount, Header... header) {
        HttpPut httpPut = new HttpPut(httpUrl);
        try {
            // 设置参数
            StringEntity stringEntity = new StringEntity(jsonStr, DEFAULT_CHARSET_NAME);
            stringEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            httpPut.setEntity(stringEntity);
            if (header != null) {
                httpPut.setHeaders(header);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return sendHttpRequestByRetry(httpPut, timeout, timeUnit, retryCount);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param headers 参数
     */
    public static String sendHttpDelete(String httpUrl, Header... headers) {
        return sendHttpDeleteByRetry(httpUrl, 0, headers);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param headers 参数
     */
    public static String sendHttpDeleteByRetry(String httpUrl, int retryCount, Header... headers) {
        HttpDelete httpDelete = new HttpDelete(httpUrl);// 创建httpDete
        if (headers != null) {
            httpDelete.setHeaders(headers);
        }
        return sendHttpRequestByRetry(httpDelete, 0L, null, retryCount);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param timeout timeout
     * @param headers 参数
     */
    public static String sendHttpDelete(String httpUrl, long timeout, TimeUnit timeUnit, Header... headers) {
        return sendHttpDeleteByRetry(httpUrl, timeout, timeUnit, 0, headers);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param timeout timeout
     * @param headers 参数
     */
    public static String sendHttpDeleteByRetry(String httpUrl, long timeout, TimeUnit timeUnit, int retryCount,
        Header... headers) {
        HttpDelete httpDelete = new HttpDelete(httpUrl);// 创建httpDete
        if (headers != null) {
            httpDelete.setHeaders(headers);
        }
        return sendHttpRequestByRetry(httpDelete, timeout, timeUnit, retryCount);
    }

    /**
     * 发送Post请求
     */
    private static String sendHttpPost(HttpPost httpPost, long timeout, TimeUnit timeUnit) {
        return HttpClientUtil.sendHttpRequest(httpPost, timeout, timeUnit);
    }

    /**
     * 发送Post请求
     */
    private static String sendHttpPostByRetry(HttpPost httpPost, long timeout, TimeUnit timeUnit, int retryCount) {
        return HttpClientUtil.sendHttpRequestByRetry(httpPost, timeout, timeUnit, retryCount);
    }

    /**
     * 发送 get请求
     */
    public static String sendHttpGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpGet(httpGet, 0L, null);
    }

    /**
     * 不带header发送 get请求
     */
    public static String sendHttpGet(String httpUrl, long timeout, TimeUnit timeUnit) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpGet(httpGet, timeout, timeUnit);
    }

    /**
     * 发送 get请求
     */
    public static String sendHttpGet(String httpUrl, Header... headers) {
        return sendHttpGet(httpUrl, 0L, null, headers);
    }

    /**
     * 发送 get请求
     */
    public static String sendHttpGet(String httpUrl, long timeout, TimeUnit timeUnit, Header... headers) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        if (headers != null) {
            httpGet.setHeaders(headers);
        }
        return sendHttpGet(httpGet, timeout, timeUnit);
    }

    /**
     * 发送Get请求
     */
    private static String sendHttpGet(HttpGet httpGet, long timeout, TimeUnit timeUnit) {
        return sendHttpRequestByRetry(httpGet, timeout, timeUnit, 3);
    }

    /**
     * 发送Get请求Https
     */
    @SuppressWarnings("unused")
	private static String sendHttpsGet(HttpGet httpGet, long timeout, TimeUnit timeUnit) {
        return sendHttpRequestByRetry(httpGet, timeout, timeUnit, 3);
    }

    public static String sendHttpRequest(HttpRequestBase httpRequestBase, long timeout, TimeUnit timeUnit) {
        return sendHttpRequestByRetry(httpRequestBase, timeout, timeUnit, 0);
    }

    public static String sendHttpRequestByRetry(HttpRequestBase httpRequestBase, long timeout, TimeUnit timeUnit,
        final int retryCount) {
        int i = 0;
        String result = null;
        while (i <= retryCount) {
            try {
                result = execute(httpRequestBase, timeout, timeUnit);
                return result;
            } catch (NoHttpResponseException e) {
                if (i == retryCount) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            } catch (Exception e) {
                logger.error("wisdomframeworkExceptionLogHttp " + e.getMessage(), e);
                new RuntimeException(e.getMessage(), e);
            }
            i++;
            logger.info("HttpClient retrycount:{}", i);
        }
        return result;
    }

    public static String execute(HttpRequestBase httpRequestBase, long timeout, TimeUnit timeUnit) throws Exception {
        // long startTimeNano = System.nanoTime();
        if (httpRequestBase == null) {
            throw new RuntimeException("httpRequestBase is null!");
        }
        CloseableHttpResponse response = null;
        String responseContent = null;
        // String url = null;
        try {
            // url = httpRequestBase.getURI().toString();
            RequestConfig config = requestConfig;
            if (timeout > 0L && timeUnit != null) {
                int timeoutInMS = Math.toIntExact(TimeUnit.MILLISECONDS.convert(timeout, timeUnit));
                config = RequestConfig.custom().setSocketTimeout(timeoutInMS).setConnectTimeout(timeoutInMS)
                    .setConnectionRequestTimeout(timeoutInMS).build();
            }
            httpRequestBase.setConfig(config);
            response = getHttpClient().execute(httpRequestBase);
            if (response != null) {
                HttpEntity entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, DEFAULT_CHARSET_NAME);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode >= 400) {
                    throw new RuntimeException("httpStatus: " + statusCode + ", result: " + responseContent);
                }
            }
            return responseContent;
        } finally {
            closeResources(response, httpRequestBase);
        }
    }

    /**
     * 关闭资源
     * 
     * @param httpResponse
     * @param httpRequestBase
     */
    private static void closeResources(CloseableHttpResponse httpResponse, HttpRequestBase httpRequestBase) {
        try {
            if (httpResponse != null) {
                httpResponse.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        try {
            if (httpRequestBase != null) {
                httpRequestBase.releaseConnection();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
