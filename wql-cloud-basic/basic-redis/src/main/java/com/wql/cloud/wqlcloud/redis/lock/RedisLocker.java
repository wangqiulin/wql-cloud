package com.wql.cloud.wqlcloud.redis.lock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author wangqiulin
 * @date 2018年5月16日
 */
@Component
public class RedisLocker implements DistributedLocker {

    private final static String LOCKER_PREFIX = "lock:";

    //@Autowired
    //private RedissonConnector redissonConnector;
    
    @Autowired
    private RedissonClient redisson;
    
    @Override
    public <T> T lock(String resourceName, AquiredLockWorker<T> worker) 
    		throws InterruptedException, UnableToAquireLockException, Exception {
        return lock(resourceName, worker, 3);
    }

    
    @Override
    public <T> T lock(String resourceName, AquiredLockWorker<T> worker, int lockTime) 
    		throws UnableToAquireLockException, Exception {
        //RedissonClient redisson= redissonConnector.getClient();
        RLock lock = redisson.getLock(LOCKER_PREFIX + resourceName);
        // Wait for 3 seconds and automatically unlock it after lockTime seconds
        boolean success = lock.tryLock(3, lockTime, TimeUnit.SECONDS);
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
