package com.wql.cloud.tool.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

    
    /**
     * 对象转map
     * 
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
		if (obj != null) {
			Field[] declaredFields = obj.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				field.setAccessible(true);
				if (field.get(obj) != null && StringUtils.isNotBlank(field.get(obj) + "")) {
					map.put(field.getName(), field.get(obj));
				}
			}
		}
		return map;
	}
	
    
    /**
	 * 将Map<String,Object>转换成Map<String, String>
	 * 
	 * @param objMap
	 * @return
	 */
	public static Map<String,String> objMap2StrMap(Map<String,Object> objMap){
		if(objMap == null) {
			return null;
		}
		Map<String,String> strMap = new HashMap<String, String>();
		for(String key:objMap.keySet()){
			if(null != objMap.get(key)){
				strMap.put(key, objMap.get(key).toString());
			} else {
				strMap.put(key, null);
			}
		}
		return strMap;
	}
    
    
    /**
     * 对象转表单（一般用于表单传值）
     * 
     * @param obj
     * @return
     * @throws Exception
     */
	public static MultiValueMap<String, Object> objectToMultiValueMap(Object obj) throws Exception {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		if (obj != null) {
			Field[] declaredFields = obj.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				field.setAccessible(true);
				if (field.get(obj) != null && StringUtils.isNotBlank(field.get(obj) + "")) {
					map.add(field.getName(), field.get(obj));
				}
			}
		}
		return map;
	}
    
	
	/**
	 * 从HttpServletRequest中获取数据（一般用于回调接收参数处理）
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();
        Set<Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();
        for (Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;
            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.toString().substring(1));
            } else {
                retMap.put(name, "");
            }
        }
        return retMap;
    }
	
	
}
