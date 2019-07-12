package com.zlw.commons.serialization;

import org.nustaq.serialization.FSTConfiguration;


public class FSTSerializationService implements SerializationService {
    private static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

    @Override
    public <T> byte[] serialize(T obj) {
        return conf.asByteArray(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return (T) conf.asObject(data);
    }

    @Override
    public Object deserialize(byte[] data) {
        return conf.asObject(data);
    }
}
