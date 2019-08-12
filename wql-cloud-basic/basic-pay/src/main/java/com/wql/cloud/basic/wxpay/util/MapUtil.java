package com.wql.cloud.basic.wxpay.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapUtil  {

	public static <K, T> T get(Map<K, T> map, Object key) {
		if (map != null) {
			return map.get(key);
		}
		return null;
	}

	/**
	 * 向key关联的list中放置value值
	 * 
	 * @param key
	 * @param value
	 * @param map
	 */
	public static <K, V> void put(Map<K, List<V>> map, K key, V value) {

		if (map != null) {
			List<V> list = map.get(key);
			if (list == null) {
				list = new LinkedList<V>();
				map.put(key, list);
			}

			list.add(value);
		}
	}

	/**
	 * 获取key关联的list，如果不存在就生成一个
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K, V> List<V> getList(Map<K, List<V>> map, K key) {
		if (map == null) {
			return null;
		}

		List<V> list = map.get(key);
		if (list == null) {
			list = new LinkedList<V>();
			map.put(key, list);
		}
		return list;
	}
	
	public static <J, K, V> Map<K, V> getSubMap(Map<J, Map<K, V>> map, J key) {
		if (map == null) {
			return null;
		}

		Map<K, V> map1 = map.get(key);
		if (map1 == null) {
			map1 = new HashMap<K, V>();
			map.put(key, map1);
		}
		return map1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Map<String,T> clone(Map<String,T> map) {
		Map remap = new HashMap();
		for (String key : map.keySet()) {
			remap.put(key, map.get(key));
		}
		return remap;
	}
	
	
	/**
	 * 将Map<String,Object>转换成Map<String,String>
	 * 
	 * @param objMap
	 * @return
	 */
	public static Map<String,String> objMap2StrMap(Map<String,Object> objMap){
		if(objMap==null)return null;
		Map<String,String> strMap=new HashMap<String, String>();
		for(String key:objMap.keySet()){
			if(null!=objMap.get(key)){
				strMap.put(key, objMap.get(key).toString());
			}else {
				strMap.put(key, null);
			}
		}
		return strMap;
	}
}
