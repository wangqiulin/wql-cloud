package com.wql.cloud.basic.datasource.response.exception.handler;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.wql.cloud.basic.datasource.response.constant.BusinessEnum;
import com.wql.cloud.basic.datasource.response.exception.myexp.BusinessException;

/**
 * 异常处理机制
 * 
 * @author wangqiulin
 * @date 2018年5月12日
 */ 
@RestControllerAdvice
public class ErrorExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ErrorExceptionHandler.class);
	
	/**
	 * 统一异常处理
	 */
	@ExceptionHandler(value = { Exception.class})
	@ResponseStatus(HttpStatus.OK)
	public ModelAndView processException(Exception ex) {
		ModelAndView mv = new ModelAndView();
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attributes = new HashMap<String, Object>();
        //异常分类
        String code = null;
        String message = null;
        
        Map<String, String> map = getExceptionMessage(ex);
    	if(map == null) {
    		Map<String, String> map2 = getExceptionMessage(ex.getCause());
    		if(map2 == null) {
    			logger.error("【异常信息】", ex);
    			code = BusinessEnum.FAIL.getCode();
        		message = BusinessEnum.FAIL.getMessage();
    		} else {
    			code = map2.getOrDefault("code", BusinessEnum.FAIL.getCode());
        		message = map2.getOrDefault("message", BusinessEnum.FAIL.getMessage());
        		logger.error("参数异常：" + message);
    		}
    	} else {
    		code = map.getOrDefault("code", BusinessEnum.FAIL.getCode());
    		message = map.getOrDefault("message", BusinessEnum.FAIL.getMessage());
    		logger.error("参数异常：" + message);
    	}
        
        attributes.put("code", code);
        attributes.put("message", message);
        view.setAttributesMap(attributes);
        mv.setView(view); 
        return mv;
	}
	
	
	private Map<String, String> getExceptionMessage(Throwable e){
		Map<String, String> map = new HashMap<>();
		if(e instanceof IllegalArgumentException || e instanceof ValidationException) {
        	map.put("code", BusinessEnum.PARAM_FAIL.getCode());
        	map.put("message", e.getMessage());
        	return map;
        }
    	if(e instanceof BusinessException) {
        	BusinessException busExp = (BusinessException) e;
        	map.put("code", busExp.getCode());
        	map.put("message", busExp.getMessage());
        	return map;
        } 
		return null;
	}
	
	
}
