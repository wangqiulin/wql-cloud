package com.wql.cloud.tool.executor;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 五种线程池的适应场景
	newCachedThreadPool：用来创建一个可以无限扩大的线程池，适用于服务器负载较轻，执行很多短期异步任务。
	newFixedThreadPool：创建一个固定大小的线程池，因为采用无界的阻塞队列，所以实际线程数量永远不会变化，适用于可以预测线程数量的业务中，或者服务器负载较重，对当前线程数量进行限制。
	newSingleThreadExecutor：创建一个单线程的线程池，适用于需要保证顺序执行各个任务，并且在任意时间点，不会有多个线程是活动的场景。
	newScheduledThreadPool：可以延时启动，定时启动的线程池，适用于需要多个后台线程执行周期任务的场景。
	newWorkStealingPool：创建一个拥有多个任务队列的线程池，可以减少连接数，创建当前可用cpu数量的线程来并行执行，适用于大耗时的操作，可以并行来执行
 * 
 * @author wangqiulin
 *
 */
@Component
public class TaskExecutorService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired
    private TaskExecutor taskExecutor;
	
	/**
	 * 异步执行无返回值
	 * 
	 * @param runnable
	 */
	public void executor(Runnable runnable) {
		taskExecutor.execute(runnable);
	}
	
	/**
	 * 异步执行有返回值
	 * 
	 * @param <V>
	 * @param callable
	 * @return
	 */
	public <V> ArrayList<V> submit(Callable<V> callable, int corePoolSize) {
		//创建ExecutorService
		ExecutorService executorService = Executors.newFixedThreadPool(corePoolSize);
		Map<Integer,Future<V>> dataMap = Maps.newHashMap();
		for (int i = 0; i < corePoolSize; i++) {
			Future<V> future = executorService.submit(callable);
			dataMap.put(i, future);
		}
		//表示不再接受新任务，但不会强行终止已经提交或者正在执行中的任务
		executorService.shutdown(); 
		//处理返回值
		ArrayList<V> list = Lists.newArrayList();
		for (int i = 0; i < corePoolSize; i++) {
			try {
				list.add(dataMap.get(i).get());
			} catch (InterruptedException | ExecutionException e) {
				logger.error("异步执行获取返回值异常", e);
			}
		}
		return list;
	}
	
	
}
