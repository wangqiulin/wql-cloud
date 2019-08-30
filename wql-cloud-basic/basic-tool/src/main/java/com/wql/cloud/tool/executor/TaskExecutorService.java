package com.wql.cloud.tool.executor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class TaskExecutorService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired
    private TaskExecutor taskExecutor;
	
	/**
	 * 异步执行无返回值
	 * 
	 * @see
	 * 	 execute(..) 方法用于提交不需要返回值的任务，所以无法判断任务是否被线程池执行成功。
	 * @param task
	 */
	public void executor(Runnable task) {
		taskExecutor.execute(task);
	}
	
	/**
	 * 异步执行，有返回值
	 * 
	 * @see	 
	 * submit(..) 方法用于提交需要返回值的任务。线程池会返回一个 future 类型的对象，通过这个 future 对象可以判断任务是否执行成功，
	 * 并且可以通过 future 的 get() 方法来获取返回值，get() 方法会阻塞当前线程直到任务完成，
	 * 而使用 get（long timeout，TimeUnit unit）方法则会阻塞当前线程一段时间后立即返回，这时候有可能任务没有执行完。
	 * 
	 * @param task
	 * @return
	 */
	public Object submit(Runnable task) {
		ExecutorService executorService = (ExecutorService)taskExecutor;
		Future<?> future = executorService.submit(task);
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error("TaskExecutorService.submit", e);
		}
		return null;
	}
	
	
}
