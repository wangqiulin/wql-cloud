package com.wql.cloud.userservice.util;

import java.util.UUID;

public class UUIDUtil {

	public static String getUuid() {
		return UUID.randomUUID().toString();
	}
	public static String getShortUuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
