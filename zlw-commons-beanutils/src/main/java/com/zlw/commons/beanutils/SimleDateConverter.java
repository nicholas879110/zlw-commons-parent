package com.zlw.commons.beanutils;

import org.apache.commons.beanutils.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimleDateConverter implements Converter {
    @Override
    public Object convert(Class class1, Object obj) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format((Date) obj);
    }
}
