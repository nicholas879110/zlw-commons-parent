package com.zlw.commons.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/29
 */
public class ObjectUtil {

    /**
     * 复制对象obj，类似于值传递，非引用
     */
    public synchronized static Object cloneObject(Object obj) throws Exception{
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(obj);
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in =new ObjectInputStream(byteIn);
        return in.readObject();
    }

    /**
     * 返回一个对象的属性和属性值的JSON字符串
     * 返回格式({id:1},{name:cc},{pass:null})
     */
    public synchronized static String getProperty(Object entityName) {
        StringBuffer sb = new StringBuffer("");
        try {
            Class<?> c = entityName.getClass();
            Field field[] = c.getDeclaredFields();
            for (Field f : field) {
                Object v = invokeMethod(entityName, f.getName(), null);
                sb.append("{" + f.getName() + ":" + v + "},");
            }
            if(sb.length() > 0){
                sb.delete(sb.length() - 1, sb.length());
            }
        } catch (Exception e) {
            sb = new StringBuffer("");
        }
        return sb.toString();
    }


    /**
     * 获得对象属性的值
     */
    private synchronized static Object invokeMethod(Object owner, String methodName,
                                                    Object[] args) throws Exception {
        Class<?> ownerClass = owner.getClass();
        methodName = methodName.substring(0, 1).toUpperCase()
                + methodName.substring(1);
        Method method = null;
        try {
            method = ownerClass.getMethod("get" + methodName);
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
            return " can't find 'get" + methodName + "' method";
        }
        return method.invoke(owner);
    }

    /**
     * 返回一个对象的属性和属性值
     */
    public synchronized static LinkedHashMap<String,Object> getPropertyMap(Object entityName) {
        LinkedHashMap<String,Object> map = new LinkedHashMap<String, Object>();
        try {
            Class <?>c = entityName.getClass();
            // 获得对象属性
            Field field[] = c.getDeclaredFields();
            for (Field f : field) {
                Object v = invokeMethod(entityName, f.getName(), null);
                map.put(f.getName(), v);
            }
        } catch (Exception e) {
            map = null;
        }
        return map;
    }
}
