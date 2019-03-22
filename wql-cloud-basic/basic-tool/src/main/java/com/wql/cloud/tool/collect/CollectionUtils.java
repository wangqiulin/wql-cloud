package com.wql.cloud.tool.collect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtils extends org.springframework.util.CollectionUtils {
	
	/**
	 * 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
	 * @param dataMap
	 * @return
	 */
	public static Map<String, Object> sortMapByASCII(Map<String, Object> dataMap) {
		Map<String, Object> resMap = new LinkedHashMap<>();
		if(dataMap != null) {
			List<Map.Entry<String, Object>> sortMap = new ArrayList<Map.Entry<String, Object>>(dataMap.entrySet());
			Collections.sort(sortMap, new Comparator<Map.Entry<String, Object>>() {
				public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});
			for (Map.Entry<String, Object> entry : sortMap) {
				resMap.put(entry.getKey(), entry.getValue());
			}
		}
		return resMap;
	}
	
}
