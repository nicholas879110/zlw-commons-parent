package com.zlw.commons.json.api;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
import com.zlw.commons.json.exception.JsonCastException;


public interface Json {
    /**
     * 将对象转换为JSON字符串
     *
     * @param value 对象
     * @return JSOn字符串
     */
    String toJson(Object value) throws JsonCastException;

    /**
     * 将JSON字符串转换为对象
     *
     * @param json      JSON字符串
     * @param valueType 对象类型
     * @return 对象
     */
    <T> T toObject(String json, Class<T> valueType) throws JsonCastException;

    /**
     * 将JSON字符串转换为对象
     * @param json
     * @param javaType
     * @param <T>
     * @return
     */
    <T> T toObject(String json, JavaType javaType) throws JsonCastException;

    /**
     * 将JSON字符串转换为List对象
     *
     * @param json  JSON字符串
     * @param clazz 对象类型
     * @return 对象List
     */
    <T> List<T> toObjectList(String json, Class<T> clazz) throws JsonCastException;

    <T> List<T> jsonToList(String json, Class<T> valueType);
    
    <T> Map<String, T> jsonToMap(String json);
    
}
