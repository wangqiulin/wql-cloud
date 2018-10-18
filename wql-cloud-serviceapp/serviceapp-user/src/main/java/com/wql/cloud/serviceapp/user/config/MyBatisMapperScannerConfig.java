package com.wql.cloud.serviceapp.user.config;

import java.util.Properties;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * 通用mapper所需要的配置
 * 必须在SessionFactoryConfig注册后再加载MapperScannerConfigurer，否则会报错
 * @author wangqiulin
 * @date 2018年3月5日
 */
@Configuration
@AutoConfigureAfter(SessionFactoryConfig.class)  
public class MyBatisMapperScannerConfig {

	@Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.wql.cloud.serviceapp.user.mapper");
        
        //初始化扫描器的相关配置，这里我们要创建一个Mapper的父类
        Properties properties = new Properties();
        properties.setProperty("mappers", "com.wql.cloud.basic.datasource.mybatis.MyMapper");
        properties.setProperty("notEmpty", "false");
        properties.setProperty("IDENTITY", "MYSQL");
        
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }

	
}
