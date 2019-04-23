package com.wql.cloud.tool.dbencrypt.service.impl;


import javax.annotation.PostConstruct;
import javax.crypto.Cipher;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import com.wql.cloud.tool.dbencrypt.service.DBEncryptService;
import com.wql.cloud.tool.dbencrypt.util.MySqlAESUtil;


/**
 * MySqlAesUtil的实现, 默认数据库加密实现
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
        logger.info("【MySqlAES---加密解密器，初始化开始】");
        if(databaseEncryptKey.length() > 16){
            logger.warn("mysqlAes密钥设置过长,系统默认截取前16位");
            databaseEncryptKey = databaseEncryptKey.substring(0,16);
        }
        encCipher = MySqlAESUtil.getEncryptCipher(databaseEncryptKey);
        decCipher = MySqlAESUtil.getDecryptCipher(databaseEncryptKey);
        logger.info("【MySqlAES---加密解密器，初始化完成】");
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

