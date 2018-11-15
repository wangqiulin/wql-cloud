/*package com.wql.cloud.userservice.distributionLock;

import java.util.concurrent.CountDownLatch;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

public class Worker implements Runnable {

    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;
    private final DistributedLockManager distributedLockManager;
    private RedissonClient redissonClient;

    public Worker(CountDownLatch startSignal, CountDownLatch doneSignal, 
    		DistributedLockManager distributedLockManager, RedissonClient redissonClient) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
        this.distributedLockManager = distributedLockManager;
        this.redissonClient = redissonClient;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " start");
            startSignal.await();

            Integer count = distributedLockManager.aspect(() -> {
                return aspect();
            });

            System.out.println(Thread.currentThread().getName() + ": count = " + count);
            doneSignal.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private int aspect() {
    	//业务代码
        RMap<String, Integer> map = redissonClient.getMap("distributionTest");
        Integer count1 = map.get("count");
        if (count1 > 0) {
            count1 = count1 - 1;
            map.put("count", count1);
        }
        return count1;
    }

}
*/