package com.wql.cloud.basic.datasource.config;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 * mybatis的配置
 * 
 * @author wangqiulin
 */
@Configuration
@EnableTransactionManagement
public class SessionFactoryConfig implements TransactionManagementConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(SessionFactoryConfig.class);
	
	@Autowired
	private DataSource dataSource;
	
	@Value("${mybatis.mapper.domainPackage}")
	private String domainPackage;
	
	@Value("${mybatis.mapper.xmlPackage}")
	private String xmlPackage;
	
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory createSqlSessionFactory() throws Exception {
		logger.info("【SqlSessionFactory】---开始初始化");
		
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setTypeAliasesPackage(domainPackage);
		
		//开启驼峰匹配
		org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
		config.setMapUnderscoreToCamelCase(true);
		sqlSessionFactoryBean.setConfiguration(config);
		
        if(StringUtils.isNotBlank(xmlPackage)) {
        	//添加XML目录
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources(xmlPackage));
        }
        SqlSessionFactory sessionFactory = sqlSessionFactoryBean.getObject();
        logger.info("【SqlSessionFactory】---初始化成功");
        return sessionFactory;
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}
	
}
