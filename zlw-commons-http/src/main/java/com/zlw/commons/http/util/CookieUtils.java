package com.zlw.commons.http.util;

import com.zlw.commons.http.client.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * cookie操作工具类
 * Created by zhangliewei on 2017/5/27.
 */
public class CookieUtils {

    public static Map<String,String> listToMap(List<Cookie> cookies){
        Map<String, String> map = new HashMap<>();
        if (cookies!=null&&cookies.size()>0) {
            for (Cookie cookie : cookies) {
                map.put(cookie.getName(),cookie.getValue());
            }
        }
        return map;
    }


    public static List<Cookie> mapToList(Map<String,String> cookies){
        List<Cookie> list=new ArrayList<>();
        if (cookies!=null&&cookies.size()>0) {
            for (Map.Entry<String,String> cookie : cookies.entrySet()) {
                list.add(new BasicClientCookie(cookie.getKey(), cookie.getValue()));
            }
        }
        return list;
    }

    public static List<ClientCookie> toClientCookie(List<Cookie> cookies){
        List<ClientCookie> list=new ArrayList<>();
        if (cookies!=null&&cookies.size()>0) {
            for (Cookie cookie : cookies) {
                list.add(new ClientCookie(cookie));
            }
        }
        return list;
    }


    public static List<Cookie> toCookie(List<ClientCookie> cookies){
        List<Cookie> list=new ArrayList<>();
        if (cookies!=null&&cookies.size()>0) {
            for (ClientCookie cookie : cookies) {
                BasicClientCookie tmp=new BasicClientCookie(cookie.getName(),cookie.getValue());
                tmp.setDomain(cookie.getDomain());
                tmp.setPath(cookie.getPath());
                tmp.setVersion(cookie.getVersion());
                tmp.setExpiryDate(cookie.getExpiryDate());
                tmp.setSecure(cookie.isSecure());
                list.add(tmp);
            }
        }
        return list;
    }
}
