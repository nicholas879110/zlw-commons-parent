package com.zlw.commons.validation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.zlw.commons.validation.annotation.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlw.commons.core.ErrorCode;
import com.zlw.commons.core.ResultData;


/**
 * 非null验证,作用于实体对象和方法参数
 * @author fukui
 */
public class NotNullValidationHandler implements ValidationHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private static NotNullValidationHandler instance = new NotNullValidationHandler();
	
	
	private NotNullValidationHandler(){
		
	}
	
	
	public static NotNullValidationHandler getInstance(){
		return instance;
	}
	
	@Override
	public ResultData<Object> validation(Object  arg , Field  field , Annotation annotation ) throws IllegalArgumentException, IllegalAccessException {
		 Object fieldValue = field.get(arg);
		 return validation(fieldValue, annotation);
	}
	
	
	@Override
	public ResultData<Object> validation( Object  object , Annotation annotation )  {
		NotNull notNull = (NotNull)annotation ;
		ResultData<Object>data = null;
		 if( object == null ){
			 data = ResultData.newResultData( ErrorCode.VALIDATION_ERROR , notNull.message() );
			 logger.debug(data.toString());
			 return data;
		 }
		 return null;
	}
	
	

}
