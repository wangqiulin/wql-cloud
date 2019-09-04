package com.wql.cloud.basic.zookeeper.lock;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperDistrbuteLock extends ZookeeperAbstractLock {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	private CountDownLatch countDownLatch = null;

	@Override
	public boolean tryLock() {
		try {
			zkClient.createEphemeral(lOCK_PATH);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void waitLock() {
		IZkDataListener iZkDataListener = new IZkDataListener() {
			//节点被删除
			public void handleDataDeleted(String dataPath) throws Exception {
				if (countDownLatch != null) {
					//计数器为0时，await后面的继续执行
					countDownLatch.countDown(); 
				}
			}
			//节点被修改
			public void handleDataChange(String dataPath, Object data) throws Exception {
				
			}
		};
		
		//注册到zkClient，进行监听
		zkClient.subscribeDataChanges(lOCK_PATH, iZkDataListener);
		
		//控制程序的等待
		if (zkClient.exists(lOCK_PATH)) {
			countDownLatch = new CountDownLatch(1);
			try {
				//等待时候不再往下走，当为0的时候后面的继续执行
				countDownLatch.await();
			} catch (Exception e) {
				
			}
		}
		
		//为了不影响程序的执行，建议删除该事件监听，监听完了就删除掉
		zkClient.unsubscribeDataChanges(lOCK_PATH, iZkDataListener);
	}

}
