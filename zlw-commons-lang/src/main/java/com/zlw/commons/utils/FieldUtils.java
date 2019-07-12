package com.zlw.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * @description
 * @date 2017/10/27
 * @author zhangliewei
 */
public class FieldUtils {

    /**
     * 加返回自身实例的方法标签VM中直接用到
     */
    public final static FieldUtils MY_INSTANCE = new FieldUtils();
    protected transient final static Logger log = LoggerFactory.getLogger(FieldUtils.class);

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
     * 取得一个成员变量的值
     *
     * @param tablebean tablebean
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object tablebean, String fieldName) {
        if (tablebean == null || fieldName == null || fieldName.trim().equals("")) {
            return null;
        } else if (Map.class.isAssignableFrom(tablebean.getClass())) {
            return ((Map) tablebean).get(fieldName);
        }

        if (tablebean.getClass().isArray()) {
            //如果是数组直接返回其索引元素
            return ((Object[]) tablebean)[Integer.parseInt(fieldName)];
        } else if (Collection.class.isAssignableFrom(tablebean.getClass())) {
            //如果是集合直接返回其索引元素
            return ((Collection) tablebean).toArray()[Integer.parseInt(fieldName)];
        } else {
            try {
                //取得get方法
                Method method = tablebean.getClass().getMethod(get(fieldName), (Class[]) null);
                //调用实体类的getXXX方法
                return method.invoke(tablebean, (Object[]) (Class[]) null);
            } catch (NoSuchMethodException noSuchMethodException) {
                log.error("没有这个方法：" + get(fieldName), noSuchMethodException);
            } catch (IllegalAccessException illegalAccessException) {
            } catch (InvocationTargetException invocationTargetException) {
            }
        }
        return null;
    }

    /**
     * 循环获取子对象
     *
     * @param tablebean
     * @param fieldName
     * @return
     */
    public static Object getSuperFieldValue(Object tablebean, String fieldName) {
        Object value = tablebean;
        if (fieldName == null || "".equals(fieldName.trim())) {
            return tablebean;
        }
        String[] split = fieldName.split("[.]");
        for (int i = 0; i < split.length - 1; i++) {
            value = getFieldValue(value, split[i]);
        }
        return getFieldValue(value, split[split.length - 1]);
    }

    /**
     * 循环获取子对象
     *
     * @param tablebean  源对象
     * @param fieldName  字段名
     * @param defaultVal 如果为null返回的值
     * @return
     */
    public static Object getSuperFieldValue(Object tablebean, String fieldName, Object defaultVal) {
        Object superFieldValue = getSuperFieldValue(tablebean, fieldName);
        return superFieldValue == null ? defaultVal : superFieldValue;
    }

    /**
     * 存入一个实体的成员变量值
     *
     * @param tablebean
     * @param fieldName
     * @param value
     * @return
     */
    public static Object setFieldValue(Object tablebean, String fieldName, Object value) {
        if (value == null) {
            return tablebean;
        }
        try {
            if (tablebean instanceof Map) {
                ((Map) tablebean).put(fieldName, value);
            } else {
                log.debug("准备为对象进行setter方法注入");
                //取得set方法
                Method method = tablebean.getClass().getMethod(set(fieldName), value.getClass());
                //调用实体类的setXXX方法
                method.invoke(tablebean, value);
            }
        } catch (IllegalAccessException ex) {
            log.error("FieldUtil错误：参数不正确", ex);
        } catch (NoSuchMethodException ex) {
            log.debug("为对象进行setter方法注入失败改为直接注入");
            Reflections.setFieldValue(tablebean, fieldName, value);
        } catch (InvocationTargetException ex) {
            log.error("FieldUtil：存入一个实体的成员变量值失败！", ex);
        }
        return tablebean;
    }

    /**
     * 为对象进行spring注射
     * @param object
     */
//    public static void inject(Object object) {
//        Field[] declaredFields = object.getClass().getDeclaredFields();
//        for (Field field : declaredFields) {
//            if (field.isAnnotationPresent(Resource.class) || field.isAnnotationPresent(Autowired.class)) {
//                setFieldValue(object, field.getName(), SpringContextUtil.getBean(field.getName()));
//                log.debug("成功为对象" + object.getClass().getSimpleName() + "注射字段：" + field.getName());
//            }
//        }
//    }
}
