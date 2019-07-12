package com.zlw.commons.paging;

import java.io.Serializable;


public class PagerQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    //当前页(默认从１开始)
    private int pageNum=1;
    //每页的数量
    private int pageSize=10;

    public PagerQuery() {
    }

    public PagerQuery(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
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

    public int getStartRow() {
        return (pageNum-1)*pageSize;
    }


    public int getEndRow() {
        return (pageNum-1)*pageSize+pageSize;
    }

}

