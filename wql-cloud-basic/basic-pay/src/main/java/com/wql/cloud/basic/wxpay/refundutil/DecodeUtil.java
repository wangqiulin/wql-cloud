package com.wql.cloud.basic.wxpay.refundutil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;

public class DecodeUtil {
	
	/**
	 * key路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
	 */
	@Value("${wxpay.privateKey:}")
	private static String privateKey;
	
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";
    
    /**
     * 生成key
     */
    private static SecretKeySpec key = new SecretKeySpec(SecureUtil.md5(privateKey).toLowerCase().getBytes(), ALGORITHM);
 
    /**
     * AES加密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptData(String data) throws Exception {
        // 创建密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        // 初始化
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.encode(cipher.doFinal(data.getBytes()));
    }
 
    /**
     * AES解密
     * 
     * @param base64Data
     * @return
     * @throws Exception
     */
    public static String decryptData(String base64Data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.decode(base64Data)));
    }
    
}    