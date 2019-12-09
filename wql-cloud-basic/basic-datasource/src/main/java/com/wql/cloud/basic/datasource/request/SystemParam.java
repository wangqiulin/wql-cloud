package com.wql.cloud.basic.datasource.request;

import java.io.Serializable;

public class SystemParam implements Serializable {

	private static final long serialVersionUID = 1852319629481780498L;

	/**客户端IP*/
	private String clientIp = null;

	/** 包名 */
	private String identifier = null;

	/**系统类型*/
	private String appType = null;

	/**app版本*/
	private String appVersion = null;

	/**系统版本号*/
	private String systemVersion = null;
	
	/**补丁版本*/
	private Integer bundleVersion = null;

	/**硬件类型(如“iphone 6s”, “Galaxy Edge7”等) */
	private String hardware = null;

	/**渠道*/
	private String channel = null;

	/**跟踪id*/
	private String traceId = null;

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public Integer getBundleVersion() {
		return bundleVersion;
	}

	public void setBundleVersion(Integer bundleVersion) {
		this.bundleVersion = bundleVersion;
	}

	public String getHardware() {
		return hardware;
	}

	public void setHardware(String hardware) {
		this.hardware = hardware;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

}
