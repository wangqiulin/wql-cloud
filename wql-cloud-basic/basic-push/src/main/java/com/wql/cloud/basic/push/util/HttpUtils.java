 package com.wql.cloud.basic.push.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * HTTP 请求工具类
 */
 public class HttpUtils {
     private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
     public static final String CHARSET_UTF8 = "UTF-8";
     public static final String CHARSET_GBK = "GBK";
     public static final String POST = "POST";
     public static final String GET = "GET";
     public static final String PUT = "PUT";

     private static final String CHARSET_DEFAULT = CHARSET_UTF8;
     
     /**
      * 
      * @param url
      * @param params
      * @return
      * @throws IOException
      */
     public static String getHttpJson(String url, Map<String, Object> params) throws IOException {
         return getHttpJson(url, params, CHARSET_DEFAULT, CHARSET_DEFAULT);
     }

     public static String postHttpJson(String url, Map<String, Object> params) throws IOException {
         return postHttpJson(url, params, CHARSET_DEFAULT, CHARSET_DEFAULT);
     }

     public static String getHttpJsonGbk(String url, Map<String, Object> params) throws IOException {
         return getHttpJson(url, params, CHARSET_GBK, CHARSET_GBK);
     }

     public static String postHttpJsonGbk(String url, Map<String, Object> params) throws IOException {
         return postHttpJson(url, params, CHARSET_GBK, CHARSET_GBK);
     }
     
     public static String getHttpJson(String url, Map<String, Object> params, String outCharset, String inCharset) throws IOException {
         return httpJson(GET, url, params, outCharset, inCharset);
     }

     public static String postHttpJson(String url, Map<String, Object> params, String outCharset, String inCharset) throws IOException {
         return httpJson(POST, url, params, outCharset, inCharset);
     }

     public static String getHttpForm(String url, Map<String, Object> params) throws IOException {
         return getHttpForm(url, params, CHARSET_DEFAULT, CHARSET_DEFAULT);
     }
     public static String getHttpForm(String url, Map<String, Object> params, String outCharset, String inCharset) throws IOException {
         return httpForm(GET, url, params, outCharset, inCharset);
     }
     
     public static String postHttpForm(String url, Map<String, Object> params) throws IOException {
         return postHttpForm(url, params, CHARSET_DEFAULT, CHARSET_DEFAULT);
     }
     
     public static String postHttpForm(String url, Map<String, Object> params, String outCharset, String inCharset) throws IOException {
         return httpForm(POST, url, params, outCharset, inCharset);
     }
     
     
     
     /**
      * http json 请求
      * @param method
      * @param u
      * @param m
      * @param outCharset
      * @param inCharset
      * @return
      * @throws IOException 
      */
     public static String httpJson(String method, String u, Map<String, Object> m, String outCharset, String inCharset) throws IOException {
    	 StringBuffer result = new StringBuffer();
         if (null != m && GET.equals(method)) {
             u += "?";
             Set<String> set = m.keySet();
             for (String key: set) {
                 u+=key+"="+m.get(key)+"&";
             }
             u = u.substring(0, u.lastIndexOf("&"));
             if (log.isDebugEnabled()) {
                 log.debug("GET http {}", u);
             }
         }
         URL url = new URL(u);
         HttpURLConnection http = (HttpURLConnection) url.openConnection();
         http.setRequestProperty("Accept", "application/json;charset="+inCharset);
         http.setRequestProperty("Content-Type", "application/json;charset="+inCharset);
         http.setReadTimeout(30000);
         http.setConnectTimeout(10000);
         http.setRequestMethod(method);
         http.setDefaultUseCaches(false);
         http.setDoInput(true);
         if (POST.equals(method) || PUT.equals(method)) {
             http.setDoOutput(true);
             OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream(), outCharset);
             String jsonStr = JSONObject.toJSONString(m);
             if (log.isDebugEnabled()) {
                 log.debug("POST HTTP {}", u);
                 log.debug("params: {}",  jsonStr);
             }
             out.write(jsonStr);
             out.flush();
             out.close();

         }
         BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), inCharset));
         String line = null;
         while((line = reader.readLine()) != null) {
             result.append(line);
         }
         if (log.isDebugEnabled()) {
             log.debug("result: {}", result.toString());
         }
         
         reader.close();
         return result.toString();
     }

     /**
      * http form 请求
      * @param method
      * @param u
      * @param m
      * @param outCharset
      * @param inCharset
      * @return
      * @throws IOException 
      */
     public static String httpForm(String method, String u, Map<String, Object> m, String outCharset, String inCharset) throws IOException {
         StringBuffer result = new StringBuffer();
         String params = "";
         Set<String> set = m.keySet();
         for (String key: set) {
             params+=key+"="+m.get(key)+"&";
         }
         params = params.substring(0, params.lastIndexOf("&"));
         
         if (GET.equals(method)) {
             u += "?" + params;
             if (log.isDebugEnabled()) {
                 log.debug("GET HTTP {}", u);
             }
         } else {
             if (log.isDebugEnabled()) {
                 log.debug("POST HTTP {}", u);
                 log.debug("params: {}", params);
             }
         }
         
         URL url = new URL(u);
         HttpURLConnection http = (HttpURLConnection) url.openConnection();
         http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset="+outCharset);
         http.setReadTimeout(30000);
         http.setConnectTimeout(10000);
         http.setRequestMethod(method);
         http.setDefaultUseCaches(false);
         http.setDoInput(true);
         http.setDoOutput(true);

         OutputStream out = http.getOutputStream();
         
         out.write(params.getBytes(outCharset));
         out.flush();
         out.close();

         BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), inCharset));

         String line = null;
         while((line = reader.readLine()) != null) {
             result.append(line);
         }
         
         reader.close();
         if (log.isDebugEnabled()) {
             log.debug("result: {}", result.toString());
         }
         return result.toString();
     }
     
     
     /**
      * 认证头 http 请求
      * @param surl
      * @param param
      * @param tokenType
      * @param accessToken
      * @return
      * @throws IOException
      */
     public static String authedJsonPost(String surl,String param, String ...headers) throws IOException{
         String result = "";
         StringBuffer sb = new StringBuffer("");
         HttpURLConnection connection = null;
         BufferedReader reader = null;
         try {
             //创建连接
             URL url = new URL(surl);
             connection = (HttpURLConnection) url.openConnection();
             connection.setConnectTimeout(1000);
             connection.setReadTimeout(2000);
             connection.setDoOutput(true);
             connection.setDoInput(true);
             connection.setRequestMethod(POST);
             connection.setUseCaches(false);
//           connection.setInstanceFollowRedirects(true);
             connection.setRequestProperty("Content-Type","application/json");
             for (int i=0; i*2<headers.length; i++) {
                 connection.setRequestProperty(headers[i], headers[i+1]);
             }
             connection.connect();

             //POST请求
             OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), CHARSET_DEFAULT);
             if (log.isDebugEnabled()) {
                 log.debug("POST HTTP {}", url);
                 log.debug("params: {}", param);
             }
             out.write(param);
             out.flush();
             out.close();

             //读取响应
             reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET_DEFAULT));

             while ((result = reader.readLine()) != null) {
                 sb.append(result);
             }
             if (log.isDebugEnabled()) {
                 log.debug("result: {}", result);
             }
         
         } finally {
             if (reader != null) {
                 try {
                     reader.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
             if (connection != null) {
                 connection.disconnect();
             }

         }
         return sb.toString();

     }
     
     public static <T> T getHttpForm(String url, BaseDTO<T> data, String outCharset, String inCharset) throws IOException {
         return httpForm(GET, url, data, outCharset, inCharset);
     }
     
     public static <T> T getHttpForm(String url, BaseDTO<T> data) throws IOException {
         return getHttpForm(url, data, CHARSET_DEFAULT, CHARSET_DEFAULT);
     }
     
     public static <T> T postHttpForm(String url, BaseDTO<T> data) throws IOException {
         return postHttpForm(url, data, CHARSET_DEFAULT, CHARSET_DEFAULT);
     }
     
     public static <T> T postHttpForm(String url, BaseDTO<T> data, String outCharset, String inCharset) throws IOException {
         return httpForm(POST, url, data, outCharset, inCharset);
     }
     
     /**
      * 
      * @param method
      * @param u
      * @param data
      * @param outCharset
      * @param inCharset
      * @return
      * @throws IOException
      */
     public static <T> T httpForm(String method, String u, BaseDTO<T> data, String outCharset, String inCharset) throws IOException {
         StringBuffer result = new StringBuffer();
         String params = data.serialize();
         
         if (GET.equals(method)) {
             u += "?" + params;
             if (log.isDebugEnabled()) {
                 log.debug("GET HTTP {}", u);
             }
         } else {
             if (log.isDebugEnabled()) {
                 log.debug("POST HTTP {}", u);
                 log.debug("params: {}", params);
             }
         }
         
         URL url = new URL(u);
         HttpURLConnection http = (HttpURLConnection) url.openConnection();
         http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset="+outCharset);
         http.setReadTimeout(30000);
         http.setConnectTimeout(10000);
         http.setRequestMethod(method);
         http.setDefaultUseCaches(false);
         http.setDoInput(true);
         http.setDoOutput(true);

         OutputStream out = http.getOutputStream();
         out.write(params.getBytes(outCharset));
         out.flush();
         out.close();

         BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), inCharset));
         String line = null;
         while((line = reader.readLine()) != null) {
             result.append(line);
         }
         
         reader.close();
         if (log.isDebugEnabled()) {
             log.debug("result: {}", result.toString());
         }
         return data.unserialize(result.toString());
     }
     
     /**
      * 传输对象抽象基础类
      * 可作为 HttpUtils 工具类 HTTP 请求时直接参数
      * 需用户自己继承实现 serialize() 与 unserialize() 两个方法
      * serialize() : 请求时参数序列化后的字符串
      * unserialize() : 接收请求结果的反序列化为对象 T
      * @author lujingjing
      * @date 2018年3月13日
      */
     public static abstract class BaseDTO<T> implements Serializable {
         /**
         *
         */
        private static final long serialVersionUID = 1L;

        /**
         * 请求参数序列化字符串
         * 如 
         *  1、表单提交格式: 
         *                  username=xxx&password=yyy
         *  2、json格式: 
         *                  {
         *                      "username": "xxx",
         *                      "password": "yyy"
         *                   }
         *  3、xml格式：
         *                  <?xml version='1.0' encoding='UTF-8' standalone='yes'?>
         *                  <root>
         *                      <username>xxx</username>
         *                      <password>yyy</password>
         *                  </root>
         *   ...
         * @return
         */
         protected abstract String serialize();
         
         /**
          * 请求返回结果，格式化为对象 T
          * @param result
          * @return
          */
         protected abstract T unserialize(String result);
         
     }
        
}
