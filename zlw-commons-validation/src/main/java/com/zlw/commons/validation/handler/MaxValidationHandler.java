package com.zlw.commons.validation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import com.zlw.commons.validation.annotation.Max;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlw.commons.core.ErrorCode;
import com.zlw.commons.core.ResultData;


/**
 * 最大值验证,作用于实体对象和方法参数
 * @author fukui
 */
public class MaxValidationHandler implements ValidationHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static MaxValidationHandler instance = new MaxValidationHandler();
	
	private static final String MAX_SET_ERROR = "没有设置最大限制值" ;
	private static final String MAX_INPUT_ERROR = "最大值输入数值无效" ;
	
	private MaxValidationHandler(){
		
	}
	
	public static MaxValidationHandler getInstance(){
		return instance;
	}
	
	@Override
	public ResultData<Object> validation(Object  arg , Field  field , Annotation annotation ) throws IllegalArgumentException, IllegalAccessException {
		 Object fieldValue = field.get(arg);
		 return validation(fieldValue, annotation);
	}

	@Override
	public ResultData<Object> validation(Object object, Annotation annotation) {
		Max max = (Max)annotation ;
		 Object fieldValue = object;
		 String value = max.value() ;
		 ResultData<Object>data = null;
		 if( StringUtils.isBlank(value) ){
			 data = ResultData.newResultData( ErrorCode.VALIDATION_ERROR , MAX_SET_ERROR );
			 logger.debug( data.toString() );
			 return data;
		 }else if ( fieldValue == null || StringUtils.isBlank(fieldValue.toString()) ){
			 logger.error( MAX_SET_ERROR );
			 return ResultData.newResultData( ErrorCode.VALIDATION_ERROR , MAX_INPUT_ERROR );
		 }
		 BigDecimal set = NumberUtils.createBigDecimal(value.trim()) ;
		 BigDecimal input = NumberUtils.createBigDecimal( fieldValue.toString().trim() );
		 if( input.compareTo( set ) > 0 ){
			 data =  ResultData.newResultData( ErrorCode.VALIDATION_ERROR , max.message() );
			 logger.debug( data.toString() );
			 return data;
		 }
		 return null;
	}
	
}
