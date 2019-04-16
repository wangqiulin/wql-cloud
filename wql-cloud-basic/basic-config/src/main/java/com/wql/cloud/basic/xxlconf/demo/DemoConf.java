package com.wql.cloud.basic.xxlconf.demo;

import org.springframework.stereotype.Component;

import com.xxl.conf.core.annotation.XxlConf;

/**
 *  测试示例
 */
@Component
public class DemoConf {

	@XxlConf("default.key02")
	public String paramByAnno;
	
}
