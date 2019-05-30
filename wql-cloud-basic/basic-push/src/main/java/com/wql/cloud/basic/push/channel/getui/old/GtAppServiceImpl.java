package com.wql.cloud.basic.push.channel.getui.old;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.wql.cloud.basic.push.channel.getui.vo.GetuiDataResult;

/**
 * 个推消息服务
 */
@Service
public class GtAppServiceImpl implements GtAppService {
	
     private static final Logger logger = LoggerFactory.getLogger(GtAppServiceImpl.class);

     private String DEFAULT_HOST = "http://sdk.open.api.igexin.com/apiex.htm";


    /**
     * ************************************************** Single ************************************************** 
     */
    /**
     * target 已封装的用户目标
     */
    @Override
    public String pushtoSingle(String appId, String appKey, String masterSecret, SingleMessage message, Target target)
        throws Exception {
        IGtPush push = new IGtPush(DEFAULT_HOST, appKey, masterSecret);
        IPushResult ret = push.pushMessageToSingle(message, target);
        logger.info("response: {}", ret.getResponse().toString());
        return ret.getResponse().toString();
    }

    /**
     * clientId 客户ID
     */
    @Override
	public String pushtoSingleByClientId(String appId, String appKey, String masterSecret, SingleMessage message,
			String clientId) throws Exception {

    	Target target = new Target();
        target.setAppId(appId);
        target.setClientId(clientId);
        return pushtoSingle(appId, appKey, masterSecret, message, target);
	}

    /**
     * alias 客户别名
     */
    @Override
    public String pushtoSingleByAlias(String appId, String appKey, String masterSecret,
        SingleMessage message, String alias) throws Exception {

        Target target = new Target();
        target.setAppId(appId);
        target.setAlias(alias);
        return pushtoSingle(appId, appKey, masterSecret, message, target);
    }

    /**
     * ************************************************** Single ************************************************** 
     */

    /**
     * ************************************************** List ************************************************** 
     */

    /**
     * targets
     */
    @Override
    public String pushList(String appId, String appKey, String masterSecret, ListMessage message, List<Target> targets)
        throws Exception {

        // 配置返回每个用户返回用户状态，可选
        System.setProperty("gexin_pushList_needDetails", "true");
        // 配置返回每个别名及其对应clientId的用户状态，可选
        System.setProperty("gexin_pushList_needAliasDetails", "true");
        IGtPush push = new IGtPush(DEFAULT_HOST, appKey, masterSecret);
        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        logger.info("taskId: {}, targets.size: {}", taskId, targets.size());
        IPushResult ret = push.pushMessageToList(taskId, targets);
        logger.info("response: {}", ret.getResponse().toString());
         return ret.getResponse().toString();
    }

    /**
     * clientIds
     */
	@Override
	public String pushListByClientIds(String appId, String appKey, String masterSecret, ListMessage message,
			List<String> clientIdList) throws Exception {

		// 配置推送目标
        List<Target> targets = new ArrayList<>(clientIdList.size());
        for (String clientId: clientIdList) {
            Target target = new Target();
            target.setAppId(appId);
            target.setClientId(clientId);
            targets.add(target);
        }
        return pushList(appId, appKey, masterSecret, message, targets);
	}

    /**
     * aliases
     */
    @Override
    public String pushListByAliases(String appId, String appKey, String masterSecret, ListMessage message,
        String... aliases) throws Exception {
        // 配置推送目标
        List<Target> targets = new ArrayList<Target>(aliases.length);
        for (String alias: aliases) {
            Target target = new Target();
            target.setAppId(appId);
            target.setAlias(alias);
            targets.add(target);
        }
        return pushList(appId, appKey, masterSecret, message, targets);
    }

    /**
     * ************************************************** List ************************************************** 
     */

    /**
     * ************************************************** App ************************************************** 
     */

    /**
     * app
     */
    @Override
    public String pushtoAPP(String appId, String appKey, String masterSecret, AppMessage message,
            String taskGroupName) throws Exception {
        IGtPush push = new IGtPush(DEFAULT_HOST, appKey, masterSecret);
        logger.debug("taskAlias: {}", taskGroupName);
        IPushResult ret = push.pushMessageToApp((AppMessage)message, taskGroupName);
        logger.debug("response: {}", ret.getResponse().toString());
        return ret.getResponse().toString();
    }

    /**
     * ************************************************** App **************************************************
     */

	@Override
	public GetuiDataResult pushtoSingle(GetuiMessageTemplate template, Target target) throws Exception {
		template.getSingleMessage(assembleTemplate(template));
		String result = pushtoSingle(
				template.getAppId(), template.getAppKey(), template.getMasterSecret(), template.getSingleMessage(), target);
		return new GetuiDataResult(result);
	}

	@Override
	public GetuiDataResult pushtoSingleByClientId(GetuiMessageTemplate template, String clientId) throws Exception {
		Target target = new Target();
		target.setAppId(template.getAppId());
		target.setClientId(clientId);
		return pushtoSingle(template, target);
	}

