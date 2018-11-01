package com.wql.cloud.basic.redisson.res;

import org.redisson.api.RReadWriteLock;

public class ReadWriteLockResult {

	private Boolean locked;
	
	private RReadWriteLock readWriteLock;

	public ReadWriteLockResult() {
	}

	public ReadWriteLockResult(Boolean locked, RReadWriteLock readWriteLock) {
		this.locked = locked;
		this.readWriteLock = readWriteLock;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public RReadWriteLock getReadWriteLock() {
		return readWriteLock;
	}

	public void setReadWriteLock(RReadWriteLock readWriteLock) {
		this.readWriteLock = readWriteLock;
	}

}
