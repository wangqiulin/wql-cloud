package com.wql.cloud.tool.dbencrypt;


import javax.annotation.PostConstruct;
import javax.crypto.Cipher;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;


/**
 * MySqlAesUtil的实现, 默认数据库加密实现
 * @author wangqiulin
 */
@Component
@ConditionalOnExpression("'${database.encrypt.type:MySqlAES}'.equalsIgnoreCase('MySqlAES')")
public class MySqlAESEncryptServiceImpl implements DBEncryptService {

    private static final Logger logger = LoggerFactory.getLogger(MySqlAESEncryptServiceImpl.class);

    @Value("${database.encrypt.key:}")
    private String databaseEncryptKey;

    private Cipher encCipher;

    private Cipher decCipher;

    @PostConstruct
    public void initCipher(){
    	if(StringUtils.isEmpty(databaseEncryptKey)){
    		return ;
    	}
        logger.info("【MySqlAesEncryptServiceImpl---初始化加密解密器，开始】");
        if(databaseEncryptKey.length() > 16){
            logger.warn("mysqlAes密钥设置过长,系统默认截取前16位");
            databaseEncryptKey = databaseEncryptKey.substring(0,16);
        }
        encCipher = MySqlAESUtil.getEncryptCipher(databaseEncryptKey);
        decCipher = MySqlAESUtil.getDecryptCipher(databaseEncryptKey);
        logger.info("【MySqlAesEncryptServiceImpl---初始化加密解密器，完成】");
    }

    @Override
    public String encrypt(String content) {
        return MySqlAESUtil.encrypt(content,encCipher);
    }

    @Override
    public String decrypt(String content) {
        return MySqlAESUtil.decrypt(content,decCipher);
    }

    @Override
    public String getEncryptKey() {
        return this.databaseEncryptKey;
    }


}

