package com.wql.cloud.basic.oss.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wql.cloud.basic.oss.service.NetRiskService;
import com.wql.cloud.basic.oss.util.HttpClientUtil;

@Service
public class NetRiskServiceImpl implements NetRiskService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	private static final String API_URL = "http://c.dun.163yun.com/api/v2/verify";
	
	@Override
	public Boolean verifyResult(String secretId, String secretKey, String captchaId, String validate) {
		if (StringUtils.isBlank(validate)) {
            return false;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("captchaId", captchaId);
        params.put("validate", validate);
        params.put("user", ""); //如果user为null会出现签名错误的问题
        params.put("secretId", secretId);
        params.put("version", "v2");
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("nonce", String.valueOf(ThreadLocalRandom.current().nextInt()));
        params.put("signature", sign(secretKey, params)); //签名
        String resp = HttpClientUtil.sendHttpPost(API_URL, params);
        return verifyRet(resp);
	}
	
	 /**
     * 生成签名信息
     *
     * @param secretKey 验证码私钥
     * @param params    接口请求参数名和参数值map，不包括signature参数名
     * @return
     */
    public static String sign(String secretKey, Map<String, String> params) {
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        StringBuffer sb = new StringBuffer();
        for (String key : keys) {
            sb.append(key).append(params.get(key));
        }
        sb.append(secretKey);
        try {
            return DigestUtils.md5Hex(sb.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();// 一般编码都支持的。。
        }
        return null;
    }
    
    /**
     * 验证返回结果
     */
    private static boolean verifyRet(String resp) {
        if (StringUtils.isNotBlank(resp)) {
        	JSONObject j = JSONObject.parseObject(resp);
            return j.getBoolean("result");
        }
        return false;
    }
	
}
