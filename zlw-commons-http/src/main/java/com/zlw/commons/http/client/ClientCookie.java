package com.zlw.commons.http.client;

import org.apache.http.cookie.Cookie;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhangliewei on 2017/6/1.
 */
public class ClientCookie implements  Serializable {

    private String name;
    private String value;
    private String comment;
    private String commentURL;
    private Date expiryDate;
    private boolean persistent;
    private String domain;
    private String path;
    private int  ports[];
    private boolean secure;
    private int version;
    private boolean expired;

    public ClientCookie() {
    }

    public ClientCookie(Cookie cookie){
        this.name=cookie.getName();
        this.value=cookie.getValue();
        this.comment=cookie.getComment();
        this.commentURL=cookie.getCommentURL();
        this.expiryDate=cookie.getExpiryDate();
        this.persistent=cookie.isPersistent();
        this.domain=cookie.getDomain();
        this.path=cookie.getPath();
        this.ports=cookie.getPorts();
        this.secure=cookie.isSecure();
        this.version=cookie.getVersion();
        if (this.expiryDate!=null) {
            this.expired = cookie.isExpired(this.expiryDate);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCommentURL(String commentURL) {
        this.commentURL = commentURL;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPorts(int[] ports) {
        this.ports = ports;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getComment() {
        return comment;
    }

    public String getCommentURL() {
        return commentURL;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public String getDomain() {
        return domain;
    }

    public String getPath() {
        return path;
    }

    public int[] getPorts() {
        return ports;
    }

    public boolean isSecure() {
        return secure;
    }

    public int getVersion() {
        return version;
    }
}
