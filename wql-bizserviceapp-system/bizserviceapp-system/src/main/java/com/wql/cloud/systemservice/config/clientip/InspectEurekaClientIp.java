package com.wql.cloud.systemservice.config.clientip;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * SpringCloud注册正确的IP - Eureka使用经验:
 * 	https://www.jianshu.com/p/93f29178340a
 * 
 * @author wangqiulin
 *
 */
@Component
public class InspectEurekaClientIp implements BeanPostProcessor, EnvironmentPostProcessor, Ordered {

	private static final Logger logger = LoggerFactory.getLogger(InspectEurekaClientIp.class);
	
    @Override
    public int getOrder() {
    	//这里是依据HostInfoEnvironmentPostProcessor 的优先级，这里让本类优先处理。
        return ConfigFileApplicationListener.DEFAULT_ORDER - 2;
    }

    /**
     * 鉴于 HostInfoEnvironmentPostProcessor 处理inetUtilsProperties 时，
     * 只从系统环境变量中取得空的  ignoredInterfaces.
     * 这里强制写入，防止被传入了错误的VMware的网卡的IP地址
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object>  ignoredInterfaces = Maps.newHashMap();
        ignoredInterfaces.put("spring.cloud.inetutils.ignoredInterfaces", Lists.newArrayList("VMware.*"));
        SystemEnvironmentPropertySource systemEnvironmentPropertySource = new SystemEnvironmentPropertySource("systemEnvironment", ignoredInterfaces);
        environment.getPropertySources().addLast(systemEnvironmentPropertySource);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("inetUtilsProperties")) {
            InetUtilsProperties inetUtilsProperties = (InetUtilsProperties) bean;
            logger.warn("ignored interfaces {}", inetUtilsProperties.getIgnoredInterfaces());
            logger.warn("prefered network {}", inetUtilsProperties.getPreferredNetworks());
            return inetUtilsProperties;
        }
        if (beanName.equals("eurekaInstanceConfigBean")) {
            EurekaInstanceConfigBean eurekaInstanceConfigBean = (EurekaInstanceConfigBean) bean;
            logger.warn("eurekaInstanceConfig Ip: {}", eurekaInstanceConfigBean.getIpAddress());
            logger.warn("eurekaInstanceConfig IstanceId: {}", eurekaInstanceConfigBean.getInstanceId());
            return bean;
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    
}
