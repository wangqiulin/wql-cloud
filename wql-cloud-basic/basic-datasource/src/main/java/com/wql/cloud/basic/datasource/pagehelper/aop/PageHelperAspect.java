package com.wql.cloud.basic.datasource.pagehelper.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wql.cloud.basic.datasource.pagehelper.annotation.ExtPageHelper;

/**
 * 统一处理分页
 */
@Component
@Aspect
public class PageHelperAspect {
 
	@Pointcut("@annotation(com.wql.cloud.basic.datasource.pagehelper.annotation.ExtPageHelper)")
	public void annotationPointcut() {
	}
 
	@SuppressWarnings("unchecked")
	@Around("annotationPointcut()")
	public <T> PageInfo<T>  doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		// 初始化参数
		int page = 0;
		int pageSize = 0;
		// 获得当前访问的class
		Class<?> className = joinPoint.getTarget().getClass();
		// 获得访问的方法名
		String methodName = joinPoint.getSignature().getName();
		// 得到方法的参数的类型
		Class<?>[] argClass = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
		// 得到方法的参数的类型
		String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
		// 获取参数 page 和 pageSize
		Object[] args = joinPoint.getArgs();
		int i = 0;
		for (String pName : parameterNames) {
			if (pName.equals("page")) {
				if(args[i] != null) {
					page = (int) args[i];
				}
			}
			if (pName.equals("pageSize")) {
				if(args[i] != null) {
					pageSize = (int) args[i];
				}
			}
			i++;
		}
		// 设置分页
		PageHelper.startPage(page, pageSize);
		try {
			// 得到访问的方法对象
			Method method = className.getMethod(methodName, argClass);
			method.setAccessible(true);
			// 判断是否存在@ExtDataSource注解
			if (method.isAnnotationPresent(ExtPageHelper.class)) {
				ExtPageHelper annotation = method.getAnnotation(ExtPageHelper.class);
				// 取出注解中的数据源名
				pageSize = annotation.pageSize();
			}
		} catch (Exception e) {
			throw new RuntimeException("动态设置分页属性报错", e);
		}
		List<T> reuslt = new ArrayList<T>();
		try {
			reuslt = (List<T>) joinPoint.proceed();
		} catch (Exception e) {
			throw new RuntimeException("返回类型不为List", e);
		}
		PageInfo<T> pageList = new PageInfo<T>(reuslt);
		return pageList;
	}
	
}
