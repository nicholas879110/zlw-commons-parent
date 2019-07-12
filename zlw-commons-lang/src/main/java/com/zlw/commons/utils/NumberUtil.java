package com.zlw.commons.utils;

import org.apache.commons.lang3.math.NumberUtils;

/**
 *数字处理工具类，继承自：org.apache.commons.lang3.math.NumberUtils
 */
public class NumberUtil extends NumberUtils {
	
	
    public static double roundTo(double val, int places) {
        double factor = Math.pow(10, places);
        return ((int) ((val * factor) + 0.5)) / factor;
    }
    
    
    
}
