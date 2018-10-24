package com.wql.cloud.basic.datasource.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @auther wangqiulin
 * @date 2018/10/11
 */
@Configuration
@ConfigurationProperties(prefix = "mybatis.mapper")
public class MybatisProperties {

	private String domainPackage;

	private String xmlPackage;

	public String getDomainPackage() {
		return domainPackage;
	}

	public void setDomainPackage(String domainPackage) {
		this.domainPackage = domainPackage;
	}

	public String getXmlPackage() {
		return xmlPackage;
	}

	public void setXmlPackage(String xmlPackage) {
		this.xmlPackage = xmlPackage;
	}

}
