package com.wql.cloud.serviceapp.user.config.tk;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wql.cloud.basic.datasource.config.SessionFactoryConfig;

import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * 【通用mapper】所需要的配置, 必须在SessionFactoryConfig注册后再加载MapperScannerConfigurer，否则会报错
 * 
 * @author wangqiulin
 * @date 2018年3月5日
 */
@Configuration
@AutoConfigureAfter(SessionFactoryConfig.class)
public class MyBatisMapperScannerConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(MyBatisMapperScannerConfig.class);

	@Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
		logger.info("【通用mapper】---启用");
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.wql.cloud.serviceapp.user.mapper");
        
        //初始化扫描器的相关配置，这里我们要创建一个Mapper的父类
        Properties properties = new Properties();
        properties.setProperty("mappers", "com.wql.cloud.basic.datasource.tk.MyMapper");
        properties.setProperty("notEmpty", "false");
        properties.setProperty("IDENTITY", "MYSQL");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }

	
}
