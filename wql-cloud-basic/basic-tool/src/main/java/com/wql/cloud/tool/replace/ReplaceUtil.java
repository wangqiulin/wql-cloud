package com.wql.cloud.tool.replace;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * 替换模板中的工具类
 */
public class ReplaceUtil {

	/**
	 * 替换模板中 '${}' 的内容
	 * @param content  模板内容
	 * @param dataMap map结构中的值
	 * @return
	 */
	public static String replace(String content, Map<String, String> dataMap){
		if(StringUtils.isBlank(content)){
			return null;
		}
		if(CollectionUtils.isEmpty(dataMap)) {
			return content;
		}
		for (Entry<String, String> entry : dataMap.entrySet()) {
			content = StringUtils.replace(content, "${"+entry.getKey()+"}", entry.getValue());
		}
		return content;
	}
	
}
