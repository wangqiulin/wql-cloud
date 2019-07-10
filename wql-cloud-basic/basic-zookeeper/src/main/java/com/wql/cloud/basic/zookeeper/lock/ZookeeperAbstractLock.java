package com.wql.cloud.basic.zookeeper.lock;

import org.I0Itec.zkclient.ZkClient;

public abstract class ZookeeperAbstractLock implements Lock {

	protected static final String CONNECT_ADDRES = "192.168.1.90:2181,192.168.1.91:2181,192.168.1.92:2181";
	
	protected static final int SESSIONTIME = 2000;
	
	protected static final String PATH = "/lock";
	
	protected ZkClient zkClient = new ZkClient(CONNECT_ADDRES);

	public void getLock() {
		if (tryLock()) {
			//  获取
			System.out.println(Thread.currentThread().getName() + "-- get lock");
		} else {
			waitLock();
			getLock();
		}
	}

	public abstract boolean tryLock();

	public abstract void waitLock();

	public void unLock() {
		try {
			if (zkClient != null) {
				zkClient.close();
			}
		} catch (Exception e) {

		}
	}

}
