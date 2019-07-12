package com.zlw.commons.json.util;

import com.fasterxml.jackson.databind.JavaType;
import com.zlw.commons.json.JsonFactory;
import com.zlw.commons.json.api.Json;
import com.zlw.commons.json.exception.JsonCastException;

import java.util.List;
import java.util.Map;


public class JsonUtil {
    private static Json json;

    public static String toJson(Object value) throws JsonCastException {
        return getJson().toJson(value);
    }

    public static <T> T toObject(String json, Class<T> valueType) throws JsonCastException {
        return getJson().toObject(json, valueType);
    }

    public static <T> T toObject(String json, JavaType javaType) {
        return getJson().toObject(json, javaType);
    }

    public static <T> List<T> toObjectList(String json, Class<T> clazz) throws JsonCastException {
        return getJson().toObjectList(json, clazz);
    }

    public static <T>  Map<String, T> jsonToMap(String json) throws JsonCastException {
        return getJson().jsonToMap(json);
    }

    private static Json getJson() {
        if (json == null) {
            json = JsonFactory.getJson();
        }
        return json;
    }
}
