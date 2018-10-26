package com.wql.cloud.basic.encrypt.util;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.util.Assert;

/**
 * mysql aes加密工具类
 * @author wangqiulin
 *
 */
public class MySqlAesUtil {

    private static final Logger logger = LoggerFactory.getLogger(MySqlAesUtil.class);

    private static final String ENCRYPT_INSTANCE = "AES/ECB/PKCS5Padding";
    private static final String ENCRYPT_TYPE = "AES";
    private static final String CHARSET = "UTF-8";
    private static final int KEY_LENGTH = 16;

    /**
     * 加密<b>推荐使用encrypt(content,encryptCipher)</b>
     *
     * @param content 明文内容
     * @param aesKey  密钥
     * @return
     * @deprecated
     */
    public static String encrypt(String content, String aesKey) {
        try {
            if (StringUtils.isBlank(content)) {
                return content;
            }
            Cipher cipher = getEncryptCipher(aesKey);
            byte[] encryptBytes = cipher.doFinal(content.getBytes(CHARSET));
            return new String(Hex.encode(encryptBytes));
        } catch (Exception e) {
            logger.error("aes 加密失败", e);
        }
        return content;
    }

    /**
     * 解密<b>推荐使用encrypt(content,decryptCipher)</b>
     *
     * @param content 密文内容
     * @param aesKey  密钥
     * @return
     * @deprecated
     */
    public static String decrypt(String content, String aesKey) {
        try {
            if (StringUtils.isBlank(content)) {
                return content;
            }
            Cipher cipher = getDecryptCipher(aesKey);
            byte[] result = cipher.doFinal(Hex.decode(content));
            return new String(result, CHARSET);
        } catch (Exception e) {
            logger.error("aes 解密失败", e);
        }
        return content;
    }

    /**
     * 加密
     *
     * @param content       明文内容
     * @param encryptCipher 加密器
     * @return
     */
    public static String encrypt(String content, Cipher encryptCipher) {
        try {
            if (StringUtils.isBlank(content)) {
                return content;
            }
            byte[] encryptBytes = encryptCipher.doFinal(content.getBytes(CHARSET));
            return new String(Hex.encode(encryptBytes));
        } catch (Exception e) {
            logger.error("aes 加密失败", e);
        }
        return content;
    }

    /**
     * 解密
     *
     * @param content       密文内容
     * @param decryptCipher 解密器
     * @return
     */
    public static String decrypt(String content, Cipher decryptCipher) {
        try {
            if (StringUtils.isBlank(content)) {
                return content;
            }
            byte[] result = decryptCipher.doFinal(Hex.decode(content));
            return new String(result, CHARSET);
        } catch (Exception e) {
            logger.error("aes 解密失败", e);
        }
        return content;
    }

    /**
     * 获取加密Cipher
     *
     * @return
     */
    public static Cipher getEncryptCipher(String aesKey) {
        try {
            Key key = getKey(aesKey);
            Cipher cipher = Cipher.getInstance(ENCRYPT_INSTANCE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher;
        } catch (Exception e) {
            logger.error("加密器创建失败", e);
        }
        return null;
    }

    /**
     * 获取解密Cipher
     *
     * @return
     */
    public static Cipher getDecryptCipher(String aesKey) {
        try {
            Key key = getKey(aesKey);
            Cipher cipher = Cipher.getInstance(ENCRYPT_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher;
        } catch (Exception e) {
            logger.error("解密器创建失败", e);
        }
        return null;
    }

    /**
     * 获取密钥对象
     *
     * @param aesKey 密钥内容
     * @return
     */
    private static Key getKey(String aesKey) throws UnsupportedEncodingException {
        Assert.isTrue(StringUtils.isNotBlank(aesKey), "加密密钥不能为空");
        Assert.isTrue(aesKey.length() <= KEY_LENGTH, "加密密钥长度不能大于" + KEY_LENGTH);
        byte[] keyBytes = Arrays.copyOf(aesKey.getBytes(CHARSET), KEY_LENGTH);
        Key key = new SecretKeySpec(keyBytes, ENCRYPT_TYPE);
        return key;
    }

}
