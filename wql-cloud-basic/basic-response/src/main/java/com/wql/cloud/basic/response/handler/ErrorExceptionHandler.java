package com.wql.cloud.basic.response.handler;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.wql.cloud.basic.response.constant.BaseConstant;
import com.wql.cloud.basic.response.constant.BusinessEnum;
import com.wql.cloud.basic.response.constant.BusinessException;

/**
 * 异常处理机制
 * 
 * @author wangqiulin
 * @date 2018年5月12日
 */ 
@ControllerAdvice
public class ErrorExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ErrorExceptionHandler.class);
	
	/**
	 * 自定义业务异常处理
	 */
	@ExceptionHandler({ BusinessException.class })
	@ResponseStatus(HttpStatus.OK)
	public ModelAndView processException(BusinessException busExp) {
		 ModelAndView mv = new ModelAndView();
         //使用FastJson提供的FastJsonJsonView视图返回，不需要捕获异常	
         FastJsonJsonView view = new FastJsonJsonView();
         Map<String, Object> attributes = new HashMap<String, Object>();
         attributes.put(BaseConstant.CODE, busExp.getCode());
         attributes.put(BaseConstant.MSG, busExp.getMsg());
         view.setAttributesMap(attributes);
         mv.setView(view); 
         return mv;
	}
	
	/**
	 * 参数异常处理
	 */
	@ExceptionHandler({ IllegalArgumentException.class })
	@ResponseStatus(HttpStatus.OK)
	public ModelAndView processException(IllegalArgumentException argEx) {
		 ModelAndView mv = new ModelAndView();
         FastJsonJsonView view = new FastJsonJsonView();
         Map<String, Object> attributes = new HashMap<String, Object>();
         attributes.put(BaseConstant.CODE, BusinessEnum.PARAM_FAIL.getCode());
         attributes.put(BaseConstant.MSG, StringUtils.isBlank(argEx.getMessage()) ? 
        		 BusinessEnum.PARAM_FAIL.getMsg() : argEx.getMessage());
         view.setAttributesMap(attributes);
         mv.setView(view); 
         return mv;
	}
	
	@ExceptionHandler({ ValidationException.class })
	@ResponseStatus(HttpStatus.OK)
	public ModelAndView processException(ValidationException validEx) {
		 ModelAndView mv = new ModelAndView();
         FastJsonJsonView view = new FastJsonJsonView();
         Map<String, Object> attributes = new HashMap<String, Object>();
         attributes.put(BaseConstant.CODE, BusinessEnum.PARAM_FAIL.getCode());
         attributes.put(BaseConstant.MSG, StringUtils.isBlank(validEx.getMessage()) ? 
        		 BusinessEnum.PARAM_FAIL.getMsg() : validEx.getMessage());
         view.setAttributesMap(attributes);
         mv.setView(view); 
         return mv;
	}
	
	
	/**
	 * 统一异常处理
	 */
	@ExceptionHandler({ Exception.class })
	@ResponseStatus(HttpStatus.OK)
	public ModelAndView processException(Exception exp) {
		logger.error("【系统异常】：", exp);
		ModelAndView mv = new ModelAndView();
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(BaseConstant.CODE, BusinessEnum.FAIL.getCode());
        attributes.put(BaseConstant.MSG, BusinessEnum.FAIL.getMsg());
        view.setAttributesMap(attributes);
        mv.setView(view); 
        return mv;
	}
	
}
