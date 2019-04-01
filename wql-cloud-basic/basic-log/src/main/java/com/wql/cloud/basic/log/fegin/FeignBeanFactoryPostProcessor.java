package com.wql.cloud.basic.log.fegin;

import java.util.Arrays;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * feign方式宕机优雅响应
 */
@Component
public class FeignBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if (containsBeanDefinition(beanFactory, "feignContext", "eurekaAutoServiceRegistration")) {
			BeanDefinition bd = beanFactory.getBeanDefinition("feignContext");
			bd.setDependsOn("eurekaAutoServiceRegistration");
		}
	}

	private boolean containsBeanDefinition(ConfigurableListableBeanFactory beanFactory, String... beans) {
		return Arrays.stream(beans).allMatch(b -> beanFactory.containsBeanDefinition(b));
	}

}