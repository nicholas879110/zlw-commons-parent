package com.zlw.commons.http;

import com.zlw.commons.http.client.ClientResponse;
import com.zlw.commons.http.client.HttpMethods;
import com.zlw.commons.http.client.ClientRequest;
import com.zlw.commons.http.client.HttpRequestUtils;
import com.zlw.commons.http.exception.HttpProcessException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Httclient操作工具类，使用该工具类可以模拟发送（http/https）的请求
 * <p>
 * HttpClient是线程安全的，请求完成后必须releaseConnection()方法来释放连接，否则会造成线程阻塞
 * </p>
 * <p>
 * 虽然HttpClient实例是线程安全的和可执行的多个线程之间共享，但是HttpContext实例则不是这样
 * 强烈建议每个线程维护自己专用的HttpContext实例。
 * </p>
 * Created by zhangliewei on 2017/5/26.
 */
public class HttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    // 响应报文解码字符集
    private final static String UTF8_CHARSET = "utf-8";

    /**
     * 获取HttpClient实例
     *
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        try {
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(HttpClientInitConfig.getPool())
                    .setDefaultRequestConfig(HttpClientInitConfig.getRequestConfig())
                    .setRetryHandler(HttpClientInitConfig.getHttpRequestRetryHandler())
                    .build();
            return httpClient;
        } catch (Exception e) {
            logger.error("创建HttpClient实例失败", e);
        }
        return HttpClients.createDefault();
    }

    /**
     * 以Get方式，请求资源或服务
     *
     * @param url 请求地址
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse get(String url) throws HttpProcessException {
        return get(ClientRequest.build().setUrl(url));
    }

    /**
     * 以Get方式，请求资源或服务
     *
     * @param url      请求地址
     * @param encoding 输出编码
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse get(String url, String encoding) throws HttpProcessException {
        return get(ClientRequest.build().setUrl(url).setOutEncode(encoding));
    }

    /**
     * 以Get方式，请求资源或服务
     *
     * @param url     资源地址
     * @param headers 请求头信息
     * @param cookies http请求Cookie
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse get(String url, List<Header> headers, List<Cookie> cookies) throws HttpProcessException {
        return get(ClientRequest.build().setUrl(url).setHeaders(headers).setCookies(cookies));
    }

    /**
     * 以Get方式，请求资源或服务
     *
     * @param url     资源地址
     * @param headers 请求头信息
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse get(String url, List<Header> headers) throws HttpProcessException {
        return get(ClientRequest.build().setUrl(url).setHeaders(headers));
    }

    /**
     * 以Get方式，请求资源或服务
     *
     * @param url      资源地址
     * @param headers  请求头信息
     * @param cookies  http请求Cookie
     * @param encoding 编码
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse get(String url, List<Header> headers, List<Cookie> cookies, String encoding) throws HttpProcessException {
        return get(ClientRequest.build().setUrl(url).setHeaders(headers).setCookies(cookies).setOutEncode(encoding));
    }

    /**
     * 以Get方式，请求资源或服务
     *
     * @param url     资源地址
     * @param headers 请求头信息
     * @param context http上下文
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse get(String url, List<Header> headers, HttpClientContext context) throws HttpProcessException {
        return get(ClientRequest.build().setUrl(url).setHeaders(headers).setContext(context));
    }

    /**
     * 以Get方式，请求资源或服务
     *
     * @param url      资源地址
     * @param headers  请求头信息
     * @param context  http上下文
     * @param encoding 编码
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse get(String url, List<Header> headers, HttpClientContext context, String encoding) throws HttpProcessException {
        return get(ClientRequest.build().setUrl(url).setHeaders(headers).setContext(context).setInEncode(encoding));
    }

    /**
     * 以Get方式，请求资源或服务
     *
     * @param httpRequest 请求参数配置
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse get(ClientRequest httpRequest) throws HttpProcessException {
        return execute(httpRequest.setMethod(HttpMethods.GET));
    }


    /**
     * 以POST方式，请求资源或服务
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, List<NameValuePair> params) throws HttpProcessException {
        return post(ClientRequest.build().setUrl(url).setParams(params));
    }

    /**
     * 以POST方式，请求资源或服务
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, Map<String, String> params) throws HttpProcessException {
        List<NameValuePair> nameValuePairs = null;
        if (params != null) {
            nameValuePairs = new ArrayList<>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                nameValuePairs.add(nameValuePair);
            }
        }
        return post(ClientRequest.build().setUrl(url).setParams(nameValuePairs));
    }

    /**
     * 以POST方式，请求资源或服务
     *
     * @param url    请求地址
     * @param params 请求body
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, String params) throws HttpProcessException {
        return post(ClientRequest.build().setUrl(url).setBody(params));
    }

    /**
     * 以POST方式，请求资源或服务
     *
     * @param url         请求地址
     * @param params      请求参数
     * @param inEncoding  输入编码
     * @param outEncoding 输出编码
     * @return
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, List<NameValuePair> params, String inEncoding, String outEncoding) throws HttpProcessException {
        return post(ClientRequest.build().setUrl(url).setParams(params).setInEncode(inEncoding).setOutEncode(outEncoding));
    }

    /**
     * 以POST方式，请求资源或服务
     *
     * @param url         请求地址
     * @param params      请求参数
     * @param inEncoding  输入编码
     * @param outEncoding 输出编码
     * @return
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, Map<String, String> params, String inEncoding, String outEncoding) throws HttpProcessException {
        List<NameValuePair> nameValuePairs = null;
        if (params != null) {
            nameValuePairs = new ArrayList<>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                nameValuePairs.add(nameValuePair);
            }
        }
        return post(ClientRequest.build().setUrl(url).setParams(nameValuePairs).setInEncode(inEncoding).setOutEncode(outEncoding));
    }

    /**
     * 以POST方式，请求资源或服务
     *
     * @param url         请求地址
     * @param params      请求参数
     * @param inEncoding  输入编码
     * @param outEncoding 输出编码
     * @return
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, String params, String inEncoding, String outEncoding) throws HttpProcessException {
        return post(ClientRequest.build().setUrl(url).setBody(params).setInEncode(inEncoding).setOutEncode(outEncoding));
    }


    /**
     * 以POST方式，请求资源或服务
     *
     * @param url     资源地址
     * @param params  请求参数
     * @param headers 请求头信息
     * @param cookies http请求Cookie
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, List<NameValuePair> params, List<Header> headers, List<Cookie> cookies) throws HttpProcessException {
        return post(ClientRequest.build().setUrl(url).setParams(params).setHeaders(headers).setCookies(cookies));
    }


    /**
     * 以POST方式，请求资源或服务
     *
     * @param url     资源地址
     * @param params  请求参数
     * @param headers 请求头信息
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, List<NameValuePair> params, List<Header> headers) throws HttpProcessException {
        return post(ClientRequest.build().setUrl(url).setParams(params).setHeaders(headers));
    }

    /**
     * 以POST方式，请求资源或服务
     *
     * @param url         资源地址
     * @param params      请求参数
     * @param headers     请求头信息
     * @param cookies     http请求Cookie
     * @param inEncoding  输入编码
     * @param outEncoding 编码
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, List<NameValuePair> params, List<Header> headers, List<Cookie> cookies, String inEncoding, String outEncoding) throws HttpProcessException {
        return post(ClientRequest.build().setUrl(url).setParams(params).setHeaders(headers).setCookies(cookies).setInEncode(inEncoding).setOutEncode(outEncoding));
    }


    /**
     * 以POST方式，请求资源或服务
     *
     * @param url     资源地址
     * @param params  请求参数
     * @param headers 请求头信息
     * @param context http上下文
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, List<NameValuePair> params, List<Header> headers, HttpClientContext context) throws HttpProcessException {
        return post(ClientRequest.build().setUrl(url).setParams(params).setHeaders(headers).setContext(context));
    }

    /**
     * 以POST方式，请求资源或服务
     *
     * @param url     资源地址
     * @param params  请求参数
     * @param headers 请求头信息
     * @param cookies http请求Cookie
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, Map<String, String> params, List<Header> headers, List<Cookie> cookies) throws HttpProcessException {
        List<NameValuePair> nameValuePairs = null;
        if (params != null) {
            nameValuePairs = new ArrayList<>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                nameValuePairs.add(nameValuePair);
            }
        }
        return post(ClientRequest.build().setUrl(url).setParams(nameValuePairs).setHeaders(headers).setCookies(cookies));
    }



    /**
     * 以POST方式，请求资源或服务
     *
     * @param url     资源地址
     * @param params  请求参数
     * @param headers 请求头信息
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, Map<String, String> params, List<Header> headers) throws HttpProcessException {
        List<NameValuePair> nameValuePairs = null;
        if (params != null) {
            nameValuePairs = new ArrayList<>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                nameValuePairs.add(nameValuePair);
            }
        }
        return post(ClientRequest.build().setUrl(url).setParams(nameValuePairs).setHeaders(headers));
    }

    /**
     * 以POST方式，请求资源或服务
     *
     * @param url         资源地址
     * @param params      请求参数
     * @param headers     请求头信息
     * @param cookies     http请求Cookie
     * @param inEncoding  输入编码
     * @param outEncoding 编码
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, Map<String, String> params, List<Header> headers, List<Cookie> cookies, String inEncoding, String outEncoding) throws HttpProcessException {
        List<NameValuePair> nameValuePairs = null;
        if (params != null) {
            nameValuePairs = new ArrayList<>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                nameValuePairs.add(nameValuePair);
            }
        }
        return post(ClientRequest.build().setUrl(url).setParams(nameValuePairs).setHeaders(headers).setCookies(cookies).setInEncode(inEncoding).setOutEncode(outEncoding));
    }


    /**
     * 以POST方式，请求资源或服务
     *
     * @param url     资源地址
     * @param params  请求参数
     * @param headers 请求头信息
     * @param context http上下文
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, Map<String, String> params, List<Header> headers, HttpClientContext context) throws HttpProcessException {
        List<NameValuePair> nameValuePairs = null;
        if (params != null) {
            nameValuePairs = new ArrayList<>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                nameValuePairs.add(nameValuePair);
            }
        }
        return post(ClientRequest.build().setUrl(url).setParams(nameValuePairs).setHeaders(headers).setContext(context));
    }

    /**
     * 以POST方式，请求资源或服务
     *
     * @param url     资源地址
     * @param params  请求参数
     * @param headers 请求头信息
     * @param context http上下文
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, Map<String, String> params, List<Header> headers, HttpClientContext context, String inEncoding, String outEncoding) throws HttpProcessException {
        List<NameValuePair> nameValuePairs = null;
        if (params != null) {
            nameValuePairs = new ArrayList<>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                nameValuePairs.add(nameValuePair);
            }
        }
        return post(ClientRequest.build().setUrl(url).setParams(nameValuePairs).setHeaders(headers).setContext(context).setInEncode(inEncoding).setOutEncode(outEncoding));
    }

    /**
     * 以POST方式，请求资源或服务
     *
     * @param url     资源地址
     * @param params  请求参数
     * @param headers 请求头信息
     * @param cookies http请求Cookie
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, String params, List<Header> headers, List<Cookie> cookies) throws HttpProcessException {
        return post(ClientRequest.build().setUrl(url).setBody(params).setHeaders(headers).setCookies(cookies));
    }


    /**
     * 以POST方式，请求资源或服务
     *
     * @param url     资源地址
     * @param params  请求参数
     * @param headers 请求头信息
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, String params, List<Header> headers) throws HttpProcessException {
        return post(ClientRequest.build().setUrl(url).setBody(params).setHeaders(headers));
    }

    /**
     * 以POST方式，请求资源或服务
     *
     * @param url         资源地址
     * @param params      请求参数
     * @param headers     请求头信息
     * @param cookies     http请求Cookie
     * @param inEncoding  输入编码
     * @param outEncoding 编码
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, String params, List<Header> headers, List<Cookie> cookies, String inEncoding, String outEncoding) throws HttpProcessException {
        return post(ClientRequest.build().setUrl(url).setBody(params).setHeaders(headers).setCookies(cookies).setInEncode(inEncoding).setOutEncode(outEncoding));
    }


    /**
     * 以POST方式，请求资源或服务
     *
     * @param url     资源地址
     * @param params  请求参数
     * @param headers 请求头信息
     * @param context http上下文
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, String params, List<Header> headers, HttpClientContext context) throws HttpProcessException {
        return post(ClientRequest.build().setUrl(url).setBody(params).setHeaders(headers).setContext(context));
    }


    /**
     * 以POST方式，请求资源或服务
     *
     * @param url         资源地址
     * @param params      请求参数
     * @param headers     请求头信息
     * @param context     http上下文
     * @param inEncoding  输入编码
     * @param outEncoding 输出编码
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(String url, String params, List<Header> headers, HttpClientContext context, String inEncoding, String outEncoding) throws HttpProcessException {
        return post(ClientRequest.build().setUrl(url).setBody(params).setHeaders(headers).setContext(context).setInEncode(inEncoding).setOutEncode(outEncoding));
    }


    /**
     * 以POST方式，请求资源或服务
     *
     * @param httpRequest 请求参数配置
     * @return 返回处理结果
     * @throws HttpProcessException
     */
    public static ClientResponse post(ClientRequest httpRequest) throws HttpProcessException {
        return execute(httpRequest.setMethod(HttpMethods.POST));
    }

    /**
     * 请求资源或服务
     *
     * @param httpRequest 请求相关参数 {@link ClientRequest}
     * @return
     * @throws HttpProcessException
     */
    private static ClientResponse execute(ClientRequest httpRequest) throws HttpProcessException {
        HttpClient client = null;
        HttpResponse resp = null;
        try {
            client = getHttpClient();
            HttpRequestBase request = HttpRequestUtils.getRequest(httpRequest.getUrl(), httpRequest.getMethod());
            if (httpRequest.getHeaders() != null) {
                request.setHeaders(httpRequest.getHeaders().toArray(new Header[httpRequest.getHeaders().size()]));
            }
            if (httpRequest.getContext() == null) {
                HttpClientContext httpClientContext = HttpClientContext.create();
                httpRequest.setContext(httpClientContext);
            }
            if (httpRequest.getContext()!=null){
                CookieStore cookieStore = httpRequest.getContext().getCookieStore();
                if (cookieStore==null){
                    cookieStore=new BasicCookieStore();
                    httpRequest.getContext().setCookieStore(cookieStore);
                }
            }
            if (httpRequest.getContext() != null && httpRequest.getCookies() != null) {
                for (Cookie cookie : httpRequest.getCookies()) {
                    httpRequest.getContext().getCookieStore().addCookie(cookie);
                }
            }
            //判断是否支持设置entity(仅HttpPost、HttpPut、HttpPatch支持)
            if (HttpEntityEnclosingRequestBase.class.isAssignableFrom(request.getClass())) {
                HttpEntity httpEntity = null;
                if (httpRequest.getParams() != null && httpRequest.getParams().size() > 0) {
                    httpEntity = new UrlEncodedFormEntity(httpRequest.getParams(), StringUtils.isNotBlank(httpRequest.getInEncode()) ? httpRequest.getInEncode() : Charset.defaultCharset().displayName());
                }
                if (StringUtils.isNotBlank(httpRequest.getBody())) {
                    httpEntity = new StringEntity(httpRequest.getBody(), StringUtils.isNotBlank(httpRequest.getInEncode()) ? httpRequest.getInEncode() : Charset.defaultCharset().displayName());
                }
                if (httpEntity != null) {
                    ((HttpEntityEnclosingRequestBase) request).setEntity(httpEntity);
                }
            }
            resp = client.execute(request, httpRequest.getContext());
            ClientResponse clientResponse = new ClientResponse(httpRequest, resp);
            return clientResponse;
        } catch (Exception e) {
            throw new HttpProcessException(e);
        }
    }


    /**
     * consume response
     *
     * @param httpResponse HttpResponse对象
     */
    public static void consume(ClientResponse httpResponse) {
        try {
            if (httpResponse == null||httpResponse.getHttpResponse()==null) return;
            EntityUtils.consume(httpResponse.getHttpResponse().getEntity());
        } catch (IOException e) {
            logger.error("consume fail!", e);
        }
    }


    /**
     * 关闭response
     *
     * @param httpResponse HttpResponse对象
     */
    public static void close(ClientResponse httpResponse) {
        try {
            if (httpResponse == null||httpResponse.getHttpResponse()==null) return;
            if (CloseableHttpResponse.class.isAssignableFrom(httpResponse.getHttpResponse().getClass())) {
                ((CloseableHttpResponse) httpResponse.getHttpResponse()).close();
            }
        } catch (IOException e) {
            logger.error("关闭连接失败!", e);
        }
    }

}
