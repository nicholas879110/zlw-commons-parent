package com.zlw.commons.serialization;


public class SerializationException extends RuntimeException {
    public SerializationException() {
    }

    public SerializationException(Throwable t) {
        super(t);
    }

    public SerializationException(String msg) {
        super(msg);
    }

    public SerializationException(String msg, Throwable t) {
        super(msg, t);
    }
}
