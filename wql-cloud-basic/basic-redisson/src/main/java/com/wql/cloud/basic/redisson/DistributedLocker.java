package com.wql.cloud.basic.redisson;

/**
 * 获取锁管理类
 *
 * @author wangqiulin
 * @date 2018年5月16日
 */
public interface DistributedLocker {

	/**
     * 获取锁(不等待锁的获取时间)
     * 
     * @param resourceName  锁的名称
     * @param worker 获取锁后的处理类
     * @param <T>
     * @return 处理完具体的业务逻辑要返回的数据
     * @throws UnableToAquireLockException
     * @throws Exception
     */
    <T> T lock(String resourceName, AquiredLockWorker<T> worker, int lockTime) 
    		throws UnableToAquireLockException, Exception;

    /**
     * 获取锁（可设置等待锁的获取时间）
     * 
     * @param resourceName
     * @param worker
     * @param waitTime
     * @param lockTime
     * @return
     * @throws UnableToAquireLockException
     * @throws Exception
     */
    <T> T lock(String resourceName, AquiredLockWorker<T> worker, int waitTime, int lockTime) 
    		throws UnableToAquireLockException, Exception;
	
}
