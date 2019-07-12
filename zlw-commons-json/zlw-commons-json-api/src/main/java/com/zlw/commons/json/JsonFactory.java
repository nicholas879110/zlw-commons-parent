package com.zlw.commons.json;

import com.zlw.commons.json.api.IJsonFactory;
import com.zlw.commons.json.api.Json;
import com.zlw.commons.json.impl.StaticJsonBinder;


public final class JsonFactory {
    public static Json getJson() {
        IJsonFactory iJsonFactory = getIJsonFactory();
        return iJsonFactory.getJson();
    }

    public static IJsonFactory getIJsonFactory() {
        return StaticJsonBinder.getSingleton().getJsonfactory();
    }
}
