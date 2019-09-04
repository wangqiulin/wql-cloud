package com.wql.cloud.basic.zookeeper.lock;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ZookeeperAbstractLock implements Lock {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	//zkServer zookeeper服务器的地址，用","分割
	protected static final String CONNECT_ADDRES = "192.168.1.90:2181,192.168.1.91:2181,192.168.1.92:2181";
	
	//sessionTimeout超时回话，为毫秒，默认是30000ms
	protected static final int SESSIONTIME = 10000;
	protected static final int CONNECTIONTIME = 10000;
	
	protected static final String lOCK_PATH = "/lock";
	
	protected ZkClient zkClient = new ZkClient(CONNECT_ADDRES, SESSIONTIME, CONNECTIONTIME, new SerializableSerializer());

	public void getLock() {
		if (tryLock()) {
			logger.info(Thread.currentThread().getName() + "-- get lock");
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
			logger.info(Thread.currentThread().getName() + "-- unLock");
		} catch (Exception e) {

		}
	}

}
