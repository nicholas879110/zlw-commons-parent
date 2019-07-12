package com.zlw.commons.validation;



import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.zlw.commons.validation.annotation.Entity;
import com.zlw.commons.validation.handler.ValidationHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlw.commons.core.ErrorCode;
import com.zlw.commons.core.ResultData;


/**
 * 
 * 参数验证工具类
 * @author fukui
 *
 */
public abstract class PV303 {
	

	private static Logger logger = LoggerFactory.getLogger(PV303.class);
	
	

	/**
	 * 验证实体对象，若验证失败抛ValidationException异常，错误代码，错误信息描述均可以在异常里面获取
	 * @param args 需要验证的对象
	 * @param ignores 需要忽略的属性数组
	 */
	public static void validationByException( Object args , String ... ignores ){
		ResultData<Object>data = validation( args , ignores ) ;
		if( data != null ){
			throw new ValidationException( data.getCode() , data.getMessage() ) ;
		}
	}
	
	public  static void validationByException( Object args  ) {
		validationByException(args,new String[]{});
	}
	
	public  static <T>ResultData<T> validation( Object args  ) {
		return validation(args,new String[]{});
	}
	
	
	public  static <T>ResultData<T> validationOnly( Object args ) {
		return validation(args,new String[]{});
	}
	
	
	/**
	 * 验证实体对象，若验证失败抛ValidationException异常，错误代码，错误信息描述均可以在异常里面获取
	 * @param args 需要验证的对象
	 * @param onlys 需要验证的属性数组
	 */
	public static void validationOnlyByException( Object args , String ... onlys ){
		ResultData<Object>data = validationOnly( args , onlys ) ;
		if( data != null ){
			throw new ValidationException( data.getCode() , data.getMessage() ) ;
		}
	}
	
	
	/**
	 * 验证实体对象，结果已ResultData返回；ResultData==null 则通过验证，否则验证失败
	 * @param args 需要验证的对象
	 * @param onlys 需要验证的属性数组
	 */
	@SuppressWarnings("unchecked")
	public  static <T>ResultData<T> validationOnly( Object args , String ... onlys  ) {
		Field[] fields = args.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (int j = 0; j < fields.length; j++) { // 遍历对象所有变量
				boolean only = only(onlys, fields, j) ;
				if( !only ){
					continue ;
				}
				fields[j].setAccessible(true);
				Annotation [] annotation = fields[j].getDeclaredAnnotations();
				for (int k = 0; k < annotation.length; k++) {
					ValidationHandler handler = AbstractHandlerFactroy.getInstance( annotation , k );
					if( handler != null ){
						try {
							ResultData<T> data =  (ResultData<T>) handler.validation(args, fields[j], annotation[k]);
							if( data != null ){
								logger.debug(data.toString());
								return data ;
							}
						} catch (IllegalArgumentException | IllegalAccessException e) {
							logger.error("参数验证失败：" ,e );
							return ResultData.newResultData(ErrorCode.VALIDATION_ERROR , "参数验证失败");
						}
					}
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public  static <T>ResultData<T> validation( Object args , String ... ignores ) {
		Field[] fields = args.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (int j = 0; j < fields.length; j++) { // 遍历对象所有变量
				boolean isIgnore = ignore(fields, j, ignores) ;
				if( isIgnore ){
					continue ;
				}
				fields[j].setAccessible(true);
				Annotation [] annotation = fields[j].getDeclaredAnnotations();
				for (int k = 0; k < annotation.length; k++) {
					ValidationHandler handler = AbstractHandlerFactroy.getInstance( annotation , k );
					if( handler != null ){
						try {
							ResultData<T> data =  (ResultData<T>) handler.validation(args, fields[j], annotation[k]);
							if( data != null ){
								logger.debug(data.toString());
								return data ;
							}
						} catch (IllegalArgumentException | IllegalAccessException e) {
							logger.error("参数验证失败：" ,e );
							return ResultData.newResultData(ErrorCode.VALIDATION_ERROR , "参数验证失败");
						}
					}
				}
			}
		}
		return null;
	}
	
	
	private static boolean ignore(Field[] fields, int j , String ... ignores) {
		String fieldName = fields[j].getName();
			if( ignores !=null && ignores.length >0 ){
				for(int t=0 ; t < ignores.length ; t ++){
					if(StringUtils.equals( StringUtils.trim(fieldName) , StringUtils.trim(ignores[t] ) ) ){
						return Boolean.TRUE ;
					}
				}
			}
		
		return Boolean.FALSE ;
	}
	
	/**
	 * 验证实体对象，若验证失败抛ValidationException异常，错误代码，错误信息描述均可以在异常里面获取
	 * @param args 需要验证的参数数组
	 */
	public static void validationByException( Object ... args ){
		ResultData<Object>data = validation(args) ;
		if( data != null ){
			throw new ValidationException( data.getCode() , data.getMessage() ) ;
		}
	}
	
	
	/**
	 * 验证实体对象，结果已ResultData返回；ResultData==null 则通过验证，否则验证失败
	 * @param args 需要验证的参数数组
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public static <T>ResultData<T> validation(Object ... args) {
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
									try {
										return (ResultData<T>) handler.validation(args[i], fields[j], annotation[k]);
									} catch (IllegalArgumentException | IllegalAccessException e) {
										logger.error("参数验证失败：" ,e );
										return ResultData.newResultData(ErrorCode.VALIDATION_ERROR , "参数验证失败");
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	
	private static boolean only( String [] onlys, Field[] fields, int j ) {
		if( onlys != null && onlys.length >0 ){
			String fieldName = fields[j].getName();
			for ( int k = 0; k < onlys.length; k++ ) {
				if(StringUtils.equals( StringUtils.trim(fieldName) , StringUtils.trim(onlys[k] ) ) ){
					return Boolean.TRUE ;
				}
			}
		}
		return Boolean.FALSE ;
	}
	
	
}
