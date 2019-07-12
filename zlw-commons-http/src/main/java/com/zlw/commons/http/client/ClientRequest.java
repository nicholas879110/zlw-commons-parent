package com.zlw.commons.http.client;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;

import java.util.*;

/**
* HttpClinet请求相关参数
* <todo>
* 文件上传，流等
* </todo>
* Created by zhangliewei on 2017/5/26.
*/
public class ClientRequest {
    //请求地址
    private String url;
	//请求方法名称
	private HttpMethods method;
	//请求头信息
	private List<Header> headers=new ArrayList<>();
    //请求cookie
    private List<Cookie> cookies =new ArrayList<>();
	//http上下文
	private HttpClientContext context;
	//请求参数
	private List<NameValuePair> params=new ArrayList<>();
    //请求body,直接将请求内容放入body
    private String body;
	//请求编码
	private String inEncode;
	//输出编码
	private String outEncode;

	private ClientRequest(){};

	/**
	 * 获取实例
	 * @return
	 */
	public static ClientRequest build(){
		return new ClientRequest();
	}

    public String getUrl() {
        return url;
    }

    public ClientRequest setUrl(String url) {
        this.url = url;
        return this;
    }

	/**
	 * 设置请求方法
	 */
	public ClientRequest setMethod(HttpMethods method) {
		this.method = method;
		return this;
	}

	/**
	 * 获取请求方法
	 * @return
	 */
    public HttpMethods getMethod() {
        return method;
    }

    /**
	 * 设置Http上下文
	 */
	public ClientRequest setContext(HttpClientContext context) {
		this.context = context;
		return this;
	}

    /**
     * 获取http上下文
     * @return
     */
    public HttpClientContext getContext() {
        return context;
    }

    /**
     * 获取http请求头
     * @return
     */
    public List<Header> getHeaders() {
        return headers;
    }

    /**
     * 设置http请求头
     * @param headers
     * @return
     */
    public ClientRequest setHeaders(List<Header> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 清除http请求头
     */
    public ClientRequest clearHeaders() {
        this.headers.clear();
        return this;
    }

    /**
     * 添加http请求头
     * @param header
     * @return
     */
    public ClientRequest addHeader(Header header) {
        if(header != null) {
            this.headers.add(header);
        }
        return this;
    }

    /**
     * 移除http请求头
     * @param header
     */
    public ClientRequest removeHeader(Header header) {
        if(header != null) {
            this.headers.remove(header);
        }
        return this;
    }

    /**
     * 获取请求Cookie
     * @return
     */
    public List<Cookie> getCookies() {
        return cookies;
    }

    /**
     * 设置请求Cookie
     * @param cookies
     * @return
     */
    public ClientRequest setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
        return this;
    }


    /**
     * 添加cookie
     * @param cookie
     * @return
     */
    public ClientRequest addCookie(Cookie cookie){
        if (cookie!=null) {
            this.cookies.add(cookie);
        }
        return this;
    }

    /**
     * 移除Cookie
     * @param cookie
     */
    public ClientRequest removeCookie(Cookie cookie) {
        if(cookie != null) {
            this.cookies.remove(cookie);
        }
        return this;
    }

    /**
     * 清除所有cookie
     */
    public ClientRequest clear(){
        this.cookies.clear();
        return this;
    }

	/**
	 * 获取请求参数
	 * @return
	 */
	public List<NameValuePair> getParams() {
		return params;
	}


	/**
	 * 设置请求参数
	 * @param params
	 */
	public ClientRequest setParams(List<NameValuePair> params) {
		this.params = params;
		return this ;
	}

	/**
	 * 清除http请求参数
	 */
	public ClientRequest clearParams() {
		this.params.clear();
		return this;
	}

	/**
	 * 添加http请求参数
	 * @param param 请求参数
	 * @return
	 */
	public ClientRequest addParam(NameValuePair param) {
		if(param != null) {
			this.params.add(param);
		}
		return this;
	}

	/**
	 * 移除http请求参数
	 * @param param
	 */
	public ClientRequest removeParam(NameValuePair param) {
		if(params != null) {
			this.headers.remove(param);
		}
		return this;
	}

	/**
	 * 获取请求体
	 * @return
	 */
	public String getBody() {
		return body;
	}

	/**
	 * 设置请求体
	 * @param body
	 */
	public ClientRequest setBody(String body) {
		this.body = body;
		return this;
	}


	/**
	 * 获取请求编码
	 * @return
	 */
	public String getInEncode() {
		return inEncode;
	}

	/**
	 * 设置请求编码
	 * @param inEncode
	 */
	public ClientRequest setInEncode(String inEncode) {
		this.inEncode = inEncode;
		return this;
	}

	/**
	 * 获取输出编码
	 * @return
	 */
	public String getOutEncode() {
		return outEncode;
	}

	/**
	 * 设置输出编码
	 * @param outEncode
	 */
	public ClientRequest setOutEncode(String outEncode) {
		this.outEncode = outEncode;
		return this;
	}
}