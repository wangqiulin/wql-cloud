package com.wql.cloud.basic.seata.config;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSource;

import io.seata.rm.datasource.DataSourceProxy;

@Configuration
public class DataSourceConfiguration {

	@Value("${mybatis.mapper.domainPackage:}")
	private String domainPackage;
	
	@Value("${mybatis.mapper.xmlPackage:}")
	private String xmlPackage;
	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		DruidDataSource druidDataSource = new DruidDataSource();
		return druidDataSource;
	}

	@Primary
	@Bean("dataSource")
	public DataSourceProxy dataSource(DataSource dataSource) {
		return new DataSourceProxy(dataSource);
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSourceProxy dataSourceProxy) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSourceProxy);
		// 实体类路径
		bean.setTypeAliasesPackage(domainPackage);
		// 开启驼峰匹配
		org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
		config.setMapUnderscoreToCamelCase(true);
		bean.setConfiguration(config);
		if (StringUtils.isNotBlank(xmlPackage)) {
			bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(xmlPackage));
		}
		bean.setTransactionFactory(new SpringManagedTransactionFactory());
		return bean.getObject();
	}

}
