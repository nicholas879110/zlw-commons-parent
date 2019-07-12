package com.zlw.commons.json.impl;

import com.zlw.commons.json.api.IJsonFactory;
import com.zlw.commons.json.api.JsonFactoryBinder;


public class StaticJsonBinder implements JsonFactoryBinder {
    private static final StaticJsonBinder SINGLETON = new StaticJsonBinder();

    public static final StaticJsonBinder getSingleton() {
        return SINGLETON;
    }

    @Override
    public IJsonFactory getJsonfactory() {
        throw new NullPointerException("This impl should never be used!");
    }

    @Override
    public String getJsonFactoryClassStr() {
        throw new NullPointerException("This impl should never be used!");
    }
}
