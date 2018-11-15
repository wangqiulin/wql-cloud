/*package com.wql.cloud.userservice.controller;

import java.util.concurrent.CountDownLatch;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.userservice.distributionLock.DistributedLockManager;
import com.wql.cloud.userservice.distributionLock.Worker;

@RestController
@RequestMapping("/user/distributedLockTest")
public class DistributedLockTestController {

    private int count = 10;

    @Autowired
    private RedissonClient redissonClient;
    
    @Autowired
    private DistributedLockManager distributedLockManager;
    
    @Autowired
    private TaskExecutor taskExecutor;

    @RequestMapping(method = RequestMethod.GET)
    public String distributedLockTest() throws Exception {
        RMap<String, Integer> map = redissonClient.getMap("distributionTest");
        map.put("count", 8);

        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(count);

        for (int i = 0; i < count; ++i) { // create and start threads
            //new Thread(new Worker1(startSignal, doneSignal, distributedLockManager, redissonClient)).start();
            taskExecutor.execute(new Worker(startSignal, doneSignal, distributedLockManager, redissonClient));
        }

        startSignal.countDown(); // let all threads proceed
        doneSignal.await();
        System.out.println("All processors done. Shutdown connection");
        return "finish";
    }

}
*/