/*package com.wql.cloud.userservice.distributionLock;

import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import com.wql.cloud.basic.redisson.distributeLock.aop.DistributedLock;

@Component
public class DistributedLockManager {
    
    @DistributedLock(lockName = "lock", lockNamePost = ".lock")
    public int aspect(Supplier<Integer> supplier) {
        return supplier.get();
    }

}*/