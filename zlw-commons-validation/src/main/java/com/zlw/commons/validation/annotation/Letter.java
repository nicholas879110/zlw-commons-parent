package com.zlw.commons.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * 验证字符串是否是合法的英文字母（连续输入字母）
 * @author fukui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD ,ElementType.PARAMETER})
public @interface Letter {
	
	public String message () default "只允许输入英文字母";
	
}
