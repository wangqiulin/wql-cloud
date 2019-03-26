package com.wql.cloud.userservice.config.fegin;

import feign.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * fegin调用打印日志
 * 
       其次，在application.properties中要设定一行这样的代码：
   logging.level.<你的feign client全路径类名>: DEBUG
       这样对应的feign client就可以输出日志了。这里必须是DEBUG才能生效。
 * @return
 */
@ConditionalOnExpression("${feign.logger.enabled:true}")
@Configuration
public class FeignLogConfig {

	public final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${feign.logger.level:FULL}")
	private String level;

	@Bean
	Logger.Level feignLoggerLevel() {
		try {
			logger.info("setting feign level->{}", level);
			return Logger.Level.valueOf(level.toUpperCase());
		} catch (IllegalArgumentException e) {
			logger.warn("feign.logger.level={}无效{},启用默认level=FULL级别配置", level, Arrays.toString(Logger.Level.values()));
		}
		return Logger.Level.FULL;
	}
	
}
