package com.zlw.commons.validation.handler;

import com.zlw.commons.core.ErrorCode;
import com.zlw.commons.core.ResultData;
import com.zlw.commons.validation.annotation.Email;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 验证email,作用于实体对象和方法参数
 * @author fukui
 */
public class EmailValidationHandler implements ValidationHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String REX_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*" ;
	private static Pattern pattern = null;
	
	static{
		pattern =  Pattern.compile( REX_EMAIL );
	}
	
	private static EmailValidationHandler instance = new EmailValidationHandler();
	
	private EmailValidationHandler(){}
	
	public static EmailValidationHandler getInstance(){
		return instance;
	}
	
	@Override
	public ResultData<Object> validation( Object  arg , Field  field , Annotation annotation ) throws IllegalArgumentException, IllegalAccessException {
		 String fieldValue = (String)field.get(arg);
		 return validation(fieldValue, annotation);
	}

	@Override
	public ResultData<Object> validation(Object value, Annotation annotation) {
		 Email email = (Email)annotation ;
		 String fieldValue = (String)value;
		 
		 if(StringUtils.isBlank( fieldValue ) && email.ignoreNull()){
			 return null;
		 }
		 ResultData<Object>data=null;
		 if( StringUtils.isBlank( fieldValue ) ){
			 data= ResultData.newResultData( ErrorCode.VALIDATION_ERROR , email.message() );
			 logger.debug( data.toString() );
			 return data ;
		 }
		 Matcher matcher = pattern.matcher( fieldValue ) ;
		 if( !matcher.matches() ){
			 data= ResultData.newResultData( ErrorCode.VALIDATION_ERROR , email.message() );
			 logger.debug( data.toString() );
			 return data ;
		 }
		 return null;
	}

}
