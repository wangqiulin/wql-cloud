package com.wql.cloud.basic.redisson.res;

import org.redisson.api.RLock;

public class LockResult {

	private Boolean locked;

	private RLock rLock;

	public LockResult() {
	}

	public LockResult(Boolean locked, RLock rLock) {
		this.locked = locked;
		this.rLock = rLock;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public RLock getrLock() {
		return rLock;
	}

	public void setrLock(RLock rLock) {
		this.rLock = rLock;
	}

}
