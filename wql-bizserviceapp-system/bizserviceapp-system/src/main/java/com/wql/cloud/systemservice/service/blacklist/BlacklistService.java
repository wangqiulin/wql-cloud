package com.wql.cloud.systemservice.service.blacklist;

public interface BlacklistService {

	/**
	 * 加载黑名单ip到缓存
	 */
	void loadBlacklistCache();

}
