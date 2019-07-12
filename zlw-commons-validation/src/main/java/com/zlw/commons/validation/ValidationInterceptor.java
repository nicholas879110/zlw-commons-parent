
package com.zlw.commons.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.zlw.commons.validation.annotation.Entity;
import com.zlw.commons.validation.annotation.PI;
import com.zlw.commons.validation.handler.ValidationHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import com.zlw.commons.core.ResultData;
import com.zlw.commons.validation.annotation.Validation;


public class ValidationInterceptor implements MethodInterceptor{

	
	private static final String IGNORE_ALL = "*";
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod() ;
		Validation validation = method.getAnnotation(Validation.class);
		if ( validation != null ){
			//忽略全部验证
			String ignore = validation.ignore() ;
			if( !StringUtils.isBlank(ignore) && StringUtils.trim(ignore).equals(IGNORE_ALL)){
				return invocation.proceed();
			}
			Object [] args = invocation.getArguments();
			ResultData<Object> data = null ;
			if( args != null && args.length >0 ){
				for( int i=0 ; i <args.length ; i++ ){	//遍历方法所有参数
					if( args[i] == null ){//参数为null
						//参数为空时直接进行参数验证
						data =  methodParametersValidation(method, args, i);
					}else{
						Entity entity = args[i].getClass().getAnnotation( Entity.class );
						if( entity !=null ){
							//被Entity注解 不仅进行参数验证，还需进行字段验证
							data = methodEntity(validation, method, args, i);
						} else {
							//未被注释Entity注解，直接进行参数验证
							data = methodParametersValidation(method, args, i);
						}
					}
					if( data != null ){
						return data ;
					}
				}
			}
		}
		return invocation.proceed();
	}

	
	private ResultData<Object>methodEntity(Validation validation, Method method, Object[] args, int i) throws IllegalAccessException {
		Annotation[][] paramsAnnotations = method.getParameterAnnotations();

		if (paramsAnnotations != null && paramsAnnotations.length > 0) {
			String piIgnore = null;
			String piOnly = null;
			
			for (int a = 0; a < paramsAnnotations.length; a++) {
				Field [] fields = args[a].getClass().getDeclaredFields() ;
				if (paramsAnnotations[a] == null || paramsAnnotations[a].length <= 0) {
					ResultData<Object> data = methodEntityValidation(validation, method, args, a);
					if( data != null ){
						return data ;
					}
				}
				for (int b = 0; b < paramsAnnotations[a].length; b++) {
					//参数有验证注解并且是PI忽略注解时，直接进入实体进行字段验证
					if( paramsAnnotations[a][b] instanceof PI){
						PI pi = (PI) paramsAnnotations[a][b] ;
						piIgnore = pi.ignore() ; 
						piOnly = pi.only() ;
						for (int j = 0; j < fields.length; j++) {
							boolean only = paramsOnly( piOnly , fields , j );
							if( !only ){//当前属性不是需要验证的
								continue ;
							}
							boolean isIgnore = paramsIgnore(fields, j, piIgnore);
							if( isIgnore ){
								continue ;
							}
							fields[j].setAccessible(true);
							Annotation [] annotation = fields[j].getDeclaredAnnotations();
							for(int k=0 ; k < annotation.length ; k ++){	//变量所有注解
								ValidationHandler handler = AbstractHandlerFactroy.getInstance( annotation , k );
								if( handler != null ){
									ResultData<Object> data = handler.validation(args[a], fields[j], annotation[k]);
									if( data != null ){
										return data ;
									}
								}
							}
						}
					}else{
						//参数有验证注解，但不是PI时，进行参数以及字段的校验
						ResultData<Object> data=methodParametersValidation(method,args,i);
						if (data!=null){
							return data;
						}
						return methodEntityValidation(validation, method, args, i);
					}
				}
			}
		}else{
			//参数无验证注解，进行字段验证
			return methodEntityValidation(validation, method, args, i);
		}
		
		
		return null ;
	}


	private ResultData<Object> methodEntityValidation(Validation validation, Method method, Object[] args, int i) throws IllegalAccessException {
		Field [] fields = args[i].getClass().getDeclaredFields() ;
		if( fields != null && fields.length >0 ){
			for (int j=0 ; j < fields.length ; j++ ){ //遍历对象所有变量
				//验证当前属性是否必须的验证的
				boolean only = only(validation, fields, j);
				if( !only ){//当前属性不是需要验证的
					continue ;
				}
				
				//判断当前属性是否忽略验证
				boolean isIgnore = ignore(validation, fields, j);
				if( isIgnore ){
					continue ;
				}
				
				fields[j].setAccessible(true);
				Annotation [] annotation = fields[j].getDeclaredAnnotations();
				for(int k=0 ; k < annotation.length ; k ++){	//变量所有注解
					ValidationHandler handler = AbstractHandlerFactroy.getInstance( annotation , k );
					if( handler != null ){
						ResultData<Object> data = handler.validation(args[i], fields[j], annotation[k]);
						if( data != null ){
							return data ;
						}
					}
				}
			}
		}
		return null;
	}



	private boolean paramsIgnore(Field[] fields, int j, String piIgnore) {
		if( !StringUtils.isBlank(piIgnore) ){
			if( !StringUtils.isBlank(piIgnore) && StringUtils.trim(piIgnore).equals( IGNORE_ALL) ){
				return Boolean.TRUE ;
			}
			String [] piIgnores = piIgnore.split(",");
			String fieldName = fields[j].getName();
			if( piIgnores !=null && piIgnores.length >0 ){
				for(int t=0 ; t < piIgnores.length ; t ++){
					if(StringUtils.equals( StringUtils.trim(fieldName) , StringUtils.trim(piIgnores[t] ) ) ){
						return Boolean.TRUE ;
					}
				}
			}
		}
		return Boolean.FALSE ;
	}

	private ResultData<Object> methodParametersValidation( Method method, Object[] args, int i ) {
		Annotation[][] annotation = method.getParameterAnnotations();
		Object value = args[i] ;
		if (annotation != null && annotation.length > 0) {
			for (int j = 0; j < annotation[i].length; j++) {
				Annotation anno = annotation[i][j];
				if( anno == null ){
					continue;
				}
				ValidationHandler handler = AbstractHandlerFactroy.getInstance( annotation[i], j );
				if (handler != null) {
					ResultData<Object> data = handler.validation( value , annotation[i][j] );
					if (data != null) {
						return data;
					}
				}
			}
		}
		return null;
	}

	private boolean ignore(Validation validation, Field[] fields, int j) {
		String fieldName = fields[j].getName();
		String ignore = validation.ignore();
		if( ! StringUtils.isEmpty(ignore) ){
			String [] ignores = ignore.split( "," );
			if( ignores !=null && ignores.length >0 ){
				for(int t=0 ; t < ignores.length ; t ++){
					if(StringUtils.equals( StringUtils.trim(fieldName) , StringUtils.trim(ignores[t] ) ) ){
						return Boolean.TRUE ;
					}
				}
			}
		}
		return Boolean.FALSE ;
	}
	
	
	
	private boolean only(Validation validation, Field[] fields, int j) {
		String only = validation.only() ;
		return paramsOnly(only, fields, j) ;
	}
	
	private boolean paramsOnly(String only, Field[] fields, int j) {
		if( StringUtils.isBlank(only) ){
			return Boolean.TRUE ;
		}else{
			return _only(only, fields, j);
		}
	}

	private boolean _only(String only, Field[] fields, int j) {
		String [] onlys = only.split(",");
		if( onlys != null && onlys.length >0 ){
			String fieldName = fields[j].getName();
			for (int k = 0; k < onlys.length; k++) {
				if(StringUtils.equals( StringUtils.trim(fieldName) , StringUtils.trim(onlys[k] ) ) ){
					return Boolean.TRUE ; 
				}
			}
		}
		return Boolean.FALSE ;
	}
	
	
}
