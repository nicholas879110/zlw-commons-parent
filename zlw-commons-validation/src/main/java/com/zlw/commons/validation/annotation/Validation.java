package com.zlw.commons.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * 作用于需要验证的方法上，声明需要对该方法的参数中有标记为com.zlw.commons.validation.annotation.Entity注解的实体验证
 * 
 * <pre>
 * Validation 属性ignore表示忽略验证属性，only表示验证属性；当两个属性同事出现时先处理only，在处理ignore
 * </pre>
 * 
 * 
 * @author fukui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD } )
public @interface Validation {
	
	/***忽略验证属性，多个用英文逗号分开，‘*’表示忽略全部验证*/
	public String ignore() default "";
	
	/***验证属性，多个用英文逗号分开，only 为空或者为‘*’表示全部验证；*/
	public String only()default "";
	
}

