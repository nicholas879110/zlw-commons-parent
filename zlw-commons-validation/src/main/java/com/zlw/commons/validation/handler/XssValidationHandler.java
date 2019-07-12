package com.zlw.commons.validation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlw.commons.core.ResultData;



/**
 * XSS过滤,作用于实体对象和方法参数
 * @author fukui
 */
public class XssValidationHandler implements ValidationHandler {

	private Logger logger = LoggerFactory.getLogger(getClass()) ;
	
	private static XssValidationHandler instance = new XssValidationHandler();
	
	
	private XssValidationHandler(){
		
	}
	
	
	public static XssValidationHandler getInstance(){
		return instance;
	}
	
	@Override
	public ResultData<Object> validation(Object  arg , Field  field , Annotation annotation ) throws IllegalArgumentException, IllegalAccessException {
		 Object fieldValue = field.get(arg);
		 String text = null ;
		 if( fieldValue != null ){
			 text = fieldValue.toString() ;
			 if( !StringUtils.isBlank( text ) ){
				 text = xssEncode( text ) ;
				 if( logger.isDebugEnabled() ){
					 logger.debug("Xss Encode Content : " , text ) ;
				 }
				 field.set(arg, text);
			 }
		 }
		 return null;
	}

	@Override
	public ResultData<Object> validation(Object value, Annotation annotation) {
		Object fieldValue = value;
		 String text = null ;
		 if( fieldValue != null ){
			 text = fieldValue.toString() ;
			 if( !StringUtils.isBlank( text ) ){
				 text = xssEncode( text ) ;
				 if( logger.isDebugEnabled() ){
					 logger.debug("Xss Encode Content : " , text ) ;
				 }
//				 field.set(arg, text);
				 value = text ;
			 }
		 }
		 return null;
	}  
	
	
	/**
	 * 将容易引起xss漏洞的半角字符直接替换成全角字符 
	 */
	private static String xssEncode(String s) {  
        if (s == null || "".equals(s)) {  
            return s;  
        }  
        StringBuilder sb = new StringBuilder(s.length() + 16);  
        for (int i = 0; i < s.length(); i++) {  
            char c = s.charAt(i);  
            switch (c) {  
            case '>':  
                sb.append('＞');//全角大于号  
                break;  
            case '<':  
                sb.append('＜');//全角小于号  
                break;  
            case '\'':  
                sb.append('‘');//全角单引号  
                break;  
            case '\"':  
                sb.append('“');//全角双引号  
                break;  
            case '&':  
                sb.append('＆');//全角  
                break;  
            case '\\':  
                sb.append('＼');//全角斜线  
                break;  
            case '#':  
                sb.append('＃');//全角井号  
                break;  
            default:  
                sb.append(c);  
                break;  
            }  
        }  
        return sb.toString();  
    }


	
}
