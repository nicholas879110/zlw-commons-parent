package com.zlw.commons.serialization;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class JavaSerializationService implements SerializationService {

    private volatile static SerializationService service;

    public static SerializationService getSerializationService() {
        if (service != null) {
            return service;
        }
        synchronized (JavaSerializationService.class) {
            if (service == null) {
                service = new JavaSerializationService();
            }
        }
        return service;
    }

    @Override
    public <T> byte[] serialize(T obj) {
        byte[] bytes;
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(bos));
        ) {
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("序列化对象错误", e);
        }
        return bytes;
    }

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        T t = null;
        try (
                BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(data));
                ObjectInputStream ois = new ObjectInputStream(bis)
        ) {
            t = (T) ois.readObject();
        } catch (IOException e) {
            throw new SerializationException("Error by deserialize {" + t.getClass() + "}", e);
        } catch (ClassNotFoundException e) {
            throw new SerializationException("Class not found {" + t.getClass() + "}", e);
        }
        return t;
    }

    @Override
    public Object deserialize(byte[] data) {
        Object t = null;
        try (
                BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(data));
                ObjectInputStream ois = new ObjectInputStream(bis)
        ) {
            t = ois.readObject();
        } catch (IOException e) {
            throw new SerializationException("Error by deserialize {" + t.getClass() + "}", e);
        } catch (ClassNotFoundException e) {
            throw new SerializationException("Class not found {" + t.getClass() + "}", e);
        }
        return t;
    }
}
