package com.zlw.commons.validation;

import java.lang.annotation.Annotation;

import com.zlw.commons.validation.annotation.Chinese;
import com.zlw.commons.validation.annotation.Email;
import com.zlw.commons.validation.annotation.IDCard;
import com.zlw.commons.validation.annotation.IP;
import com.zlw.commons.validation.annotation.Length;
import com.zlw.commons.validation.annotation.Letter;
import com.zlw.commons.validation.annotation.Max;
import com.zlw.commons.validation.annotation.Min;
import com.zlw.commons.validation.annotation.Mobile;
import com.zlw.commons.validation.annotation.NotEmpty;
import com.zlw.commons.validation.annotation.NotNull;
import com.zlw.commons.validation.annotation.Numeric;
import com.zlw.commons.validation.annotation.Range;
import com.zlw.commons.validation.annotation.RegMatcher;
import com.zlw.commons.validation.annotation.StringLength;
import com.zlw.commons.validation.annotation.Tel;
import com.zlw.commons.validation.annotation.Xss;
import com.zlw.commons.validation.handler.ChineseValidationHandler;
import com.zlw.commons.validation.handler.EmailValidationHandler;
import com.zlw.commons.validation.handler.IDCardValidationHandler;
import com.zlw.commons.validation.handler.IPValidationHandler;
import com.zlw.commons.validation.handler.LengthValidationHandler;
import com.zlw.commons.validation.handler.LetterValidationHandler;
import com.zlw.commons.validation.handler.MaxValidationHandler;
import com.zlw.commons.validation.handler.MinValidationHandler;
import com.zlw.commons.validation.handler.MobileValidationHandler;
import com.zlw.commons.validation.handler.NotEmptyValidationHandler;
import com.zlw.commons.validation.handler.NotNullValidationHandler;
import com.zlw.commons.validation.handler.NumericValidationHandler;
import com.zlw.commons.validation.handler.RangeValidationHandler;
import com.zlw.commons.validation.handler.RegMatcherValidationHandler;
import com.zlw.commons.validation.handler.StringLengthValidationHandler;
import com.zlw.commons.validation.handler.TelValidationHandler;
import com.zlw.commons.validation.handler.ValidationHandler;
import com.zlw.commons.validation.handler.XssValidationHandler;


public abstract class AbstractHandlerFactroy {

	
	public static ValidationHandler getInstance(Annotation[] annotation, int k) {
		if( annotation[k] instanceof NotNull){
			return NotNullValidationHandler.getInstance();
		}else if( annotation[k] instanceof NotEmpty ){
			return NotEmptyValidationHandler.getInstance();
		}else if( annotation[k] instanceof Email){
			return EmailValidationHandler.getInstance();
		}else if( annotation[k] instanceof Mobile){
			return MobileValidationHandler.getInstance();
		}else if( annotation[k] instanceof Tel){
			return TelValidationHandler.getInstance();
		}else if( annotation[k] instanceof Min){
			return MinValidationHandler.getInstance();
		}else if( annotation[k] instanceof Max ){
			return MaxValidationHandler.getInstance();
		}else if( annotation[k] instanceof Range ){
			return RangeValidationHandler.getInstance();
		}else if( annotation[k] instanceof Length ){
			return LengthValidationHandler.getInstance();
		}else if( annotation[k] instanceof Xss ){
			return XssValidationHandler.getInstance();
		}else if( annotation[k] instanceof Chinese ){
			return ChineseValidationHandler.getInstance();
		}else if( annotation[k] instanceof IDCard  ){
			return IDCardValidationHandler.getInstance();
		}else if( annotation[k] instanceof IP  ){
			return IPValidationHandler.getInstance();
		}else if( annotation[k] instanceof Letter  ){
			return LetterValidationHandler.getInstance();
		}else if( annotation[k] instanceof RegMatcher  ){
			return RegMatcherValidationHandler.getInstance();
		}else if( annotation[k] instanceof StringLength ){
			return StringLengthValidationHandler.getInstance();
		}else if( annotation[k] instanceof Numeric ){
			return NumericValidationHandler.getInstance();
		}
		return null;
	}
	
}
