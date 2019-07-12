package com.zlw.commons.validation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zlw.commons.validation.annotation.RegMatcher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlw.commons.core.ErrorCode;
import com.zlw.commons.core.ResultData;


/**
 * 自定义正则匹配,作用于实体对象和方法参数
 * @author fukui
 */
public class RegMatcherValidationHandler implements ValidationHandler {

	private Logger logger = LoggerFactory.getLogger(getClass()) ;
	
	private static RegMatcherValidationHandler instance = new RegMatcherValidationHandler();
	
	private RegMatcherValidationHandler(){}
	
	public static RegMatcherValidationHandler getInstance(){
		return instance;
	}
	
	@Override
	public ResultData<Object> validation( Object  arg , Field  field , Annotation annotation ) throws IllegalArgumentException, IllegalAccessException {
		Object fieldValue = (Object)field.get(arg);
		 return validation(fieldValue, annotation);
	}

	@Override
	public ResultData<Object> validation(Object value, Annotation annotation) {
		RegMatcher regMatcher = (RegMatcher)annotation ;
		String regex = regMatcher.regex() ;
		if( StringUtils.isBlank(regex) ){
			logger.warn("自定义正则匹配表达式为空");
			return null;
		}
		Pattern pattern =  Pattern.compile( regex );
		Object fieldValue = (Object)value;
		ResultData<Object>data = null;
		if( fieldValue == null ){
			data = ResultData.newResultData( ErrorCode.VALIDATION_ERROR , regMatcher.message() );
			logger.debug(data.toString());
			return data;
		}
		Matcher matcher = pattern.matcher( fieldValue.toString() ) ;
		 if( !matcher.matches() ){
				data = ResultData.newResultData( ErrorCode.VALIDATION_ERROR , regMatcher.message() );
				logger.debug(data.toString());
				return data;
		 }
		 return null;
	}
	
}
