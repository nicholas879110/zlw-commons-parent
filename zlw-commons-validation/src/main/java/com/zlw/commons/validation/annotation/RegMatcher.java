package com.zlw.commons.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * 自定义正则匹配，在当前提供的注解不能满足要求的情况下使用。建议不要在项目中过多使用
 * @author fukui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD ,ElementType.PARAMETER})
public @interface RegMatcher {
	
	public String regex ()  ;
	public String message () default "输入参数验证失败";
	
}
