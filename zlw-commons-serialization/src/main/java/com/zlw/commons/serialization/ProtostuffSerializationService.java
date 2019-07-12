package com.zlw.commons.serialization;


public class ProtostuffSerializationService implements SerializationService{
    @Override
    public <T> byte[] serialize(T obj) {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return null;
    }

    @Override
    public Object deserialize(byte[] data) {
        return null;
    }
}
