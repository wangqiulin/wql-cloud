package com.wql.cloud.basic.security.wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import com.wql.cloud.basic.security.model.UserInfo;
import io.jsonwebtoken.lang.Assert;

public interface SystemRequestWrapper {

    Logger log = LoggerFactory.getLogger(SystemRequestWrapper.class);

    /**
     * 包装请求对象
     * T to SystemRequest<T>
     *
     * @param t
     * @param <T>
     * @return
     */
    default <T> SystemRequest<T> wrap(T t) {
        UserInfo user = null;
        try {
            user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            log.warn("获取用户信息失败",e);
        }
        Assert.notNull(user, "用户未登陆");
        SystemRequest<T> request = new SystemRequest<>();
        BeanUtils.copyProperties(user, request);
        request.setData(t);
        return request;
    }

    /**
     * 生成包装请求对象
     * @return
     */
    default SystemRequest<Void> wrap() {
        return wrap(null);
    }

}
