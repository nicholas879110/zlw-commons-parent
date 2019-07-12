package com.zlw.commons.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * 验证字符串是否是合法的汉字（连续输入汉字）
 * @author fukui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD ,ElementType.PARAMETER})
public @interface Chinese {
	
	public String message () default "只允许输入中文字符";
	
}
