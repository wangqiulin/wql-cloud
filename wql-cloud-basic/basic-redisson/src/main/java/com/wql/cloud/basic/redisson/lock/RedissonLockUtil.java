package com.wql.cloud.basic.redisson.lock;

import java.util.concurrent.TimeUnit;

import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;

import com.wql.cloud.basic.redisson.res.LockResult;
import com.wql.cloud.basic.redisson.res.MultiLockResult;
import com.wql.cloud.basic.redisson.res.ReadWriteLockResult;

/**
 * 分布式锁工具类
 * 
 * @author wangqiulin
 * @date 2018/10/19
 *
 */
public class RedissonLockUtil {

	private static DistributedLocker redissLock;

	public static void setLocker(DistributedLocker locker) {
		redissLock = locker;
	}

	/**
	 * 可重入锁
	 * @param lockKey
	 * @param waitTime
	 * @param leaseTime
	 * @param unit
	 * @return
	 */
	public static LockResult tryLock(String lockKey, int waitTime, int leaseTime, TimeUnit unit) {
		return redissLock.tryLock(lockKey, unit, waitTime, leaseTime);
	}

	/**
	 * 联锁
	 * @param lockKey
	 * @param lockKey2
	 * @param waitTime
	 * @param leaseTime
	 * @param unit
	 * @return
	 */
	public static MultiLockResult tryMultiLock(String lockKey, String lockKey2, int waitTime, int leaseTime,
			TimeUnit unit) {
		return redissLock.tryMultiLock(lockKey, lockKey2, unit, waitTime, leaseTime);
	}

	/**
	 * 读写锁
	 * @param lockKey
	 * @param waitTime
	 * @param leaseTime
	 * @param unit
	 * @return
	 */
	public static ReadWriteLockResult tryReadWriteLock(String lockKey, int waitTime, int leaseTime, TimeUnit unit) {
		return redissLock.tryReadWriteLock(lockKey, unit, waitTime, leaseTime);
	}

	/**
	 * 释放可重入锁
	 * @param lock
	 */
	public static void unlock(RLock lock) {
		redissLock.unlock(lock);
	}
	
	/**
	 * 释放联锁
	 * @param lock
	 */
	public static void unlock(RedissonMultiLock lock) {
		redissLock.unlock(lock);
	}
	
	/**
	 * 释放读写锁
	 * @param lock
	 */
	public static void unlock(RReadWriteLock lock) {
		redissLock.unlock(lock);
	}
	

}
