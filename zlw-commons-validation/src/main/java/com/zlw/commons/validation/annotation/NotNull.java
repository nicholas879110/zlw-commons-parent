package com.zlw.commons.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * 不允许为null
 * @author fukui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD ,ElementType.PARAMETER})
public @interface NotNull {
	
	public String message () default "不允许为空";
	
}
