package com.zlw.commons.utils;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.*;
import java.text.ParseException;
import java.util.*;

/**
 * FileUtil说明.
 * @author Hyberbin
 * @date 2013-6-26 14:09:54
 */
public class Reflections {

    /** CGLIB */
    public static final String CGLIB_CLASS_SEPARATOR = "$$";

    private Reflections() {
    }

    /**
     * 调用Getter方法
     * @param obj
     * @param propertyName
     * @return
     */
    public static Object invokeGetter(Object obj, String propertyName) {
        String getterMethodName = get(propertyName);
        return invokeMethod(obj, getterMethodName, new Class[]{}, new Object[]{});
    }

    /**
     * 调用Setter方法.使用value的Class来查找Setter方法.
     * @param obj
     * @param propertyName
     * @param value
     */
    public static void invokeSetter(Object obj, String propertyName, Object value) {
        invokeSetter(obj, propertyName, value, null);
    }
    /**
     * 调用Setter方法.
     * @param obj
     * @param propertyName
     * @param value
     * @param propertyType  用于查找Setter方法,为空时使用value的Class替代.
     */
    public static void invokeSetter(Object obj, String propertyName, Object value, Class<?> propertyType) {
        Class<?> type = propertyType != null ? propertyType : value.getClass();
        String setterMethodName = set(propertyName);
        invokeMethod(obj, setterMethodName, new Class[]{type}, new Object[]{value});
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            FieldUtils.log.error(Reflections.class.getName() + "[不可访问的对象]", e);
        }
        return result;
    }


    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     * @param obj
     * @param fieldName
     * @param value
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }
        try {
            field.set(obj, value);
        }  catch (IllegalAccessException ex) {
            FieldUtils.log.error(Reflections.class.getName() + "[不可访问的对象]", ex);
        }
    }

    /**
     * 对于被cglib AOP过的对象, 取得真实的Class类型.
     * @param clazz
     * @return
     */
    public static Class<?> getUserClass(Class<?> clazz) {
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符. 用于一次性调用的情况.
     * @param obj
     * @param methodName
     * @param parameterTypes
     * @param args
     * @return
     */
    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
                                      final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField,  并强制设置为可访问.
     * <p></p>
     * 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getAccessibleField(final Object obj, final String fieldName) {
        for (Class<?> superClass = obj instanceof Class ?(Class)obj:obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {//NOSONAR
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 获取一个对象中的所有字段，包括父类中的字段信息
     * @param obj
     * @return
     */
    public static List<Field> getAllFields(Object obj) {
        return getAllFields(obj,Object.class);
    }

    /**
     * 获取一个对象中的所有字段，包括父类中的字段信息
     * @param obj
     * @return
     */
    public static List<Field> getAllFields(Object obj, Class theEnd) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> superClass = obj instanceof Class ?(Class)obj: obj.getClass(); superClass != theEnd; superClass = superClass.getSuperclass()) {
            Field[] field = superClass.getDeclaredFields();
            for (Field f : field) {
                f.setAccessible(true);
                fields.add(f);
            }
        }
        return fields;
    }

    /**
     * 获取一个对象中的所有字段，包括父类中的字段信息
     * @param obj
     * @return
     */
    public static List<String> getAllFieldNames(Object obj) {
        return getAllFieldNames(obj,Object.class);
    }

    /**
     * 获取一个对象中的所有字段，包括父类中的字段信息
     * @param obj
     * @return
     */
    public static List<String> getAllFieldNames(Object obj, Class theEnd) {
        List<String> fields = new ArrayList<String>();
        for (Class<?> superClass = obj instanceof Class ?(Class)obj: obj.getClass(); superClass != theEnd; superClass = superClass.getSuperclass()) {
            Field[] field = superClass.getDeclaredFields();
            for (Field f : field) {
                f.setAccessible(true);
                fields.add(f.getName());
            }
        }
        return fields;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
     * <p></p>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object...
     * args)
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName,
                                             final Class<?>... parameterTypes) {
        for (Class<?> superClass = obj instanceof Class ?(Class)obj:  obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Method method = superClass.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException e) {//NOSONAR
                // Method不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    public static List<Method> getAllMethods(final Object obj) {
        List<Method> methods = new ArrayList<Method>();
        for (Class<?> superClass = obj instanceof Class ? (Class) obj : obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            Method[] method = superClass.getDeclaredMethods();
            for (Method method1 : method) {
                method1.setAccessible(true);
                methods.add(method1);
            }
        }
        return methods;
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg.
     * <code>
     * public UserDao
     * extends HibernateDao&lt;User&gt;
     *</code>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     * <p></p>
     * 如public UserDao extends HibernateDao&lt;User,Long&gt;
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings("rawtypes")
    public static Class getSuperClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType.getClass().isAssignableFrom(ParameterizedType.class))) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index].getClass().isAssignableFrom(Class.class))) {
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 执行反射执行
     *
     * @param className 类型
     * @return 类的实例
     */
    @SuppressWarnings("rawtypes")
    public static Object instance(String className) {
        try {
            Class dialectCls = Class.forName(className);
            return dialectCls.newInstance();
        } catch (ClassNotFoundException e) {
            FieldUtils.log.error(Reflections.class.getName() + "[无法找到方言类]", e);
            return null;
        } catch (InstantiationException e) {
            FieldUtils.log.error(Reflections.class.getName() + "[实例化方言错误]", e);
            return null;
        } catch (IllegalAccessException e) {
            FieldUtils.log.error(Reflections.class.getName() + "[实例化方言错误]", e);
            return null;
        }
    }

    /** 将反射时的checked exception转换为unchecked exception. */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }

    /**
     * 实体的getXXX方法
     *
     * @param name 成员变量名
     * @return
     */
    public static String get(String name) {
        //get+变量名的第一个字母大写
        String get = "get" + (name.charAt(0) + "").toUpperCase() + name.substring(1);
        return get;
    }

    /**
     * 实体的setXXX方法
     *
     * @param name 成员变量名
     * @return
     */
    public static String set(String name) {
        //get+变量名的第一个字母大写
        return "set" + (name.charAt(0) + "").toUpperCase() + name.substring(1);
    }

    /**
     * 反射类生产
     * @param obj
     * @return
     */
    public static Map<String,Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String,Object> queryMap = new HashMap(10);
        //父节点和自身公共属性
        Field[] fieldsParen = obj.getClass().getFields();
        //自身所有属性
        Field[] fieldsSelf = obj.getClass().getDeclaredFields();
        //有一点概率自身公共属性重复出现，直接覆盖不会产生任何问题
        Field[] fields = (Field[]) ArrayUtils.addAll(fieldsParen, fieldsSelf);
        for(Field field : fields){
            field.setAccessible(true);
            String name = field.getName();
            Object val = field.get(obj);
            if(null != val && val.getClass().isArray()){
                val = StringUtils.join((Object[]) val, ",");
            }
            queryMap.put(name,val);
        }
        return  queryMap;
    }

    /**
     *
     * @param map 源Map.
     * @param classType 目标对象类型
     * @param <T> 目标对象类型
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ParseException
     */
    public static <T> T mapToObject(Map map, Class<T> classType) throws IllegalAccessException, InstantiationException, ParseException {
        return (T)mapToObject(map,classType, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
    }

    /**
     *
     * @param map 源Map.
     * @param classType 目标对象类型
     * @param genericMap 泛型field对应的泛型类
     * @param keyFieldMap 将map中的属性值按照keyFieldMap映射放到指定classType中.默认按map中的原key
     * @param <T> 目标对象类型
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ParseException
     */
    public static <T> T mapToObject(Map map, Class<T> classType, Map<String,Class> genericMap, Map<String,String> keyFieldMap) throws IllegalAccessException, InstantiationException, ParseException {
        for(String mapkey:keyFieldMap.keySet()){
            String fieldKey=keyFieldMap.get(mapkey);
            map.put(fieldKey,map.get(mapkey));
            //因为oracle中查出来的都是大写
            map.put(fieldKey,map.get(mapkey.toUpperCase()));
        }
        T obj = classType.newInstance();
        List<Field> allFields = getAllFields(obj);
        for (Field field : allFields) {
            Class fieldType = field.getType();
            String name = field.getName();
            Object value = map.get(name.toUpperCase());

            if(value == null)
                //如果全大写获取不到 再次使用原名获取一次
                 value = map.get(name);
            if (value != null) {
                if(value instanceof Collection){
                    //如果是数组
                    Class genericType = genericMap.get(name);
                    if(genericType!=null&& ObjectHelper.isNotEmpty(value)){
                        Collection collection=(Collection)value;
                        Object[] array = collection.toArray();
                        collection.clear();
                        if(array[0] instanceof Map){
                            for(Object object:array){
                                collection.add(mapToObject((Map) object, genericType, genericMap, keyFieldMap));
                            }
                        }
                    }
                }else if (!(value instanceof Map)) {
                    //如果全大写获取不到 再次使用原名获取一次
                    if (value != null) {
                        if (!fieldType.isAssignableFrom(value.getClass())) {
                            value = ConverString.asType(fieldType, value.toString());
                        }
                    }
                } else {//如果对象中还有对象
                    value = mapToObject((Map) value, fieldType,genericMap,keyFieldMap);
                }
                field.set(obj, value);
            }
        }
        return obj;
    }

    /**
     *
     * @param map 源Map.
     * @param classType 目标对象类型
     * @param genericMap 泛型field对应的泛型类
     * @param <T> 目标对象类型
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ParseException
     */
    public static <T> T mapToObject(Map map, Class<T> classType, Map<String,Class> genericMap) throws IllegalAccessException, InstantiationException, ParseException {
        return (T)mapToObject(map,classType,genericMap, Collections.EMPTY_MAP);
    }

    public static boolean isSimpleType(Class clazz) {
        return Number.class.isAssignableFrom(clazz) || clazz.equals(String.class)
                || Date.class.isAssignableFrom(clazz)
                || boolean.class.isAssignableFrom(clazz)|| Boolean.class.isAssignableFrom(clazz);
    }
}