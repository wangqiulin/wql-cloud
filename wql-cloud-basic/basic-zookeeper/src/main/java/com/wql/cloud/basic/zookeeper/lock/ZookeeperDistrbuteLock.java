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
			public void handleDataDeleted(String dataPath) throws Exception {
				if (countDownLatch != null) {
					countDownLatch.countDown();
				}
			}
			public void handleDataChange(String dataPath, Object data) throws Exception {
			}
		};
		
		zkClient.unsubscribeDataChanges(PATH, iZkDataListener);
		if (zkClient.exists(PATH)) {
			try {
				countDownLatch = new CountDownLatch(1);
				countDownLatch.await();
			} catch (Exception e) {
			}
		}

	}

}