	@Override
	public GetuiDataResult pushtoSingleByAlias(GetuiMessageTemplate template, String alias) throws Exception {
		Target target = new Target();
		target.setAppId(template.getAppId());
		target.setAlias(alias);
		return pushtoSingle(template, target);
	}

	@Override
	public GetuiDataResult pushList(GetuiMessageTemplate template, List<Target> targets) throws Exception {
		template.getListMessage(assembleTemplate(template));
		String result = pushList(
				template.getAppId(), template.getAppKey(), template.getMasterSecret(), template.getListMessage(), targets);
		return new GetuiDataResult(result);
	}

	@Override
	public GetuiDataResult pushListByClientIds(GetuiMessageTemplate template, List<String> clientIdList) throws Exception {

		template.getListMessage(assembleTemplate(template));
		String result = pushListByClientIds(
				template.getAppId(), template.getAppKey(), template.getMasterSecret(), template.getListMessage(), clientIdList);
		return new GetuiDataResult(result);
	}

	@Override
	public GetuiDataResult pushListByAliases(GetuiMessageTemplate template, String... aliases) throws Exception {

		template.getListMessage(assembleTemplate(template));
		String result = pushListByAliases(
				template.getAppId(), template.getAppKey(), template.getMasterSecret(), template.getListMessage(), aliases);
		return new GetuiDataResult(result);
	}

	@Override
	public GetuiDataResult pushtoApp(GetuiMessageTemplate template, List<String> phoneTypeList,
			List<String> provinceList, List<String> tagList, String taskGroupName) throws Exception {

		AppMessage appMessage = template.getAppMessage(assembleTemplate(template), template.getAppConditions(phoneTypeList, provinceList, tagList));
		String result = pushtoAPP(template.getAppId(), template.getAppKey(), template.getMasterSecret(), appMessage, taskGroupName);
		return new GetuiDataResult(result);
	}

	@Override
	public GetuiDataResult pushtoAppByPhoneTypeList(GetuiMessageTemplate template, List<String> phoneTypeList, String taskGroupName)
			throws Exception {

		AppMessage appMessage = template.getAppMessage(assembleTemplate(template), template.getAppConditions(phoneTypeList, null, null));
		String result = pushtoAPP(template.getAppId(), template.getAppKey(), template.getMasterSecret(), appMessage, taskGroupName);
		return new GetuiDataResult(result);
	}

	@Override
	public GetuiDataResult pushtoAppByprovinceList(GetuiMessageTemplate template, List<String> provinceList, String taskGroupName)
			throws Exception {

		AppMessage appMessage = template.getAppMessage(assembleTemplate(template), template.getAppConditions(null, provinceList, null));
		String result = pushtoAPP(template.getAppId(), template.getAppKey(), template.getMasterSecret(), appMessage, taskGroupName);
		return new GetuiDataResult(result);
	}

	@Override
	public GetuiDataResult pushtoAppByTagList(GetuiMessageTemplate template, List<String> tagList, String taskGroupName) throws Exception {

		AppMessage appMessage = template.getAppMessage(assembleTemplate(template), template.getAppConditions(null, null, tagList));
		String result = pushtoAPP(template.getAppId(), template.getAppKey(), template.getMasterSecret(), appMessage, taskGroupName);
		return new GetuiDataResult(result);
	}

	@Override
	public GetuiMessageTemplate createMessageTemplate(String appId, String appKey, String masterSecret, boolean offline,
			long offlineExpireTime, int pushNetWorkType) {

		return new GetuiMessageTemplate(appId, appKey, masterSecret, offline, offlineExpireTime, pushNetWorkType);
	}

	/**
	 * 设置个推得url,否则使用默认
	 *
	 * @param url
	 */
	@Override
	public void setPushUrl(String url) {
		this.DEFAULT_HOST = url;
	}

	private ITemplate assembleTemplate(GetuiMessageTemplate template) {
		if (BaseMessageTemplate.LINK_TEMPLATE_TYPE == template.getTemplateType()) {
			LinkTemplate linkTemplate = template.getLinkTemplate(template.getUrl());
			return linkTemplate;

		} else if (BaseMessageTemplate.NOTIFY_TEMPLATE_TYPE == template.getTemplateType()) {
			NotificationTemplate notificationTemplate =
					template.getNotificationTemplate(template.getTransmissionType(), template.getTransmissionContent());
			return notificationTemplate;
		}else if (BaseMessageTemplate.TRANSMISSION_TEMPLATE_TYPE == template.getTemplateType()){
            TransmissionTemplate transmissionTemplate =
                    template.getTransmissionTemplate(template.getTransmissionType(), template.getTransmissionContent());
            return transmissionTemplate;
        }else {
			throw new RuntimeException("不确定的模板类型");
		}
	}

	
}
