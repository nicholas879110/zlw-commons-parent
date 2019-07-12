package com.zlw.commons.validation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.zlw.commons.core.ResultData;



/**
 * 定义验证接口
 * @author fukui
 */
public interface ValidationHandler {

	
	public ResultData<Object> validation( Object  arg , Field  field , Annotation annotation ) throws IllegalArgumentException, IllegalAccessException;
	
	
	public ResultData<Object> validation( Object  value , Annotation annotation );
	
}
