package com.wql.cloud.basic.push.channel.getui.old;

import java.util.List;

import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;

/**
 * 个推消息模板
 *
 */
public class GetuiMessageTemplate extends BaseMessageTemplate {

	public GetuiMessageTemplate(String appId, String appKey, String masterSecret, boolean offline,
			long offlineExpireTime, int pushNetWorkType) {
		super(appId, appKey, masterSecret, offline, offlineExpireTime, pushNetWorkType);
	}

	private SingleMessage singleMessage;
	private ListMessage listMessage;
	private AppMessage appMessage;
	private LinkTemplate linkTemplate;
	private NotificationTemplate notificationTemplate;
	private TransmissionTemplate transmissionTemplate;
	private Style0 style0;
	private AppConditions appConditions;

	/**
	 * 转链接模板
	 *
	 * @param url
	 * @return
	 */
	public LinkTemplate getLinkTemplate(String url) {
		if (null == linkTemplate) {
			linkTemplate = new LinkTemplate();
		}
		linkTemplate.setAppId(this.appId);
		linkTemplate.setAppkey(this.appKey);
		if (null == style0) {
			style0 = new Style0();
		}
		style0.setTitle(this.title);
		style0.setText(this.text);
		style0.setLogo(this.logo);
		style0.setLogoUrl(this.logoUrl);
		style0.setRing(this.ring);
		style0.setVibrate(this.vibrate);
		style0.setClearable(this.clearable);

		linkTemplate.setStyle(style0);
		// 设置打开的网址地址
		linkTemplate.setUrl(url);
		return this.linkTemplate;
	}

	/**
	 * 通知模板
	 *
	 * @param transmissionType 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	 * @param content
	 * @return
	 */
	public NotificationTemplate getNotificationTemplate(int transmissionType, String content) {
		if (null == linkTemplate) {
			notificationTemplate = new NotificationTemplate();
		}
		notificationTemplate.setAppId(this.appId);
		notificationTemplate.setAppkey(this.appKey);
		if (null == style0) {
			style0 = new Style0();
		}
		style0.setTitle(this.title);
		style0.setText(this.text);
		style0.setLogo(this.logo);
		style0.setLogoUrl(this.logoUrl);
		style0.setRing(this.ring);
		style0.setVibrate(this.vibrate);
		style0.setClearable(this.clearable);

		notificationTemplate.setStyle(style0);
		// 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
		notificationTemplate.setTransmissionType(transmissionType);
		// 透传内容
		notificationTemplate.setTransmissionContent(content);

		return this.notificationTemplate;
	}

	/**
	 * 透传模板
	 * 
	 * @param transmissionType 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	 * @param triContent       透传内容
	 * @return
	 */
	public TransmissionTemplate getTransmissionTemplate(int transmissionType, String triContent) {
		if (transmissionTemplate == null) {
			transmissionTemplate = new TransmissionTemplate();
		}
		transmissionTemplate.setAppId(this.appId);
		transmissionTemplate.setAppkey(this.appKey);
		transmissionTemplate.setTransmissionContent(triContent);
		transmissionTemplate.setTransmissionType(transmissionType);

		APNPayload payload = new APNPayload();
		// 角标
		payload.setAutoBadge("-1");
		// 声音
		payload.setSound("default");
		payload.setContentAvailable(1);
		// 消息内容
		APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
		alertMsg.setBody(this.text);
		// payload.setAlertMsg(new APNPayload.SimpleAlertMsg(this.text));
		payload.setAlertMsg(alertMsg);
		payload.addCustomMsg("message", triContent);
		transmissionTemplate.setAPNInfo(payload);

		return this.transmissionTemplate;
	}

	/**
	 * 获取单例推送消息对象
	 *
	 * @param template
	 * @return
	 */
	public SingleMessage getSingleMessage(ITemplate template) {
		if (null == singleMessage) {
			singleMessage = new SingleMessage();
		}
		singleMessage.setOffline(this.offline);
		if (this.offline) {
			singleMessage.setOfflineExpireTime(this.offlineExpireTime);
		}
		singleMessage.setPushNetWorkType(this.pushNetWorkType);
		singleMessage.setData(template);
		return this.singleMessage;
	}

	/**
	 * 获取批量推送消息对象
	 *
	 * @param template
	 * @return
	 */
	public ListMessage getListMessage(ITemplate template) {
		if (null == listMessage) {
			listMessage = new ListMessage();
		}
		listMessage.setOffline(this.offline);
		if (this.offline) {
			listMessage.setOfflineExpireTime(this.offlineExpireTime);
		}
		listMessage.setPushNetWorkType(this.pushNetWorkType);
		listMessage.setData(template);
		return this.listMessage;
	}

	/**
	 * @param template
	 * @param appConditions
	 * @return
	 */
	public AppMessage getAppMessage(ITemplate template, AppConditions appConditions) {
		if (null == appMessage) {
			appMessage = new AppMessage();
		}
		appMessage.setOffline(this.offline);
		if (this.offline) {
			appMessage.setOfflineExpireTime(this.offlineExpireTime);
		}
		appMessage.setPushNetWorkType(this.pushNetWorkType);
		appMessage.setData(template);
		appMessage.setConditions(appConditions);
		return this.appMessage;
	}

	/**
	 * App类别推送条件 1、PHONE_TYPE 手机类型 2、REGION 省份 3、TAG 自定义群体
	 *
	 * @param phoneTypeList
	 * @param provinceList
	 * @param tagList
	 * @return
	 */
	public AppConditions getAppConditions(List<String> phoneTypeList, List<String> provinceList, List<String> tagList) {
		if (null == appConditions) {
			appConditions = new AppConditions();
		}
		appConditions.addCondition(AppConditions.PHONE_TYPE, phoneTypeList);
		appConditions.addCondition(AppConditions.REGION, provinceList);
		appConditions.addCondition(AppConditions.TAG, tagList);
		return this.appConditions;
	}

	public SingleMessage getSingleMessage() {
		return singleMessage;
	}

	public void setSingleMessage(SingleMessage singleMessage) {
		this.singleMessage = singleMessage;
	}

	public ListMessage getListMessage() {
		return listMessage;
	}

	public void setListMessage(ListMessage listMessage) {
		this.listMessage = listMessage;
	}

	public AppMessage getAppMessage() {
		return appMessage;
	}

	public void setAppMessage(AppMessage appMessage) {
		this.appMessage = appMessage;
	}

	public LinkTemplate getLinkTemplate() {
		return linkTemplate;
	}

	public void setLinkTemplate(LinkTemplate linkTemplate) {
		this.linkTemplate = linkTemplate;
	}

	public NotificationTemplate getNotificationTemplate() {
		return notificationTemplate;
	}

	public void setNotificationTemplate(NotificationTemplate notificationTemplate) {
		this.notificationTemplate = notificationTemplate;
	}

	public Style0 getStyle0() {
		return style0;
	}

	public void setStyle0(Style0 style0) {
		this.style0 = style0;
	}

	public AppConditions getAppConditions() {
		return appConditions;
	}

	public void setAppConditions(AppConditions appConditions) {
		this.appConditions = appConditions;
	}

}
