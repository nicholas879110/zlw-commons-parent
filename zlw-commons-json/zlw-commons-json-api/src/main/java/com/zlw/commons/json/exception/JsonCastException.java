package com.zlw.commons.json.exception;


public class JsonCastException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JsonCastException() {
    }

    public JsonCastException(Throwable t) {
        super(t);
    }

    public JsonCastException(String msg) {
        super(msg);
    }

    public JsonCastException(String msg, Throwable t) {
        super(msg, t);
    }
}
