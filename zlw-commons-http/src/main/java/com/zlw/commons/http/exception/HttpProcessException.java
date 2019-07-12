package com.zlw.commons.http.exception;

/**
 * HttpClient请求过程中异常
 * Created by zhangliewei on 2017/5/26.
 */
public class HttpProcessException extends Exception {
    public HttpProcessException(Exception e){
        super(e);
    }

    /**
     * @param msg
     */
    public HttpProcessException(String msg) {
        super(msg);
    }

    /**
     * @param message
     * @param e
     */
    public HttpProcessException(String message, Exception e) {
        super(message, e);
    }

}
