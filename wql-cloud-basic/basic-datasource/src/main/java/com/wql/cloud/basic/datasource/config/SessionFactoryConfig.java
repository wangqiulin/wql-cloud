package com.wql.cloud.basic.datasource.config;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.wql.cloud.basic.datasource.properties.MybatisProperties;

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
	
	@Autowired
	private MybatisProperties mybatisProperties;
	
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory createSqlSessionFactory() throws Exception {
		logger.info("【SqlSessionFactory】---开始初始化");
		
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setTypeAliasesPackage(mybatisProperties.getDomainPackage());
		
		//开启驼峰匹配
		org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
		config.setMapUnderscoreToCamelCase(true);
		sqlSessionFactoryBean.setConfiguration(config);
		
		//添加分页插件 
        /*PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        //分页 查询总数不返回大于页码的数据(false);  查询总数如果页码大于最大页，返回最后一页数据(true)
        properties.setProperty("reasonable", "false"); 
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});*/
        
        if(StringUtils.isNotBlank(mybatisProperties.getXmlPackage())) {
        	//添加XML目录
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources(mybatisProperties.getXmlPackage()));
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
