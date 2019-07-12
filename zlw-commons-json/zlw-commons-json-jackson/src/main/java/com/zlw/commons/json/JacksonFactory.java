package com.zlw.commons.json;

import com.zlw.commons.json.api.IJsonFactory;
import com.zlw.commons.json.api.Json;
import com.zlw.commons.json.jackson.Jackson;


public class JacksonFactory implements IJsonFactory {
    @Override
    public Json getJson() {
        return Jackson.getSingleton();
    }
}
