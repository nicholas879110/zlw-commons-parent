package com.zlw.commons.json.impl;

import com.zlw.commons.json.JacksonFactory;
import com.zlw.commons.json.api.IJsonFactory;
import com.zlw.commons.json.api.JsonFactoryBinder;


public class StaticJsonBinder implements JsonFactoryBinder {
    private static final StaticJsonBinder SINGLETON = new StaticJsonBinder();
    private final IJsonFactory jsonFactory;

    private StaticJsonBinder() {
        jsonFactory = new JacksonFactory();
    }

    public static final StaticJsonBinder getSingleton() {
        return SINGLETON;
    }

    @Override
    public IJsonFactory getJsonfactory() {
        return jsonFactory;
    }

    @Override
    public String getJsonFactoryClassStr() {
        return "test str";
    }
}
