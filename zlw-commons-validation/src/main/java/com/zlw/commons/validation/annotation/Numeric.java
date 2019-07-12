package com.zlw.commons.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.zlw.commons.validation.enums.NumericEnum;


/**
 * 数字验证
 * @author fukui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD ,ElementType.PARAMETER})
public @interface Numeric {

	/**
	 * 默认全数字类型
	 */
	public NumericEnum type () default NumericEnum.NUMERIC;
	
	public String message () default "无效的数字类型";
	
}
