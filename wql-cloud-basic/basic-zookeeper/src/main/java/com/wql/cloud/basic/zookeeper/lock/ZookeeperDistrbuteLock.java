package com.wql.cloud.basic.zookeeper.lock;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkDataListener;

public class ZookeeperDistrbuteLock extends ZookeeperAbstractLock {

	private CountDownLatch countDownLatch = null;

	@Override
	public boolean tryLock() {
		try {
			zkClient.createEphemeral(PATH);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void waitLock() {
		IZkDataListener iZkDataListener = new IZkDataListener() {
			//节点被删除的时候 事件通知
			public void handleDataDeleted(String dataPath) throws Exception {
				if (countDownLatch != null) {
					countDownLatch.countDown();
				}
			}
			public void handleDataChange(String dataPath, Object data) throws Exception {
			}
		};
		
		//注册到zkClient，进行监听
		zkClient.subscribeDataChanges(PATH, iZkDataListener);
		
		if (zkClient.exists(PATH)) {
			try {
				countDownLatch = new CountDownLatch(1);
				countDownLatch.await();
			} catch (Exception e) {
			}
		}
		
		//删除监听
		zkClient.unsubscribeDataChanges(PATH, iZkDataListener);
	}

}
