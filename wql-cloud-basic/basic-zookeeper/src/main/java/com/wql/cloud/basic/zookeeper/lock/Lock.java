package com.wql.cloud.basic.zookeeper.lock;

public interface Lock {

	public void getLock();

	public void unLock();
	
}
