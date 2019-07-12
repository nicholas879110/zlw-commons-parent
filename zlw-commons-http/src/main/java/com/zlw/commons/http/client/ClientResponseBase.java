package com.zlw.commons.http.client;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by zhangliewei on 2017/5/27.
 */
public interface ClientResponseBase {
    /**
     * 获取响应状态
     * @return
     */
    public StatusLine getStatusline();

    /**
     * 获取请求相关参数
     * @return
     */
    public ClientRequest getRequest();

    /**
     * 获取请求上下文
     * @return
     */
    public HttpClientContext getHttpContext();

    /**
     * 获取请求响应内容
     * @return
     */
    public HttpEntity getHttpEntity();

    /**
     * 获取请求响应头
     * @return
     */
    public List<Header> getHeaders();


    /**
     * 获取请求响应Cookie
     * @return
     */
    public List<Cookie> getCookies();

    /**
     * 返回内容转换为字符串
     *
     * @return
     */
    public String getAsString() throws IOException;


    /**
     * 返回内容转换为字符串
     *
     * @param encoding 字符编码
     * @return
     */
    public String getAsString(String encoding) throws IOException;


    /**
     * 返回内容转换为流
     *
     * @return
     */
    public void getAsStream(OutputStream out) throws IOException;


}
