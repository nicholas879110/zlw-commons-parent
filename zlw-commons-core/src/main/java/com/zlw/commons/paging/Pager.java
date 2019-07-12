package com.zlw.commons.paging;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Pager<T> implements Serializable {

    private static final long serialVersionUID = 8656597559014685635L;
    private long total;        //总记录数
    private List<T> list;    //结果集
    private int pageNum;    // 第几页
    private int pageSize;    // 每页记录数
    private int pages;        // 总页数
    private int size;        // 当前页的数量 <= pageSize，该属性来自ArrayList的size属性

    /**
     * 分页查询结果对象构造方法
     */
    public Pager() {
    }

    public Pager(long total, List<T> list, int pageNum, int pageSize, int pages) {
        this.total = total;
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = pages;
        this.size = list==null?0:list.size();
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}

