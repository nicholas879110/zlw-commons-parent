package com.zlw.commons.beanutils;

import com.zlw.commons.json.util.JsonUtil;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    public static <T> List copyList(List list, Class<T> clazz) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList();
        }
        return JsonUtil.toObjectList(JsonUtil.toJson(list), clazz);
    }

    public static Map<String, Object> copyMap(Map map) {
        return JsonUtil.jsonToMap(JsonUtil.toJson(map));
    }

    public static void copyProperties(Object dest, Object orig) {
        try {

            BeanUtilsBean.getInstance().copyProperties(dest, orig);
        } catch (Exception e) {
            logger.error("BeanUtils.copyProperties(Object dest, Object orig) throws Exception", e);
        }
    }

}
