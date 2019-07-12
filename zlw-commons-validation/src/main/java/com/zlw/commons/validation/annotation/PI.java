package com.zlw.commons.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * PI ：Parameters Ignore 注解，作用于参数实体对象有效
 * @author fukui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface PI {

	/***忽略验证属性，多个用英文逗号分开，‘*’表示忽略全部验证*/
	public String ignore() default "";
	
	/***验证属性，多个用英文逗号分开，only 为空或者为‘*’表示全部验证；*/
	public String only()default "";
	
}
