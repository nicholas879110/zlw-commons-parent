package com.zlw.commons.exception;

/**
 * @Description: 通用runtime异常，所有自定义的runtime异常需要继承此类
 */
public class GlobalRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -227961057827878851L;

    protected String code;

    public GlobalRuntimeException() {
    }

    public GlobalRuntimeException(String code) {
        super(code);
        this.setCode(code);
    }

    public GlobalRuntimeException(Throwable t) {
        super(t);
    }

    public GlobalRuntimeException(String code, Throwable t) {
        super(t);
        this.setCode(code);
    }

    public GlobalRuntimeException(String code, String msg) {
        super(msg);
        this.setCode(code);
    }

    public GlobalRuntimeException(String code, String msg, Throwable t) {
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
