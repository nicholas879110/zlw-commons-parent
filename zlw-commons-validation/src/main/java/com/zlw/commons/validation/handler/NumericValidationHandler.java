package com.zlw.commons.validation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zlw.commons.validation.annotation.Numeric;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlw.commons.core.ErrorCode;
import com.zlw.commons.core.ResultData;
import com.zlw.commons.validation.enums.NumericEnum;



/**
 * 非空验证,作用于实体对象和方法参数
 * @author fukui
 */
public class NumericValidationHandler implements ValidationHandler {
	
	//全数字
	private static final String NUMBER_REX = "[0-9]+";

	//非负整数 （正整数  +  0）
	private static final String POSITIVE_REX = "^\\d+$";
	
	//非正整数（负整数  +  0） 
	private static final String NEGATIVE_REX = "^((-\\d+)|(0+))$";
	
	//整数
	private static final String INTEGER_REX = "^-?\\d+$";
	
	//非负浮点数（正浮点数   +   0）
	private static final String NONNEGATIVE_FLOAT_REX = "^\\d+(\\.\\d+)?$";

	//非正浮点数（负浮点数   +   0）
	private static final String NONPOSITIVE_FLOAT_REX = "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$";
	
	//浮点数
	private static final String FLOAT_REX = "^(-?\\d+)(\\.\\d+)?$";
	
	private static Pattern NUMBER_PATTERN = null;
	private static Pattern POSITIVE_PATTERN = null;
	private static Pattern NEGATIVE_PATTERN = null;
	private static Pattern INTEGER_PATTERN = null;
	private static Pattern NONNEGATIVE_FLOAT_PATTERN = null;
	private static Pattern NONPOSITIVE_FLOAT_PATTERN = null;
	private static Pattern FLOAT_PATTERN = null;
	
	static {
		NUMBER_PATTERN =  Pattern.compile( NUMBER_REX  );
		POSITIVE_PATTERN =  Pattern.compile( POSITIVE_REX  );
		NEGATIVE_PATTERN =  Pattern.compile( NEGATIVE_REX  );
		INTEGER_PATTERN =  Pattern.compile( INTEGER_REX  );
		NONNEGATIVE_FLOAT_PATTERN =  Pattern.compile( NONNEGATIVE_FLOAT_REX  );
		NONPOSITIVE_FLOAT_PATTERN =  Pattern.compile( NONPOSITIVE_FLOAT_REX  );
		FLOAT_PATTERN =  Pattern.compile( FLOAT_REX  );
	}
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static NumericValidationHandler instance = new NumericValidationHandler();
	
	private NumericValidationHandler(){}
	
	public static NumericValidationHandler getInstance(){
		return instance;
	}
	
	@Override
	public ResultData<Object> validation(Object  arg , Field  field , Annotation annotation ) throws IllegalArgumentException, IllegalAccessException {
		 Object fieldValue = field.get(arg);
		 return validation(fieldValue, annotation);
	}

	@Override
	public ResultData<Object> validation(Object value, Annotation annotation) {
		Numeric numeric = (Numeric) annotation;
		String fieldValue = String.valueOf(value);
		ResultData<Object> data = null ;
		if ((value == null) || StringUtils.isBlank( fieldValue )) {
			data = ResultData.newResultData(ErrorCode.VALIDATION_ERROR , numeric.message());
			logger.debug(data.toString());
			return data;
		}
		Matcher matcher = null ;
		NumericEnum numericEnum = numeric.type() ;
		switch (numericEnum) {
		case POSITIVE:
			matcher = POSITIVE_PATTERN.matcher(fieldValue);
			break;
		case NEGATIVE:
			matcher = NEGATIVE_PATTERN.matcher(fieldValue);
			break;
		case INTEGER:
			matcher = INTEGER_PATTERN.matcher(fieldValue);
			break;
		case NONNEGATIVE_FLOAT:
			matcher = NONNEGATIVE_FLOAT_PATTERN.matcher(fieldValue);
			break;
		case NONPOSITIVE_FLOAT:
			matcher = NONPOSITIVE_FLOAT_PATTERN.matcher(fieldValue);
			break;
		case FLOAT:
			matcher = FLOAT_PATTERN.matcher(fieldValue);
			break;
		default:
			matcher = NUMBER_PATTERN.matcher(fieldValue);
		}
		if ( !matcher.matches() ) {
			 data =  ResultData.newResultData(ErrorCode.VALIDATION_ERROR , numeric.message() );
			 logger.debug(data.toString());
			 return data;
		}
		return null;
	}

}
