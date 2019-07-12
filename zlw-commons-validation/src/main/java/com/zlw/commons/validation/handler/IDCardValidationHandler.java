package com.zlw.commons.validation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlw.commons.core.ErrorCode;
import com.zlw.commons.core.ResultData;
import com.zlw.commons.validation.annotation.IDCard;


/**
 * 匹配字符串是否为身份证号码,作用于实体对象和方法参数
 * @author fukui
 */
public class IDCardValidationHandler implements ValidationHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String REX_IDCARD = "^[1-9](\\d{5})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$" ;
	private static Pattern pattern = null;
	
	static{
		pattern =  Pattern.compile( REX_IDCARD );
	}
	
	private static IDCardValidationHandler instance = new IDCardValidationHandler();
	
	private IDCardValidationHandler(){}
	
	public static IDCardValidationHandler getInstance(){
		return instance;
	}
	
	@Override
	public ResultData<Object> validation( Object  arg , Field  field , Annotation annotation ) throws IllegalArgumentException, IllegalAccessException {
		String fieldValue = (String)field.get(arg);
		 return validation(fieldValue, annotation);
	}

	@Override
	public ResultData<Object> validation(Object value, Annotation annotation) {
		IDCard idCard = (IDCard)annotation ;
		String fieldValue = (String)value;
		ResultData<Object>data = null ;
		if (StringUtils.isBlank(fieldValue)) {
			data = ResultData.newResultData(ErrorCode.VALIDATION_ERROR , idCard.message());
			logger.debug(data.toString());
			return data;
		}
		Matcher matcher = pattern.matcher(fieldValue);
		if (!matcher.matches()) {
			data = ResultData.newResultData(ErrorCode.VALIDATION_ERROR , idCard.message());
			logger.debug(data.toString());
			return data;
		}
		 return null;
	}
	
}
