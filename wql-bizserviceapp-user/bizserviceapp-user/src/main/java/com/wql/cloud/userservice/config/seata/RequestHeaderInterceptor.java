//package com.wql.cloud.userservice.config.seata;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;
//
//import com.wql.cloud.basic.seata.constant.SeataConstant;
//
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import io.seata.core.context.RootContext;
//
///**
// * 业务层
// * 
// * Feign拦截器，把RootContext中的XID（XID用于标识一个局部事务属于哪个全局事务，需要在调用链路的上下文中传递）传递到上层调用链路
// * 
// */
//@Component
//public class RequestHeaderInterceptor implements RequestInterceptor {
//
//	@Override
//	public void apply(RequestTemplate template) {
//		String xid = RootContext.getXID();
//		if (StringUtils.isNotBlank(xid)) {
//			template.header(SeataConstant.XID_HEADER, xid);
//		}
//	}
//
//}
