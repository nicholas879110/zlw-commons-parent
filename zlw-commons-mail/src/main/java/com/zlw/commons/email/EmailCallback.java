package com.zlw.commons.email;


public interface EmailCallback {

    /**
     * 当发送email成功是回调函数
     */
    void whenSuccess();

    /**
     * 当发送email失败时回调函数
     */
    void whenFailed();
}
