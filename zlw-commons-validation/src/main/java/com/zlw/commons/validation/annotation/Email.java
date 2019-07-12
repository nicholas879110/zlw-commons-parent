package com.zlw.commons.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 验证邮箱正确性
 * ignoreNull:参数为是否忽略空值，默认为false不忽略，true表示忽略空值
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD ,ElementType.PARAMETER})
public @interface Email {

	public boolean ignoreNull() default false; //是否忽略空值
	
	public String message () default "邮箱格式错误";
	
}
