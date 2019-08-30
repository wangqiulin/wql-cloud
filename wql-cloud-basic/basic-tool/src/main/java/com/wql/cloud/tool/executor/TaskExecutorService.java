package com.wql.cloud.tool.executor;

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
	
}
