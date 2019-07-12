package com.zlw.commons.paging;

import com.zlw.commons.core.ResultData;

import java.util.List;


public interface InnerPageQuery<T> {
     ResultData<List<T>> innerQuery();
}