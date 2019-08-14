package com.wql.cloud.basic.datasource.response.handler;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.wql.cloud.basic.datasource.response.constant.BaseConstant;
import com.wql.cloud.basic.datasource.response.constant.BusinessEnum;
import com.wql.cloud.basic.datasource.response.constant.BusinessException;

/**
 * 异常处理机制
 * 
 * @author wangqiulin
 * @date 2018年5月12日
 */ 
@RestControllerAdvice
//@ControllerAdvice
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
        Throwable cause = ex.getCause();
        if(cause != null) {
        	Throwable cause2 = cause.getCause();
            if(cause2 != null) {
            	if(cause2 instanceof IllegalArgumentException || cause2 instanceof ValidationException) {
                	code = BusinessEnum.PARAM_FAIL.getCode();
                	message = cause2.getMessage();
                } else if(cause2 instanceof BusinessException) {
                	BusinessException businessException = (BusinessException)cause2;
                	code = businessException.getCode();
                	message = businessException.getMessage();
                } else {
                	logger.error("【异常信息】", ex);
                }
            }
        }
        attributes.put(BaseConstant.CODE, StringUtils.isBlank(code) ? BusinessEnum.FAIL.getCode() : code);
        attributes.put(BaseConstant.MSG, StringUtils.isBlank(message) ? BusinessEnum.FAIL.getMessage() : message);
        view.setAttributesMap(attributes);
        mv.setView(view); 
        return mv;
	}
	
}
