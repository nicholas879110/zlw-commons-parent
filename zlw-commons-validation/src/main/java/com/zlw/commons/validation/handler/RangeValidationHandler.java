package com.zlw.commons.validation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import com.zlw.commons.validation.annotation.Range;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlw.commons.core.ErrorCode;
import com.zlw.commons.core.ResultData;


/**
 * 范围验证,作用于实体对象和方法参数
 * @author fukui
 */
public class RangeValidationHandler implements ValidationHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static RangeValidationHandler instance = new RangeValidationHandler();
	
	private static final String RANGE_SET_ERROR = "没有设置最大限制或最小限制值" ;
	private static final String RANGE_INPUT_ERROR = "输入数值没有在指定范围" ;
	
	private RangeValidationHandler(){
		
	}
	
	public static RangeValidationHandler getInstance(){
		return instance;
	}
	
	@Override
	public ResultData<Object> validation(Object  arg , Field  field , Annotation annotation ) throws IllegalArgumentException, IllegalAccessException {
		 Object fieldValue = field.get(arg);
		 return validation(fieldValue, annotation);
	}

	@Override
	public ResultData<Object> validation(Object value, Annotation annotation) {
		Range range = (Range)annotation ;
		 Object fieldValue = value;
		 String max = range.max() ;
		 String min = range.min() ;
		 ResultData<Object>data = null;
		 if( StringUtils.isBlank(min) ||  StringUtils.isBlank(max) ){
			 logger.error( RANGE_SET_ERROR );
			 data =  ResultData.newResultData( ErrorCode.VALIDATION_ERROR , RANGE_SET_ERROR );
			 logger.debug(data.toString());
			 return data;
		 }else if ( fieldValue == null || StringUtils.isBlank(fieldValue.toString()) ){
			 data =  ResultData.newResultData( ErrorCode.VALIDATION_ERROR , RANGE_INPUT_ERROR );
			 logger.debug(data.toString());
			 return data;
		 }
		 BigDecimal set_min = NumberUtils.createBigDecimal( min ) ;
		 BigDecimal set_max = NumberUtils.createBigDecimal( max ) ;
		 BigDecimal input = NumberUtils.createBigDecimal( fieldValue.toString().trim() );
		 if( (input.compareTo( set_min ) < 0) ||  (input.compareTo( set_max ) > 0) ){
			 data =  ResultData.newResultData( ErrorCode.VALIDATION_ERROR , range.message() );
			 logger.debug(data.toString());
			 return data;
		 }
		return null;
	}
	
}
