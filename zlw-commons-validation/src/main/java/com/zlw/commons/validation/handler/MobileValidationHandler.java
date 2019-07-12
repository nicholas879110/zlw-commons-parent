package com.zlw.commons.validation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zlw.commons.validation.annotation.Mobile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlw.commons.core.ErrorCode;
import com.zlw.commons.core.ResultData;


/**
 * 手机号码验证,作用于实体对象和方法参数
 * @author fukui
 */
public class MobileValidationHandler implements ValidationHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String REX_MOBILE = "^((13[0-9])|(14[5,7])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$" ;
	private static Pattern pattern = null;
	
	static{
		pattern =  Pattern.compile( REX_MOBILE  );
	}
	
	private static MobileValidationHandler instance = new MobileValidationHandler();
	
	private MobileValidationHandler(){}
	
	public static MobileValidationHandler getInstance(){
		return instance;
	}
	
	@Override
	public ResultData<Object> validation( Object  arg , Field  field , Annotation annotation ) throws IllegalArgumentException, IllegalAccessException {
		 String fieldValue = (String)field.get(arg);
		 return validation(fieldValue, annotation);
	}

	@Override
	public ResultData<Object> validation(Object value, Annotation annotation) {
		Mobile mobile = (Mobile)annotation ;
		 String fieldValue = (String)value;
		 ResultData<Object>data = null;
		 if( StringUtils.isBlank( fieldValue ) ){
			 data =  ResultData.newResultData( ErrorCode.VALIDATION_ERROR , mobile.message() );
			 logger.debug(data.toString());
			 return data;
		 }
		 Matcher matcher = pattern.matcher(  fieldValue ) ;
		 if( !matcher.matches() ){
			 data =  ResultData.newResultData( ErrorCode.VALIDATION_ERROR , mobile.message() );
			 logger.debug(data.toString());
			 return data;
		 }
		 return null;
	}

}
