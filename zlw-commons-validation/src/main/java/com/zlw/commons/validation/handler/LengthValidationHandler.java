package com.zlw.commons.validation.handler;

import com.zlw.commons.core.ErrorCode;
import com.zlw.commons.core.ResultData;
import com.zlw.commons.validation.annotation.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 验证输入字符串长度（UTF-8编码的字节数）,作用于实体对象和方法参数
 * 
 * @author Administrator
 *
 */
public class LengthValidationHandler implements ValidationHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static LengthValidationHandler instance = new LengthValidationHandler();

	private static final String CHAR_SET = "UTF-8";

	// private static final String RANGE_INPUT_ERROR = "输入的字符串长度为空" ;

	private LengthValidationHandler() {

	}

	public static LengthValidationHandler getInstance() {
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
		Length length = (Length) annotation;
		Object fieldValue = value;
		int max = length.max();
		int min = length.min();
		ResultData<Object> data = null;
		if (fieldValue == null) {
			data = ResultData.newResultData(ErrorCode.VALIDATION_ERROR, length.message());
			logger.debug(data.toString());
			return data;
		}
		int inputLength = 0;
		try {
			inputLength = fieldValue.toString().getBytes(CHAR_SET).length;
		} catch (UnsupportedEncodingException e) {
			logger.error("不支持UTF-8编码", e);
		}
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
