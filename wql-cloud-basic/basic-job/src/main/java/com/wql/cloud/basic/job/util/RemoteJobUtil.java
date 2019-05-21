package com.wql.cloud.basic.job.util;

import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.model.TriggerParam;
import com.xxl.rpc.remoting.invoker.call.CallType;
import com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean;
import com.xxl.rpc.remoting.invoker.route.LoadBalance;
import com.xxl.rpc.remoting.net.NetEnum;
import com.xxl.rpc.serialize.Serializer;

/**
 * 调用Job的Util
 */
@Component
public class RemoteJobUtil {

	
	/**
	 * 调用远程方法
	 * 
	 * @param serverUrl Job服务器Url
	 * @param accessToken 服务器Token
	 * @param jobId 需要调用的JobID
	 * @throws Exception
	 */
	public static void triggerJob(String serverUrl, String accessToken, Integer jobId) throws Exception {
		ExecutorBiz adminBiz = getExecutorBiz(serverUrl, accessToken, jobId);
		//设置参数和jobId
		TriggerParam triggerParam = new TriggerParam();
		triggerParam.setJobId(jobId);
		//执行
        adminBiz.run(triggerParam);
	}
	
	
	/**
	 * 带参数的执行job
	 * 
	 * @param serverUrl Job服务器Url
	 * @param accessToken 服务器Token
	 * @param jobId 需要调用的JobID
	 * @param executorParams 参数
	 * @throws Exception
	 */
	public static void triggerJob(String serverUrl, String accessToken, Integer jobId, String executorParams) throws Exception {
		ExecutorBiz adminBiz = getExecutorBiz(serverUrl, accessToken, jobId);
		//设置参数和jobId
		TriggerParam triggerParam = new TriggerParam();
		triggerParam.setJobId(jobId);
		triggerParam.setExecutorParams(executorParams);
		//执行
        adminBiz.run(triggerParam);
	}

	
	private static ExecutorBiz getExecutorBiz(String serverUrl, String accessToken, Integer jobId) {
		if (serverUrl == null || "".equals(serverUrl)) {
			throw new RuntimeException("没有指定Job服务器");
		}
		if (jobId == null) {
			throw new RuntimeException("没有指定需要调用Job");
		}
		String addressUrl = "";
		if ('/' == serverUrl.charAt(serverUrl.length() - 1)) {
			addressUrl = serverUrl.concat(AdminBiz.MAPPING);
		} else {
			addressUrl = serverUrl.concat("/").concat(AdminBiz.MAPPING);
		}
		ExecutorBiz adminBiz = (ExecutorBiz) new XxlRpcReferenceBean(
                NetEnum.NETTY_HTTP,
                Serializer.SerializeEnum.HESSIAN.getSerializer(),
                CallType.SYNC,
                LoadBalance.ROUND,
                AdminBiz.class,
                null,
                10000,
                addressUrl,
                accessToken,
                null,
                null
        ).getObject();
		return adminBiz;
	}
	
}
