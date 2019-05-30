package com.wql.cloud.basic.push.channel.getui.vo;

import com.wql.cloud.basic.push.route.vo.BaseDataResult;

/**
 * 个推消息推送返回结果类
 */
public class GetuiDataResult extends BaseDataResult {

	private static final String RESULT_PREFIX = "{";
	private static final String RESULT_POSTFIX = "}";
	/**
	 * {result=ok, taskId=OSS-0315_8cd3abbdb47063dbd32db78630a80b4b, status=successed_offline}
	 * @param result
	 */
	public GetuiDataResult(String result) {
		this.data = result;
		int prefixPosition = result.indexOf(RESULT_PREFIX) + 1;
		int postfixPosition = result.indexOf(RESULT_POSTFIX);
		result = result.substring(prefixPosition, postfixPosition);
		String[] results = result.split(",");
		for (String rs: results) {
			String[] kvp = rs.split("=");
			if ("result".equals(kvp[0].trim())) {
				this.success = "ok".equals(kvp[1].trim());
			} else if ("status".equals(kvp[0].trim())) {
				this.message = kvp[1].trim();
			} 
		}
		
	}
	
}
