package com.zlw.commons.serialization;


public class SerializationFactory {
    public static SerializationService getSerializationService(SerializationType type) {
        switch (type) {
            case JAVA_SERIALIZATION:
                return JavaSerializationService.getSerializationService();
            default:
        }
        return null;
    }
}
