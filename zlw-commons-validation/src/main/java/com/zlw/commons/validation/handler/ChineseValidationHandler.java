package com.zlw.commons.validation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zlw.commons.validation.annotation.Chinese;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlw.commons.core.ErrorCode;
import com.zlw.commons.core.ResultData;


/**
 * 匹配字符串中所有字符为中文,作用于实体对象和方法参数
 * @author fukui
 */
public class ChineseValidationHandler implements ValidationHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String REX_CHINESE = "^[\u4E00-\u9FA5]$" ;
	private static Pattern pattern = null;
	
	static{
		pattern =  Pattern.compile( REX_CHINESE );
	}
	
	private static ChineseValidationHandler instance = new ChineseValidationHandler();
	
	private ChineseValidationHandler(){}
	
	public static ChineseValidationHandler getInstance(){
		return instance;
	}
	
	@Override
	public ResultData<Object> validation( Object  arg , Field  field , Annotation annotation ) throws IllegalArgumentException, IllegalAccessException {
		 String fieldValue = (String)field.get(arg);
		 return validation(fieldValue, annotation);
	}

	@Override
	public ResultData<Object> validation(Object value, Annotation annotation) {
		Chinese chinese = (Chinese)annotation ;
		 String fieldValue = (String)value;
		 ResultData<Object> data = null ;
		if (StringUtils.isBlank(fieldValue)) {
			data = ResultData.newResultData(ErrorCode.VALIDATION_ERROR , chinese.message());
			logger.debug( data.toString() );
			return data;
		}
		char [] c = fieldValue.toCharArray();
		for (int i = 0; i < c.length; i++) {
			Matcher matcher = pattern.matcher( String.valueOf(c[i]) ) ;
			 if( !matcher.matches() ){
				 data = ResultData.newResultData( ErrorCode.VALIDATION_ERROR , chinese.message() );
				 logger.debug( data.toString() );
				 return data;
			 }
		}
		return null;
	}
	
}
