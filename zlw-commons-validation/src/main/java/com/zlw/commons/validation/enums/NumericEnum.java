package com.zlw.commons.validation.enums;



/**
 * 数字类型
 * @author fukui
 */
public enum NumericEnum {

	/**
	 * 全数字
	 */
	NUMERIC , 
	
	/**
	 * 非负整数 （正整数  +  0）  
	 */
	POSITIVE ,
	
	/**
	 *非正整数（负整数  +  0） 
	 */
	NEGATIVE ,
	
	/**
	 * 整数
	 */
	INTEGER ,
	
	/**
	 * 非负浮点数（正浮点数   +   0）
	 */
	NONNEGATIVE_FLOAT ,
	
	/**
	 * 非正浮点数（负浮点数   +   0）
	 */
	NONPOSITIVE_FLOAT ,
	
	/**
	 * 浮点数
	 */
	FLOAT
	
}
