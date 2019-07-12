package com.zlw.commons.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD ,ElementType.PARAMETER})
public @interface Tel {

	/**
	 *<p> 是否验证电话区号,默认不验证（Tel.ZipCode.NO）,验证zipCode=Tel.ZipCode.YES。
	 * 带区号的电话号码格式匹配如：028-66666666,0826-3512566
	 * </p>
	 */
	public ZipCode []  zipCode() default ZipCode.NO ;
	public String message () default "电话号码格式错误";
	
	enum ZipCode{
		YES , NO  , ALL;
	}
	
}

