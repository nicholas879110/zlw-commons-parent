package com.zlw.commons.validation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zlw.commons.validation.annotation.Tel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlw.commons.core.ErrorCode;
import com.zlw.commons.core.ResultData;


/**
 * 电话号码验证,作用于实体对象和方法参数
 * @author fukui
 */
public class TelValidationHandler implements ValidationHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	//带区号的电话号码
	private static final String REX_TEL_ZIP_CODE = "^[0][1-9]{2,3}-[1-9]{1}[0-9]{6,7}$" ;
	//不带区号
	private static final String REX_TEL = "^[1-9]{1}[0-9]{6,7}$" ;
	private static Pattern pattern = null;
	private static Pattern pattern_zip_code = null;
	
	static{
		pattern =  Pattern.compile( REX_TEL  );
		pattern_zip_code =  Pattern.compile( REX_TEL_ZIP_CODE  );
	}
	
	private static TelValidationHandler instance = new TelValidationHandler();
	
	private TelValidationHandler(){}
	
	public static TelValidationHandler getInstance(){
		return instance;
	}
	
	@Override
	public ResultData<Object> validation( Object  arg , Field  field , Annotation annotation ) throws IllegalArgumentException, IllegalAccessException {
		 String fieldValue = (String)field.get(arg);
		 return validation(fieldValue, annotation);
	}

	@Override
	public ResultData<Object> validation(Object value, Annotation annotation) {
		Tel tel = (Tel)annotation ;
		 String fieldValue = (String)value;
		 ResultData<Object>data = null;
		 if( StringUtils.isBlank( fieldValue ) ){
			 data =  ResultData.newResultData( ErrorCode.VALIDATION_ERROR , tel.message() );
			 logger.debug(data.toString());
			 return data;
		 }
		 Tel.ZipCode [] x = tel.zipCode();
		 Matcher matcher = null ;
		switch (x[0]) {
		case YES:
			matcher = pattern_zip_code.matcher(  fieldValue ) ;
			break;
		case NO:
			matcher = pattern.matcher(  fieldValue ) ;
			break;
		default:
			matcher = pattern.matcher(  fieldValue ) ;
			if (!matcher.matches()) {
				matcher = pattern_zip_code.matcher(  fieldValue ) ;
			}
			break;
		}
		if (!matcher.matches()) {
			 data =  ResultData.newResultData(ErrorCode.VALIDATION_ERROR , tel.message() );
			 logger.debug(data.toString());
			 return data;
		}
		 return null;
	}

}
