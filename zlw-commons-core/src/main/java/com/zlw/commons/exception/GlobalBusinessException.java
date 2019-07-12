package com.zlw.commons.exception;

/**
* @Description: 通用checked异常，所有自定义的checked异常需要继承此类
* @version V1.0
*/
public class GlobalBusinessException extends Exception {
	
	
    private static final long serialVersionUID = 1L;

    protected String code;

    public GlobalBusinessException() {
    }

    public GlobalBusinessException(String code) {
        super(code);
        this.setCode(code);
    }

    public GlobalBusinessException(Throwable t) {
        super(t);
    }

    public GlobalBusinessException(String code, Throwable t) {
        super(t);
        this.setCode(code);
    }

    public GlobalBusinessException(String code, String msg) {
        super(msg);
        this.setCode(code);
    }

    public GlobalBusinessException(String code, String msg, Throwable t) {
        super(msg, t);
        this.setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
