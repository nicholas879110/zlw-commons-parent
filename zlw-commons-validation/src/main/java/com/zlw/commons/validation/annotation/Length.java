package com.zlw.commons.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * 验证字符串长度,字符串不能为 null
 * @author fukui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD ,ElementType.PARAMETER})
public @interface Length {
	
	public int min() default 0 ;
	public int max() default Integer.MAX_VALUE ;
	public String message () default "输入字符串未在制定范围内";
	
}
