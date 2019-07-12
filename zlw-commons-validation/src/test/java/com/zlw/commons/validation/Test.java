package com.zlw.commons.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.zlw.commons.core.ResultData;
import com.zlw.commons.validation.annotation.Entity;
import com.zlw.commons.validation.handler.ValidationHandler;

public class Test {

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		
		
		User user = new User();
		user.setAddr("cheng du");
//		valid(user);
//		Object [] args1 = {user};
		String ignore [] = {"username","password"};
		
		
		ResultData<Object> result = PV303.validation( user , ignore );
		if( result !=null ){
			//验证不通过
		}
		
		System.out.println( PV303.validation( user , ignore ) );
		
	}
	
	public static ResultData<Object> valid(Object ... args) throws IllegalArgumentException, IllegalAccessException {
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) { // 遍历方法所有参数
				Entity entity = args[i].getClass().getAnnotation(Entity.class);
				if (entity != null) {
					Field[] fields = args[i].getClass().getDeclaredFields();
					if (fields != null && fields.length > 0) {
						for (int j = 0; j < fields.length; j++) { // 遍历对象所有变量
							fields[j].setAccessible(true);
							Annotation [] annotation = fields[j].getDeclaredAnnotations();
							for (int k = 0; k < annotation.length; k++) {
								ValidationHandler handler = AbstractHandlerFactroy.getInstance( annotation , k );
								if( handler != null ){
									ResultData<Object> data = handler.validation(args[i], fields[j], annotation[k]);
									return data ;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
}
