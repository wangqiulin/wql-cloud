package com.wql.cloud.tool.executor;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池参数配置
 * 
 * 使用方式：
 *  @Autowired
    private TaskExecutor taskExecutor;
 * 	
 * 	taskExecutor.execute(() -> { 线程任务 });
 * 
 */
@Configuration
@ConditionalOnExpression("${taskExecutor.enabled:true}")
public class TaskExecutorConfig {

	/**
	 * 核心线程数
	 */
	@Value("${taskExecutor.corePoolSize:5}")
	private int corePoolSize;

	/**
	 * 最大线程数
	 */
	@Value("${taskExecutor.maxPoolSize:30}")
	private int maxPoolSize;

	/**
	 * 线程池维护线程所允许的空闲时间
	 */
	@Value("${taskExecutor.keepAliveSeconds:10}")
	private int keepAliveSeconds;

	/**
	 * 队列最大长度
	 */
	@Value("${taskExecutor.queueCapacity:500}")
	private int queueCapacity;

	/**
	 * 自定义线程前缀名称
	 */
	@Value("${taskExecutor.threadNamePrefix:MyTaskExecutor}")
	private String threadNamePrefix;

	/**
	 * 参数配置
	 */
	@Bean(initMethod = "initialize", destroyMethod = "destroy")
	public ThreadPoolTaskExecutor customeTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setKeepAliveSeconds(keepAliveSeconds);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix(threadNamePrefix);
		// 线程池对拒绝任务(无线程可用)的处理策略 ThreadPoolExecutor.CallerRunsPolicy策略,调用者的线程会执行该任务,如果执行器已关闭,则丢弃
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return executor;
	}

}
