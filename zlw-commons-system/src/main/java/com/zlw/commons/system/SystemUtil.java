package com.zlw.commons.system;

import java.util.Properties;

/**
 * @author limingwei
 * @date 2016年6月13日 下午3:21:39
 */
public class SystemUtil {
    public static Properties getEnvAndProperties() {
        Properties properties = new Properties();
        properties.putAll(System.getenv());
        properties.putAll(System.getProperties());
        return properties;
    }
}