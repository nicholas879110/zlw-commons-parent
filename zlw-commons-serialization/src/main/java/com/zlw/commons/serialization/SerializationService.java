package com.zlw.commons.serialization;


public interface SerializationService {
    /**
     * 序列化方法
     *
     * @param obj
     * @param <T>
     * @return
     */
    <T> byte[] serialize(T obj);


    /**
     * 反序列化方法
     * @param data
     * @return
     */
    Object deserialize(byte[] data);


    /**
     * 反序列化方法
     *
     * @param data
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] data, Class<T> clazz);
}
