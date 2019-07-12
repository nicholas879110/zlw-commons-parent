package com.zlw.commons.validation.handler;

import com.zlw.commons.core.ErrorCode;
import com.zlw.commons.core.ResultData;
import com.zlw.commons.validation.annotation.StringLength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 验证输入字符串长度（UTF-8编码的字节数）,作用于实体对象和方法参数
 * 
 * @author Administrator
 *
 */
public class StringLengthValidationHandler implements ValidationHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static StringLengthValidationHandler instance = new StringLengthValidationHandler();

	private StringLengthValidationHandler() {

	}

	public static StringLengthValidationHandler getInstance() {
		return instance;
	}

	@Override
	public ResultData<Object> validation(Object arg, Field field, Annotation annotation) throws IllegalArgumentException,
			IllegalAccessException {
		Object fieldValue = field.get(arg);
		return validation(fieldValue, annotation);
	}

	@Override
	public ResultData<Object> validation(Object value, Annotation annotation) {
		StringLength length = (StringLength) annotation;
		String fieldValue = value == null ? "" : String.valueOf(value);
		int max = length.max();
		int min = length.min();
		ResultData<Object> data = null;
		int inputLength = 0;
		inputLength = fieldValue.length();
		if (max < inputLength) {
			data = ResultData.newResultData(ErrorCode.VALIDATION_ERROR, length.message());
			logger.debug(data.toString());
			return data;
		}
		if (min > inputLength) {
			data = ResultData.newResultData(ErrorCode.VALIDATION_ERROR, length.message());
			logger.debug(data.toString());
			return data;
		}

		return null;
	}

}
