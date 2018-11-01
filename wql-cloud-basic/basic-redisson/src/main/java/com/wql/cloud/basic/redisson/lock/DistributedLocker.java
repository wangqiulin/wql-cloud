package com.wql.cloud.basic.redisson.lock;

import java.util.concurrent.TimeUnit;

import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;

import com.wql.cloud.basic.redisson.res.LockResult;
import com.wql.cloud.basic.redisson.res.MultiLockResult;
import com.wql.cloud.basic.redisson.res.ReadWriteLockResult;

/**
 * @auther wangqiulin
 * @date 2018/10/19
 */
public interface DistributedLocker {

	//================加锁===============//
	
	/**
	 * 可重入锁
	 */
	LockResult tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);
	
	/**
	 * 联锁
	 */
	MultiLockResult tryMultiLock(String lockKey, String lockKey2, TimeUnit unit, int waitTime, int leaseTime);
	
	/**
	 * 读写锁
	 */
	ReadWriteLockResult tryReadWriteLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);
	
	//================解锁===============//
	
	void unlock(RLock lock);

	void unlock(RReadWriteLock lock);
	
	void unlock(RedissonMultiLock lock);
	
}
