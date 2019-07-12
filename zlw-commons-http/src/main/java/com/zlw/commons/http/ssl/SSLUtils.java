package com.zlw.commons.http.ssl;


import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 创建SSL请求相关配置
 * Created by zhangliewei on 2017/5/26.
 */
public class SSLUtils {


    /**
     * 创建信任服务器所有证书的SSLConnectionSocketFactory
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLConnectionSocketFactory trustSSLConnectionSocketFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustStrategy() {
            //信任服务器所有正式
            public boolean isTrusted(X509Certificate[] chain,
                                     String authType) throws CertificateException {
                return true;
            }
        });
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                builder.build());
        return sslConnectionSocketFactory;
    }
}
