package com.wql.cloud.basic.xxlconf.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xxl.conf.core.XxlConfClient;
import com.xxl.conf.core.annotation.XxlConf;
import com.xxl.conf.core.listener.XxlConfListener;

/**
 *  测试示例
 */
@Component
public class DemoConf {

	private static final Logger logger = LoggerFactory.getLogger(DemoConf.class);
	
	@XxlConf("default.key02")
	public String paramByAnno;
	
	/**
	 * 代码中直接调用API即可:
	 * 		XxlConfClient.get("default.key02");
	 * 
	 */
	
	/**
	 * 可开发Listener逻辑，监听配置变更事件；可据此实现动态刷新JDBC连接池等高级功能；
	 */
	public void testname() throws Exception {
		XxlConfClient.addListener("default.key01", new XxlConfListener(){
		    @Override
		    public void onChange(String key, String value) throws Exception {
		        logger.info("配置变更事件通知：{}={}", key, value);
		    }
		});
	}
	
}
