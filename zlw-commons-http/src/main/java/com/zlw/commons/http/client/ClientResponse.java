package com.zlw.commons.http.client;

import org.apache.http.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangliewei on 2017/5/27.
 */
public class ClientResponse implements ClientResponseBase{
    //http响应状态
    private StatusLine statusline;
    //http请求参数
    private ClientRequest request;
    //http请求上下文
    private HttpClientContext httpContext;
    //http返回正文
    private HttpEntity httpEntity;
    //http请求返回头
    private List<Header> headers;
    //http请求返回Cookie
    private List<Cookie> cookies;
    private HttpResponse httpResponse;
    public ClientResponse(StatusLine statusline, ClientRequest request, HttpClientContext httpContext, HttpEntity httpEntity, List<Header> headers) {
        this.statusline = statusline;
        this.request = request;
        this.httpContext = httpContext;
        this.httpEntity = httpEntity;
        this.headers = headers;
    }

    public ClientResponse(ClientRequest httpRequest, HttpResponse httpResponse) {
        this.request=httpRequest;
        this.httpResponse=httpResponse;
        this.httpEntity=httpResponse.getEntity();
        this.statusline=httpResponse.getStatusLine();
        this.httpContext=httpRequest.getContext();
        this.headers= Arrays.asList(httpResponse.getAllHeaders());
        this.cookies=httpRequest.getContext().getCookieStore().getCookies();

    }

    @Override
    public StatusLine getStatusline() {
        return statusline;
    }
    @Override
    public ClientRequest getRequest() {
        return request;
    }
    @Override
    public HttpClientContext getHttpContext() {
        return httpContext;
    }
    @Override
    public HttpEntity getHttpEntity() {
        return httpEntity;
    }
    @Override
    public List<Header> getHeaders() {
        return headers;
    }

    @Override
    public List<Cookie> getCookies() {
        return cookies;
    }

    @Override
    public String getAsString(String encoding) throws IOException {
        return EntityUtils.toString(this.httpEntity, encoding);
    }

    @Override
    public String getAsString() throws IOException {
        return EntityUtils.toString(this.httpEntity, Charset.defaultCharset().displayName());
    }

    @Override
    public void getAsStream(OutputStream out) throws IOException {
        this.httpEntity.writeTo(out);
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }
}
