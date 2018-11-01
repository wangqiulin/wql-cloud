package com.wql.cloud.basic.redisson.lock;

import java.util.concurrent.TimeUnit;

import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import com.wql.cloud.basic.redisson.res.LockResult;
import com.wql.cloud.basic.redisson.res.MultiLockResult;
import com.wql.cloud.basic.redisson.res.ReadWriteLockResult;

/**
 * @auther wangqiulin
 * @date 2018/10/19
 */
public class RedissonDistributedLocker implements DistributedLocker {

	private RedissonClient redissonClient;

	public void setRedissonClient(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}
	
	@Override
	public LockResult tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
		try {
			RLock lock = redissonClient.getLock(lockKey);
			boolean locked = lock.tryLock(waitTime, leaseTime, unit);
			return new LockResult(locked, lock);
		} catch (InterruptedException e) {
			return new LockResult(false, null);
		}
	}

	@Override
	public ReadWriteLockResult tryReadWriteLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
		try {
			RReadWriteLock rwlock = redissonClient.getReadWriteLock(lockKey);
			boolean locked = rwlock.writeLock().tryLock(waitTime, leaseTime, unit);
			return new ReadWriteLockResult(locked, rwlock);
		} catch (InterruptedException e) {
			return new ReadWriteLockResult(false, null);
		}
	}

	@Override
	public MultiLockResult tryMultiLock(String lockKey, String lockKey2, TimeUnit unit, int waitTime, int leaseTime) {
		RLock lock1 = redissonClient.getLock(lockKey);
		RLock lock2 = redissonClient.getLock(lockKey2);
		RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2);
		try {
			// 同时加锁：lock1 lock2, 所有的锁都上锁成功才算成功。
			boolean locked = lock.tryLock(waitTime, leaseTime, unit);
			return new MultiLockResult(locked, lock);
		} catch (InterruptedException e) {
			return new MultiLockResult(false, null);
		} 
	}
	
	@Override
	public void unlock(RLock lock) {
		if (lock != null) {
			lock.unlock();
		}
	}
	
	@Override
	public void unlock(RReadWriteLock lock) {
		if (lock != null) {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void unlock(RedissonMultiLock lock) {
		if(lock != null) {
			lock.unlock();
		}
	}
	

}
