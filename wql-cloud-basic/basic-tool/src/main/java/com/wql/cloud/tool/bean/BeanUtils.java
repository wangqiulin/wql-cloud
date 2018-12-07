package com.wql.cloud.tool.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;

public class BeanUtils extends org.springframework.beans.BeanUtils{
	
	private BeanUtils() {}

    /**
     * 基于cglib进行对象复制
     * @author shengguofan
     * @param source 被复制的对象
     * @param clazz 复制对象类型
     * @return
     */
    public static <T> T copy(Object source, Class<T> clazz) {
        if (isEmpty(source)) {
            return null;
        }
        T target = instantiate(clazz);
        BeanCopier copier = BeanCopier.create(source.getClass(), clazz, false);
        copier.copy(source, target, null);
        return target;
    }

    /**
     * 基于cglib进行对象复制
     *
     * @param source 被复制的对象
     * @param target 复制对象
     * @return
     */
    public static void copy(Object source, Object target) {
        Assert.notNull(source, "The source must not be null");
        Assert.notNull(target, "The target must not be null");
        BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
        copier.copy(source, target, null);
    }

    /**
     * 基于cglib进行对象组复制
     * @author shengguofan
     * @param datas 被复制的对象数组
     * @param clazz 复制对象
     * @return
     */
    public static <T> List<T> copyByList(List<?> datas, Class<T> clazz) {
        if (isEmpty(datas)) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(datas.size());
        for (Object data : datas) {
            result.add(copy(data, clazz));
        }
        return result;
    }
    
    /**
     * 利用fastjson进行深拷贝
     * 
     * @author shengguofan
     * @param datas
     * @param clazz
     * @return
     */
    public static <T> List<T> deepCopyByList(List<?> datas, Class<T> clazz) {
        if (isEmpty(datas)) {
            return Collections.emptyList();
        }
        return JSON.parseArray(JSON.toJSONString(datas), clazz);
    }
    
    /**
     * 通过class实例化对象
     * @author shengguofan
     * @param clazz
     * @return
     * @throws RuntimeException
     */
    public static <T> T instantiate(Class<T> clazz) {
        Assert.notNull(clazz, "The class must not be null");
        try {
            return clazz.newInstance();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
        	ex.printStackTrace();
        }
		return null;
    }
    
    public static <T> boolean isEmpty(T t) {
        if (t == null) {
            return true;
        }
        return StringUtils.isEmpty(t.toString());
    }

}
