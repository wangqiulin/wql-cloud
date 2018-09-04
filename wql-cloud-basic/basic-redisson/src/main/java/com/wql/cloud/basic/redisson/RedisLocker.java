package com.wql.cloud.basic.redisson;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author wangqiulin
 * @date 2018年5月16日
 */
@Component
public class RedisLocker implements DistributedLocker {

    private final static String LOCKER_PREFIX = "lock:";

    @Autowired
    private RedissonConnector redissonConnector;
    
    @Override
    public <T> T lock(String resourceName, AquiredLockWorker<T> worker, int lockTime) 
    		throws UnableToAquireLockException, Exception {
        return lock(resourceName, worker, 0, lockTime);
    }

    
    @Override
    public <T> T lock(String resourceName, AquiredLockWorker<T> worker, int waitTime, int lockTime) 
    		throws UnableToAquireLockException, Exception {
        RedissonClient redisson = redissonConnector.getClient();
        RLock lock = redisson.getLock(LOCKER_PREFIX + resourceName);
        boolean success = lock.tryLock(waitTime, lockTime, TimeUnit.SECONDS);
        if (success) {
            try {
                return worker.invokeAfterLockAquire();
            } finally {
                lock.unlock();
            }
        }
        throw new UnableToAquireLockException();
    }

    
}
