package com.wql.cloud.basic.push.channel.getui.old;

import java.util.List;

import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.wql.cloud.basic.push.channel.getui.vo.GetuiDataResult;

/**
 * 个推 app 消息服务
 */
 public interface GtAppService {

     /**
      * 场景1：某用户发生了一笔交易，银行及时下发一条推送消息给该用户。
      * 场景2：用户定制了某本书的预订更新，当本书有更新时，需要向该用户及时下发一条更新提醒信息。
      * 这些需要向指定某个用户推送消息的场景，即需要使用对单个用户推送消息的接口。
      * @param appId
      * @param appKey
      * @param masterSecret
      * @param message
      * @param target
      * @return
      * @throws Exception
      */
     public String pushtoSingle(String appId, String appKey, String masterSecret, SingleMessage message, Target target) throws Exception;
     
     /**
      * 场景1：某用户发生了一笔交易，银行及时下发一条推送消息给该用户。
      * 场景2：用户定制了某本书的预订更新，当本书有更新时，需要向该用户及时下发一条更新提醒信息。
      * 这些需要向指定某个用户推送消息的场景，即需要使用对单个用户推送消息的接口。
      * @param appId
      * @param appKey
      * @param masterSecret
      * @param message
      * @param clientId
      * @return
      * @throws Exception
      */
     public String pushtoSingleByClientId(String appId, String appKey, String masterSecret, SingleMessage message, String clientId) throws Exception;
     
     /**
      * 场景1：某用户发生了一笔交易，银行及时下发一条推送消息给该用户。
      * 场景2：用户定制了某本书的预订更新，当本书有更新时，需要向该用户及时下发一条更新提醒信息。
      * 这些需要向指定某个用户推送消息的场景，即需要使用对单个用户推送消息的接口。
      * @param appId
      * @param appKey
      * @param masterSecret
      * @param message
      * @param alias
      * @return
      * @throws Exception
      */
     public String pushtoSingleByAlias(String appId, String appKey, String masterSecret, SingleMessage message, String alias) throws Exception;
     
     /**
      * pushMessageToList-对指定用户列表推送消息
      * @param appId
      * @param appKey
      * @param masterSecret
      * @param message
      * @param targets
      * @return
      * @throws Exception
      */
     public String pushList(String appId, String appKey, String masterSecret, ListMessage message, List<Target> targets) throws Exception;
     

     /**
      * pushMessageToList-对指定用户列表推送消息
      * @param appId
      * @param appKey
      * @param masterSecret
      * @param message
      * @param clientIds
      * @return
      * @throws Exception
      */
     public String pushListByClientIds(String appId, String appKey, String masterSecret, ListMessage message, List<String> clientIdList) throws Exception;
     
     /**
      * pushMessageToList-对指定用户列表推送消息
      * @param appId
      * @param appKey
      * @param masterSecret
      * @param message
      * @param aliases
      * @return
      * @throws Exception
      */
     public String pushListByAliases(String appId, String appKey, String masterSecret, ListMessage message, String...aliases) throws Exception;
     
     /**
      * 场景1，某app周年庆，群发消息给该app的所有用户，提醒用户参加周年庆活动。
      * 接口(pushMessageToApp-对指定应用群推消息)
      * 注：此接口有频次控制，申请修改请联系邮箱：kegf@getui.com 。
      * @param appId
      * @param appKey
      * @param masterSecret
      * @param message
      * @param taskGroupName
      * @return
      * @throws Exception
      */
     public String pushtoAPP(String appId, String appKey, String masterSecret, AppMessage message, String taskGroupName) throws Exception;
     
     
     /**
      * 单个推送
      * @param template
      * @param target
      * @param url
      * @return
      * @throws Exception
      */
     public GetuiDataResult pushtoSingle(GetuiMessageTemplate template, Target target) throws Exception;
     /**
      * 单个推送-clientId
      * @param template
      * @param clientId
      * @return
      * @throws Exception
      */
     public GetuiDataResult pushtoSingleByClientId(GetuiMessageTemplate template, String clientId) throws Exception;
     
     /**
      * 单个推送-alias
      * @param template
      * @param alias
      * @return
      * @throws Exception
      */
     public GetuiDataResult pushtoSingleByAlias(GetuiMessageTemplate template, String alias) throws Exception;
     
     /**
      * 批量推送
      * @param template
      * @param target
      * @param url
      * @return
      * @throws Exception
      */
     public GetuiDataResult pushList(GetuiMessageTemplate template, List<Target> target) throws Exception;
     
     /**
      * 批量推送-clientIds
      * @param template
      * @param clientIds
      * @return
      * @throws Exception
      */
     public GetuiDataResult pushListByClientIds(GetuiMessageTemplate template, List<String> clientIdList) throws Exception;
     
     /**
      * 批量推送-aliases
      * @param template
      * @param aliases
      * @return
      * @throws Exception
      */
     public GetuiDataResult pushListByAliases(GetuiMessageTemplate template, String...aliases) throws Exception;
     
     /**
      * app推送-分用户群
      * @param template
      * @param phoneTypeList
      * @param provinceList
      * @param tagList
      * @param taskGroupName
      * @return
      * @throws Exception
      */
     public GetuiDataResult pushtoApp(GetuiMessageTemplate template, List<String> phoneTypeList, List<String> provinceList, List<String> tagList, String taskGroupName) throws Exception;
     
     /**
      * app推送-分手机
      * @param template
      * @param phoneTypeList
      * @param taskGroupName
      * @return
      * @throws Exception
      */
     public GetuiDataResult pushtoAppByPhoneTypeList(GetuiMessageTemplate template, List<String> phoneTypeList, String taskGroupName) throws Exception;
     
     /**
      * app推送-分省份
      * @param template
      * @param provinceList
      * @param taskGroupName
      * @return
      * @throws Exception
      */
     public GetuiDataResult pushtoAppByprovinceList(GetuiMessageTemplate template, List<String> provinceList, String taskGroupName) throws Exception;
     
     /**
      * app推送-分自定义群体
      * @param template
      * @param tagList
      * @param taskGroupName
      * @return
      * @throws Exception
      */
     public GetuiDataResult pushtoAppByTagList(GetuiMessageTemplate template, List<String> tagList, String taskGroupName) throws Exception;
     
     /**
      * 创建消息模板
      * @param appId
      * @param appKey
      * @param masterSecret
      * @param offline
      * @param offlineExpireTime
      * @param pushNetWorkType
      * @return
      */
     public GetuiMessageTemplate createMessageTemplate(String appId, String appKey, String masterSecret, boolean offline,
 			long offlineExpireTime, int pushNetWorkType);

    /**
     * 设置个推得url,否则使用默认
     * @param url
     */
    public void setPushUrl(String url);
}
