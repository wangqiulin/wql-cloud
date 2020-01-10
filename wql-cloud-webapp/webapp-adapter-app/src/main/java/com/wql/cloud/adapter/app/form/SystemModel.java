package com.wql.cloud.adapter.app.form;

import java.io.Serializable;

/**
 * 请求入参-System节
 */
public class SystemModel implements Serializable {

	private static final long serialVersionUID = 3049280940905983337L;

	/**
	 * 包名
	 */
	private String identifier;

	/**
	 * iOS/Android/H5
	 */
	private String appType;

	/**
	 * app 版本
	 */
	private String appVersion;

	/**
	 * 补丁版本
	 */
	private String bundleVersion;

	/**
	 * 操作系统版本
	 */
	private String systemVersion;

	/**
	 * 硬件类型
	 */
	private String hardware;

	/**
	 * 设备唯一号
	 */
	private String deviceId;

	/**
	 * 渠道
	 */
	private String channel;

	/**
	 * 客户端IP
	 */
	private String clientIp;

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the appType
	 */
	public String getAppType() {
		return appType;
	}

	/**
	 * @param appType the appType to set
	 */
	public void setAppType(String appType) {
		this.appType = appType;
	}

	/**
	 * @return the appVersion
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * @param appVersion the appVersion to set
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * @return the bundleVersion
	 */
	public String getBundleVersion() {
		return bundleVersion;
	}

	/**
	 * @param bundleVersion the bundleVersion to set
	 */
	public void setBundleVersion(String bundleVersion) {
		this.bundleVersion = bundleVersion;
	}

	/**
	 * @return the systemVersion
	 */
	public String getSystemVersion() {
		return systemVersion;
	}

	/**
	 * @param systemVersion the systemVersion to set
	 */
	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	/**
	 * @return the hardware
	 */
	public String getHardware() {
		return hardware;
	}

	/**
	 * @param hardware the hardware to set
	 */
	public void setHardware(String hardware) {
		this.hardware = hardware;
	}

	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * @return the clientIp
	 */
	public String getClientIp() {
		return clientIp;
	}

	/**
	 * @param clientIp the clientIp to set
	 */
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
