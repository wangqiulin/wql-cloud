package com.wql.cloud.basic.redisson.res;

import org.redisson.RedissonMultiLock;

public class MultiLockResult {

	private Boolean locked;

	private RedissonMultiLock multiLock;

	public MultiLockResult() {
	}

	public MultiLockResult(Boolean locked, RedissonMultiLock multiLock) {
		this.locked = locked;
		this.multiLock = multiLock;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public RedissonMultiLock getMultiLock() {
		return multiLock;
	}

	public void setMultiLock(RedissonMultiLock multiLock) {
		this.multiLock = multiLock;
	}

}
