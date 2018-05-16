package com.wql.cloud.wqlcloud.controller;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.wqlcloud.client.order.OrderClient;
import com.wql.cloud.wqlcloud.redis.lock.AquiredLockWorker;
import com.wql.cloud.wqlcloud.redis.lock.RedisLocker;

/**
 *
 * @author wangqiulin
 * @date 2018年5月15日
 */
@RestController
public class UserController {

	@Autowired
	private OrderClient orderClient;
	
	
	/**
	 * 调用order模块的接口
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/user/queryUserOrderByName",method = RequestMethod.GET)
    public String queryOrderByName(@RequestParam String name){
        return orderClient.queryOrderByName(name);
    }
	
	
	@RequestMapping(value = "/hi",method = RequestMethod.GET)
    public String sayHi(@RequestParam String name){
        return "user===> hi, " + name;
    }
	
	
	/**
	 * POST方式刷新配置值: http://localhost:8881/bus/refresh
	 */
	@Value("${name}")
	String configName;
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String sayHello(){
        return configName;
    }
	
	
	@Autowired
    RedisLocker distributedLocker;
	
    @RequestMapping(value = "/redlock", method = RequestMethod.GET)
    public String testRedlock() throws Exception{
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(5);
        for (int i = 0; i < 5; ++i) { // create and start threads
            new Thread(new Worker(startSignal, doneSignal)).start();
        }
        startSignal.countDown(); // let all threads proceed
        doneSignal.await();
        System.out.println("All processors done. Shutdown connection");
        return "redlock";
    }

    class Worker implements Runnable {
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;

        Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        public void run() {
            try {
                startSignal.await();
                distributedLocker.lock("test",new AquiredLockWorker<Object>() {

                    @Override
                    public Object invokeAfterLockAquire() {
                        doTask();
                        return null;
                    }

                });
            }catch (Exception e){

            }
        }

        void doTask() {
            System.out.println(Thread.currentThread().getName() + " start");
            Random random = new Random();
            int _int = random.nextInt(200);
            System.out.println(Thread.currentThread().getName() + " sleep " + _int + "millis");
            try {
                Thread.sleep(_int);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " end");
            doneSignal.countDown();
        }
    }
	
	
	
}
