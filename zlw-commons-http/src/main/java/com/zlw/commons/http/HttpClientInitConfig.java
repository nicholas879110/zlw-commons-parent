package com.zlw.commons.http;

import com.zlw.commons.http.ssl.SSLUtils;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * 初始化HttpClient连接相关配置
 * Created by zhangliewei on 2017/5/26.
 */
public class HttpClientInitConfig {

    private static Logger logger = LoggerFactory.getLogger(HttpClientInitConfig.class);

    // HttpClient连接管理器
    private static PoolingHttpClientConnectionManager pool;
    // HttpClient请求配置
    private static RequestConfig requestConfig;
    // HttpClient请求重试配置
    private static HttpRequestRetryHandler httpRequestRetryHandler;

    //连接池最大连接数
    private static final int poolMaxTotal = 200;
    //连接池最大路由个数
    private static final int poolMaxPerRoute = 200;
    //服务器返回响应超时时间
    private static final int socketTimeout = 3000;
    //连接超时时间
    private static final int connectTimeout = 3000;
    //请求超时时间
    private static final int connectionRequestTimeout = 3000;
    //请求重试次数
    private static final int retryTime = 3;

    static {
        try {
            SSLConnectionSocketFactory sslsf = SSLUtils.trustSSLConnectionSocketFactory();
            // 配置HttpClinet同时支持 HTTP 和 HTPPS
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslsf)
                    .build();
            pool = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            pool.setMaxTotal(poolMaxTotal);
            pool.setDefaultMaxPerRoute(poolMaxPerRoute);

            requestConfig = RequestConfig
                    .custom()
                    .setConnectionRequestTimeout(connectionRequestTimeout)
                    .setSocketTimeout(socketTimeout)
                    .setConnectTimeout(connectTimeout)
                    .build();
            httpRequestRetryHandler=createHttpRequestRetryHandler();
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException",e);
        } catch (KeyStoreException e) {
            logger.error("KeyStoreException", e);
        } catch (KeyManagementException e) {
            logger.error("KeyManagementException", e);
        }
    }


    private static HttpRequestRetryHandler createHttpRequestRetryHandler(){
        return new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= retryTime) {
                    // 超过#{retryTime}次则不再重试请求
                    return false;
                }
                if (exception instanceof InterruptedIOException) {
                    // Timeout
                    return false;
                }
                if (exception instanceof UnknownHostException) {
                    // Unknown host
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {
                    // Connection refused
                    return false;
                }
                if (exception instanceof SSLException) {
                    // SSL handshake exception
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if (idempotent) {
                    // Retry if the request is considered idempotent
                    return true;
                }
                return false;
            }
        };
    }

    public static PoolingHttpClientConnectionManager getPool() {
        return pool;
    }

    public static RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public static HttpRequestRetryHandler getHttpRequestRetryHandler() {
        return httpRequestRetryHandler;
    }
}
