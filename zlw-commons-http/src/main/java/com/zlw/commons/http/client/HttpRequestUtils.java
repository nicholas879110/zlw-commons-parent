package com.zlw.commons.http.client;

import org.apache.http.client.methods.*;

/**
 * 根据请求参数获取相应的请求方式
 * Created by zhangliewei on 2017/5/26.
 */
public class HttpRequestUtils {

    /**
     * 根据请求方法名，获取request对象
     * @param url    资源地址
     * @param method 请求方式
     * @return
     */
    public static HttpRequestBase getRequest(String url, HttpMethods method) {
        HttpRequestBase request = null;
        switch (method.getCode()) {
            case 0:// HttpGet
                request = new HttpGet(url);
                break;
            case 1:// HttpPost
                request = new HttpPost(url);
                break;
            case 2:// HttpHead
                request = new HttpHead(url);
                break;
            case 3:// HttpPut
                request = new HttpPut(url);
                break;
            case 4:// HttpDelete
                request = new HttpDelete(url);
                break;
            case 5:// HttpTrace
                request = new HttpTrace(url);
                break;
            case 6:// HttpPatch
                request = new HttpPatch(url);
                break;
            case 7:// HttpOptions
                request = new HttpOptions(url);
                break;
            default:
                request = new HttpGet(url);
                break;
        }
        return request;
    }
}
