package com.wql.cloud.basic.push.channel.getui.old;

/**
 * 通用消息推送内部模板
 */
public abstract class BaseMessageTemplate {

    /** 打开网页模板 */
	public static final int LINK_TEMPLATE_TYPE = 1;
	/** 打开应用模板 */
    public static final int NOTIFY_TEMPLATE_TYPE = 2;
    /** 透传消息模板 */
    public static final int TRANSMISSION_TEMPLATE_TYPE = 3;

    public static final int PUSH_IN_WIFI = 1;
    public static final int PUSH_AUTO = 0;
	/**
	 * 模板类型， 1: 转链接， 2: 通知  3:透传
	 */
	protected int templateType;
	
	public BaseMessageTemplate(String appId, String appKey, String masterSecret, boolean offline,
			long offlineExpireTime, int pushNetWorkType) {
		super();
		this.appId = appId;
		this.appKey = appKey;
		this.masterSecret = masterSecret;
		this.offline = offline;
		this.offlineExpireTime = offlineExpireTime;
		this.pushNetWorkType = pushNetWorkType;
	}
	

	public BaseMessageTemplate setBasicView(String title, String text, String logo, String logoUrl) {
		this.title = title;
		this.text = text;
		this.logo = logo;
		this.logoUrl = logoUrl;
		return this;
	}
	
	public BaseMessageTemplate setLinkNotify(boolean ring, boolean vibrate, boolean clearable, 
			String url) {
		this.ring = ring;
		this.vibrate = vibrate;
		this.clearable = clearable;
		this.url = url;
		this.templateType = LINK_TEMPLATE_TYPE;
		return this;
	}

	public BaseMessageTemplate setTransmissionNotify(boolean ring, boolean vibrate, boolean clearable, 
			int transmissionType, String transmissionContent) {
		this.ring = ring;
		this.vibrate = vibrate;
		this.clearable = clearable;
		this.transmissionType = transmissionType;
		this.transmissionContent = transmissionContent;
		this.templateType = NOTIFY_TEMPLATE_TYPE;
		return this;
	}
	
	/**
	 * 公共
	 */
	protected String appId;
	protected String appKey;
	protected String masterSecret;
	
	/**
	 * 是否离线推送，true:是，false:否
	 */
	protected boolean offline;
	/**
	 * 离线推送过期时间，单位 ms
	 */
	protected long offlineExpireTime;
	/**
	 * 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
	 */
	protected int pushNetWorkType;
//	/**
//	 * 是否同步执行
//	 */
//	protected boolean isSync;
//	/**
//	 * 优先级
//	 */
//	protected int priority;
	
	/**
	 * 通知栏标题
	 */
	protected String title;
	/**
	 * 通知栏内容
	 */
	protected String text;
	/**
	 * 通知栏图标
	 */
	protected String logo;
	/**
	 * 通知栏网络图标
	 */
	protected String logoUrl;
	/**
	 * 链接地址
	 */
	protected String url;
	/**
	 * 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	 */
	protected int transmissionType;
	/**
	 * 透传内容
	 */
	protected String transmissionContent;
	/**
	 * 通知是否响铃
	 */
	protected boolean ring = true;
	/**
	 * 通知是否震动
	 */
	protected boolean vibrate = true;
	/**
	 * 通知是否可清除
	 */
	protected boolean clearable;
	
	

	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getMasterSecret() {
		return masterSecret;
	}

	public void setMasterSecret(String masterSecret) {
		this.masterSecret = masterSecret;
	}

	public boolean isOffline() {
		return offline;
	}

	public void setOffline(boolean offline) {
		this.offline = offline;
	}

	public long getOfflineExpireTime() {
		return offlineExpireTime;
	}

	public void setOfflineExpireTime(long offlineExpireTime) {
		this.offlineExpireTime = offlineExpireTime;
	}

	public int getPushNetWorkType() {
		return pushNetWorkType;
	}

	public void setPushNetWorkType(int pushNetWorkType) {
		this.pushNetWorkType = pushNetWorkType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public boolean isRing() {
		return ring;
	}

	public void setRing(boolean ring) {
		this.ring = ring;
	}

	public boolean isVibrate() {
		return vibrate;
	}

	public void setVibrate(boolean vibrate) {
		this.vibrate = vibrate;
	}

	public boolean isClearable() {
		return clearable;
	}

	public void setClearable(boolean clearable) {
		this.clearable = clearable;
	}

	public int getTemplateType() {
		return templateType;
	}

	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getTransmissionType() {
		return transmissionType;
	}

	public void setTransmissionType(int transmissionType) {
		this.transmissionType = transmissionType;
	}

	public String getTransmissionContent() {
		return transmissionContent;
	}

	public void setTransmissionContent(String transmissionContent) {
		this.transmissionContent = transmissionContent;
	}

	

	
	
}
