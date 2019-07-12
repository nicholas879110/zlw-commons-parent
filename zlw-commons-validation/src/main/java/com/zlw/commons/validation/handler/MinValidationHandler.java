package com.zlw.commons.validation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import com.zlw.commons.validation.annotation.Min;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlw.commons.core.ErrorCode;
import com.zlw.commons.core.ResultData;


/**
 * 最小值验证,作用于实体对象和方法参数
 * @author fukui
 */
public class MinValidationHandler implements ValidationHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static MinValidationHandler instance = new MinValidationHandler();
	
	private static final String MIN_SET_ERROR = "没有设置最小限制值" ;
	private static final String MIN_INPUT_ERROR = "最小值输入数值无效" ;
	
	private MinValidationHandler(){
		
	}
	
	public static MinValidationHandler getInstance(){
		return instance;
	}
	
	@Override
	public ResultData<Object> validation(Object  arg , Field  field , Annotation annotation ) throws IllegalArgumentException, IllegalAccessException {
		 Object fieldValue = field.get(arg);
		 return validation(fieldValue, annotation);
	}

	@Override
	public ResultData<Object> validation(Object object, Annotation annotation) {
		Min min = (Min)annotation ;
		 Object fieldValue = object;
		 String value = min.value() ;
		 ResultData<Object>data = null;
		 if( StringUtils.isBlank(value) ){
			 data =  ResultData.newResultData( ErrorCode.VALIDATION_ERROR , MIN_SET_ERROR );
			 logger.debug( data.toString() );
			 return data;
		 }else if ( fieldValue == null || StringUtils.isBlank(fieldValue.toString()) ){
			 data =  ResultData.newResultData( ErrorCode.VALIDATION_ERROR , MIN_INPUT_ERROR );
			 logger.debug( data.toString() );
			 return data;
		 }
		 BigDecimal set = NumberUtils.createBigDecimal(value.trim()) ;
		 BigDecimal input = NumberUtils.createBigDecimal( fieldValue.toString().trim() );
		 
		 if( input.compareTo( set ) < 0 ){
			 data = ResultData.newResultData( ErrorCode.VALIDATION_ERROR , min.message() );
			 logger.debug(data.toString());
			 return data ;
		 }
		 return null;
	}
	
}
