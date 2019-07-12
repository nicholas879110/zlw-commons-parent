package com.zlw.commons.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;


public class KryoSerializationService implements SerializationService {
    private ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<>();
    private Kryo kryo = new Kryo();

    public <T> void register(Class<T> clazz) {
        kryo.register(clazz, new FieldSerializer(kryo, clazz));
    }

    @Override
    public <T> byte[] serialize(T obj) {
        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); Output output = new Output(bos)) {
            getKryo().writeObject(output, obj);
            output.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Error by serialize {" + obj.getClass() + "}", e);
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return getKryo().readObject(new Input(new ByteArrayInputStream(data)), clazz);
    }

    @Override
    public Object deserialize(byte[] data) {
        return getKryo().readClassAndObject(new Input(new ByteArrayInputStream(data)));
    }

    private Kryo getKryo() {
        Kryo kryo = kryoThreadLocal.get();
        if (kryo == null) {
            kryo = createKryo();
            kryoThreadLocal.set(kryo);
        }
        return kryo;
    }

    protected Kryo createKryo() {
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        kryo.register(BigDecimal.class, new DefaultSerializers.BigDecimalSerializer());
        kryo.register(BigInteger.class, new DefaultSerializers.BigIntegerSerializer());
        kryo.register(HashMap.class);
        kryo.register(ArrayList.class);
        kryo.register(LinkedList.class);
        kryo.register(HashSet.class);
        kryo.register(TreeSet.class);
        kryo.register(Hashtable.class);
        kryo.register(Date.class);
        kryo.register(Calendar.class);
        kryo.register(ConcurrentHashMap.class);
        kryo.register(SimpleDateFormat.class);
        kryo.register(GregorianCalendar.class);
        kryo.register(Vector.class);
        kryo.register(BitSet.class);
        kryo.register(StringBuffer.class);
        kryo.register(StringBuilder.class);
        kryo.register(Object.class);
        kryo.register(Object[].class);
        kryo.register(String[].class);
        kryo.register(byte[].class);
        kryo.register(char[].class);
        kryo.register(int[].class);
        kryo.register(float[].class);
        kryo.register(double[].class);
        return kryo;
    }
}
