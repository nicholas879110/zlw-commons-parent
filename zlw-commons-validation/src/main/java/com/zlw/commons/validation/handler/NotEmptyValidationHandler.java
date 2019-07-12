package com.zlw.commons.validation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlw.commons.core.ErrorCode;
import com.zlw.commons.core.ResultData;
import com.zlw.commons.validation.annotation.NotEmpty;



/**
 * 非空验证,作用于实体对象和方法参数
 * @author fukui
 */
public class NotEmptyValidationHandler implements ValidationHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private static NotEmptyValidationHandler instance = new NotEmptyValidationHandler();
	
	private NotEmptyValidationHandler(){}
	
	public static NotEmptyValidationHandler getInstance(){
		return instance;
	}
	
	@Override
	public ResultData<Object> validation(Object  arg , Field  field , Annotation annotation ) throws IllegalArgumentException, IllegalAccessException {
		 Object fieldValue = field.get(arg);
		 return validation(fieldValue, annotation);
	}

	@Override
	public ResultData<Object> validation(Object value, Annotation annotation) {
		NotEmpty notNull = (NotEmpty)annotation ;
		 Object fieldValue = value;
		 ResultData<Object>data = null;
//		 if( fieldType.isAssignableFrom(String.class) ){
			 if((value==null) || StringUtils.isBlank(String.valueOf(fieldValue))){
				 data = ResultData.newResultData( ErrorCode.VALIDATION_ERROR , notNull.message() );
				 logger.debug(data.toString());
				 return data;
			 }
//		 }
		 return null;
	}

}
