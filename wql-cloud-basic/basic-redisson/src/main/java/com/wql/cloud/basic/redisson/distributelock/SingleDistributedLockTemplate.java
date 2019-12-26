package com.wql.cloud.basic.redisson.distributelock;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleDistributedLockTemplate implements DistributedLockTemplate {
	
	private static final Logger logger = LoggerFactory.getLogger(SingleDistributedLockTemplate.class);
	
    private RedissonClient redisson;

    public void setRedisson(RedissonClient redisson) {
        this.redisson = redisson;
    }
    
    public SingleDistributedLockTemplate() {
    	
    }

    public SingleDistributedLockTemplate(RedissonClient redisson) {
        this.redisson = redisson;
    }

    
    @Override
    public <T> T lock(DistributedLockCallback<T> callback, boolean fairLock) {
        return lock(callback, DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT, fairLock);
    }

    @Override
    public <T> T lock(DistributedLockCallback<T> callback, long leaseTime, TimeUnit timeUnit, boolean fairLock) {
        RLock lock = null;
        try {
        	lock = getLock(callback.getLockName(), fairLock);
        	if(lock != null) {
        		lock.lock(leaseTime, timeUnit);
        		return callback.process();
        	}
        } finally {
            if (lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }
        return null;
    }
    
    
    @Override
    public <T> T tryLock(DistributedLockCallback<T> callback, boolean fairLock) {
        return tryLock(callback, DEFAULT_WAIT_TIME, DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT, fairLock);
    }

    @Override
    public <T> T tryLock(DistributedLockCallback<T> callback, long waitTime, long leaseTime, TimeUnit timeUnit, boolean fairLock) {
        RLock lock = null;
        try {
        	lock = getLock(callback.getLockName(), fairLock);
            if (lock != null && lock.tryLock(waitTime, leaseTime, timeUnit)) {
            	logger.info("【分布式锁】 key--->{}, 获取成功", callback.getLockName());
                return callback.process();
            }
        } catch (InterruptedException e) {
        	logger.error("【分布式锁】处理异常", e);
        } finally {
            if (lock != null && lock.isLocked()) {
                lock.unlock();
                logger.info("【分布式锁】 key--->{}, 释放成功", callback.getLockName());
            }
        }
        return null;
    }

    private RLock getLock(String lockName, boolean fairLock) {
        RLock lock;
        if (fairLock) {
            lock = redisson.getFairLock(lockName);
        } else {
            lock = redisson.getLock(lockName);
        }
        return lock;
    }

    
}
