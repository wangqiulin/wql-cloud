package com.wql.cloud.wqlcloud.redis.lock;
/**
 * Created by fangzhipeng on 2017/4/5.
 * 获取锁后需要处理的逻辑
 * 
 * @author wangqiulin
 * @date 2018年5月16日
 * @param <T>
 */
public interface AquiredLockWorker<T> {
	
     T invokeAfterLockAquire() throws Exception;
     
}
