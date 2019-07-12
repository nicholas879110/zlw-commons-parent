package com.zlw.commons.json.jackson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zlw.commons.json.api.Json;
import com.zlw.commons.json.exception.JsonCastException;


public class Jackson implements Json {


    private static final Jackson SINGLETON = new Jackson();
    /**
     * ObjectMapper
     */
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        //转成对象时，可以忽略多余参数
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static final Jackson getSingleton() {
        return SINGLETON;
    }

    @Override
    public String toJson(Object value) throws JsonCastException {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new JsonCastException("Json cast failed!", e);
        }
    }

    @Override
    public <T> T toObject(String json, JavaType javaType) throws JsonCastException {
        try {
            return mapper.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            throw new JsonCastException("Json cast failed!", e);
        } catch (IOException e) {
            throw new JsonCastException("Json cast failed!", e);
        }
    }

    @Override
    public <T> T toObject(String json, Class<T> valueType) throws JsonCastException {
        try {
            return mapper.readValue(json, valueType);
        } catch (Exception e) {
            throw new JsonCastException("Json cast failed!", e);
        }
    }


    /**
     * 将JSON字符串转换为对象
     *
     * @param json          JSON字符串
     * @param typeReference 对象类型
     * @return 对象
     */
    public <T> T toObject(String json, TypeReference<?> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new JsonCastException("Json cast failed!", e);
        }
    }

    /**
     * 将JSON字符串转换为List对象
     * @param json  JSON字符串
     * @param clazz 对象类型
     * @return 对象List
     */
    @Override
    public <T> List<T> toObjectList(String json, Class<T> clazz) {
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, clazz);
            List<T> list=mapper.readValue(json, javaType);
            return list;
        } catch (Exception e) {
            try {
                return mapper.readValue(json, new TypeReference<List<T>>() {});
            } catch (IOException e1) {
                throw new JsonCastException("Json cast failed!", e);
            }
        }
    }

    @Override
    public <T> List<T> jsonToList(String json, Class<T> valueType) {
        List<T> list = null;
        try {
            @SuppressWarnings("deprecation")
            JsonParser parser = mapper.getJsonFactory().createJsonParser(json);
            JsonNode nodes = parser.readValueAsTree();
            list = new ArrayList<T>(nodes.size());
            for (JsonNode node : nodes) {
                list.add(mapper.readValue(node.textValue(), valueType));
            }
        } catch (Exception e) {
            throw new JsonCastException("Json cast failed!", e);
        }
        return list;

    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Map<String, T> jsonToMap(String json) {
        Map<String, T> userData = null;
        try {
            userData = mapper.readValue(json, Map.class);
            return userData;
        } catch (Exception e) {
            throw new JsonCastException("Json cast failed!", e);
        }
    }


}
