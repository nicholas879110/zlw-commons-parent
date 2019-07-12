package com.zlw.commons.validation;


/**
 * 验证异常
 */
public class ValidationException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	
	private String code;


	public ValidationException(){}
	
	
	public ValidationException( Throwable t ){
		super( t );
	}
	
	public ValidationException( String message ){
		super( message );
	}
	
    public ValidationException( String message, Throwable t) {
        super(message, t);
    }
	
    public ValidationException(  String code , String message) {
        super(message);
        this.code=code;
    }
    
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	
	
	
}
