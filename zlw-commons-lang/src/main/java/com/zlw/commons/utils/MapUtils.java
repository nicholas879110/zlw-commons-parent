package com.zlw.commons.utils;


import java.text.ParseException;
import java.util.*;

/**
 * @description
 * @date 2017/10/27
 * @author zhangliewei
 */
public class MapUtils {
    /**
     * 将List中的对象根据Key整理到Map
     *
     * @param list
     * @param key
     * @return
     */
    public static Map listToMap(List list, String key) {
        Map map = new HashMap(10);
        if (ObjectHelper.isNotEmpty(list)) {
            for (Object object : list) {
                map.put(FieldUtils.getFieldValue(object, key), object);
            }
        }
        return map;
    }

    /**
     * 复制属性
     *
     * @param maps
     * @param keys
     */
    public static void copyProperty(List<Map> maps, Map<String, String> keys) {
        if (ObjectHelper.isNotEmpty(maps)) {
            for (Map map : maps) {
                for (String key : keys.keySet()) {
                    String newKey = keys.get(key);
                    Object value = map.get(key);
                    if (ObjectHelper.isNotEmpty(newKey) && ObjectHelper.isNotEmpty(value)) {
                        map.put(newKey, value);
                    }
                }
            }
        }
    }

    /**
     * 从map中填充List对象中的忏悔
     *
     * @param list
     * @param map
     * @param fieldName 对象有值的属性名
     * @param valueKey  对象将填写的属性名
     */
    public static void mergeListByMap(List list, Map map, String fieldName, String valueKey) {
        if (ObjectHelper.isNotEmpty(list)) {
            for (Object object : list) {
                FieldUtils.setFieldValue(object, fieldName, FieldUtils.getFieldValue(map.get(FieldUtils.getFieldValue(object, valueKey)), fieldName));
            }
        }
    }

    /**
     * 把List<Map>里面的内容转换成List<PO>,各种键忽略大小写.
     *
     * @param mapsList   要转换的东西
     * @param objectType PO的泛型类
     * @param <T>
     * @return
     */
    public static <T> List<T> mapToObjectIgnoreCase(List<Map> mapsList, Class<T> objectType) {
        List<T> list = new ArrayList<T>();
        if (ObjectHelper.isNotEmpty(mapsList)) {
            Object[] keys = mapsList.get(0).keySet().toArray();
            for (Map<String, String> map : mapsList) {
                for (Object key : keys) {
                    map.put(key.toString().toUpperCase(), map.get(key));
                }
                try {
                    list.add(Reflections.mapToObject(map, objectType));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 把Map里面的内容转换成PO,各种键忽略大小写.
     *
     * @param map        要转换的东西
     * @param objectType PO的泛型类
     * @param <T>
     * @return
     */
    public static <T> T mapToObjectIgnoreCase(Map map, Class<T> objectType) {
        for (Object key : map.keySet()) {
            map.put(key.toString().toUpperCase(), map.get(key));
        }
        try {
            return Reflections.mapToObject(map, objectType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将一般的Map转成LinkMap
     *
     * @param map
     * @return
     */
    public static LinkedHashMap mapToLinkMap(Map map) {
        return new LinkedHashMap<>(map);
    }

    /**
     * List<Map>排序
     *
     * @param list 要排序的LIST
     * @param key  根据哪个字段排序
     * @param desc 是否是倒序排序
     */
    public static void mapSort(List<Map> list, final String key, final boolean desc) {
        Collections.sort(list, new Comparator<Map>() {
            @Override
            public int compare(Map map1, Map map2) {
                Object o1 = desc ? map2.get(key) : map1.get(key);
                Object o2 = desc ? map1.get(key) : map2.get(key);
                if (o1 == null && o2 != null) {
                    return -1;
                } else if (o1 != null && o2 == null) {
                    return 1;
                } else if (o1 == null && o2 == null) {
                    return 0;
                } else if (o1 instanceof Number) {
                    return (int) (((Number) o1).doubleValue() - ((Number) o2).doubleValue());
                } else if (o1 instanceof String) {
                    return ((String) o1).compareTo((String) o2);
                }
                return 0;
            }
        });
    }

    /**
     * 为List<Map>所有元素添加相同属性
     *
     * @param list
     * @param values
     */
    public static void setValue4List(List<Map> list, final Map values) {
        for (Map map : list) {
            map.putAll(values);
        }
    }

}