package com.zlw.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Description:日期时间工具类:获取/转换时间
 * 
 * @author fukui
 *
 * @see org.apache.commons.lang3.time.DateUtils
 * @see org.apache.commons.lang3.time.DateFormatUtils
 */
public final class TimeUtil extends DateUtils {
    private static final Logger log = LoggerFactory.getLogger(TimeUtil.class);

    /**
     * 日期格式：yyyyMMdd，如20130812=2013年8月12日
     */
    public static final String PATTERN_YYYYMMDD = "yyyyMMdd";

    /**
     * 日期格式：yyMMdd，如130812=2013年8月12日
     */
    public static final String PATTERN_YYMMDD = "yyMMdd";

    /**
     * 日期格式：yyyyMMddHHmmss，如20130812092030=2013年8月12日9点20分30秒
     */
    public static final String PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    /**
     * 日期格式：HHmmss，如092030=9点20分30秒
     */
    public static final String PATTERN_HHMMSS = "HHmmss";

    /**
     * 获取当前系统日期,例：20130812
     * 
     * @return 当前日期yyyyMMdd格式字符串
     */
    public static final String getCurrentDate8() {
        return getCurrentTimeString(PATTERN_YYYYMMDD);
    }

    /**
     * 获取当前系统日期,例：130812
     * 
     * @return 当前日期yyMMdd格式字符串
     */
    public static final String getCurrentDate6() {
        return getCurrentTimeString(PATTERN_YYMMDD);
    }

    /**
     * 获取当前系统日期时间,例：20130812092030
     * 
     * @return 当前日期时间yyyyMMddHHmmss格式字符串
     */
    public static final String getCurrentDateTime14() {
        return getCurrentTimeString(PATTERN_YYYYMMDDHHMMSS);
    }

    /**
     * 获取当前系统时间,例：092030
     * 
     * @return 当前日期时间HHmmss格式字符串
     */
    public static final String getCurrentTime6() {
        return getCurrentTimeString(PATTERN_HHMMSS);
    }

    /**
     * 获取当前系统日期时间的格式化字符串,默认采用yyyyMMddHHmmss格式
     * 
     * @param pattern
     *            格式化字符串
     * @return 表示当前系统日期时间的格式化字符串
     */
    public static final String getCurrentTimeString(String pattern) {
        if (StringUtils.isEmpty(pattern)) {// 默认处理
            pattern = PATTERN_YYYYMMDDHHMMSS;
        }
        return DateFormatUtils.format(Calendar.getInstance(), pattern);
    }

    /**
     * 日期偏移计算
     * 
     * <pre>
     * assertNull(TimeUtil.daysOffset(null, null, 0));
     * assertEquals(&quot;&quot;, TimeUtil.daysOffset(&quot;&quot;, null, 0));
     * assertEquals(&quot;20130813&quot;, TimeUtil.daysOffset(&quot;20130812&quot;, null, 1));// 后移
     * assertEquals(&quot;20130811&quot;, TimeUtil.daysOffset(&quot;20130812&quot;, null, -1));// 前移
     * assertEquals(&quot;20130301&quot;, TimeUtil.daysOffset(&quot;20130228&quot;, null, 1));// 2月
     * assertEquals(&quot;20130101&quot;, TimeUtil.daysOffset(&quot;20121229&quot;, null, 3));// 跨年
     * assertEquals(&quot;130101&quot;, TimeUtil.daysOffset(&quot;121229&quot;, &quot;yyMMdd&quot;, 3));// 跨年,指定格式
     * </pre>
     * 
     * @param srcDate
     *            原日期
     * @param pattern
     *            日期格式
     * @param offset
     *            偏移量，正数表示后移，负数表示前移
     * @return 日期偏移后的格式化字符串
     */
    public static final String daysOffset(String srcDate, String pattern, int offset) {
        if (StringUtils.isEmpty(srcDate)) {
            return srcDate;// balking
        }
        if (StringUtils.isEmpty(pattern)) {// 默认格式
            pattern = PATTERN_YYYYMMDD;
        }
        try {
            Date before = parseDateStrictly(srcDate, pattern);// 解析成日期类型
            Date after = addDays(before, offset);// 偏移计算
            String afterFormat = DateFormatUtils.format(after, pattern);
            if (log.isDebugEnabled()) {
                log.debug("# daysOffset({},{},{})={}", srcDate, pattern, offset, afterFormat);
            }
            return afterFormat;
        } catch (ParseException e) {
            throw new IllegalArgumentException("解析日期异常", e);
        }
    }

    /**
     * 当前线程休眠指定时间，单位：毫秒
     * 
     * @param milliSec
     *            休眠毫秒数
     * @param info
     *            休眠原因，不为空者日志输出
     */
    public static final void sleepMilliSec(long milliSec, String info) {
        if (StringUtils.isNotEmpty(info)) {
            log.info("#sleepMilliSec({}) for {}!", milliSec, info);
        }
        try {
            TimeUnit.MILLISECONDS.sleep(milliSec);
        } catch (InterruptedException e) {
            log.warn("#sleepMilliSec({}) interrupted!", milliSec);
            // ignore
        }
    }

    /**
     * @see #sleepMilliSec(long, String)
     */
    public static final void sleepMilliSec(long milliSec) {
        sleepMilliSec(milliSec, null);
    }

    /**
     * 当前线程休眠指定时间，单位：秒
     * 
     * @param sec
     *            休眠秒数
     * @param info
     *            休眠原因，不为空者日志输出
     */
    public static final void sleepSec(long sec, String info) {
        if (StringUtils.isNotEmpty(info)) {
            log.info("#sleepSec({}) for {}!", sec, info);
        }
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            log.warn("#sleepSec({}) interrupted!", sec);
            // ignore
        }
    }

    /**
     * @see #sleepSec(long, String)
     */
    public static final void sleepSec(long sec) {
        sleepSec(sec, null);
    }

    /**
     * 当前线程休眠指定时间，单位：纳秒
     * 
     * @param nanoSec
     *            休眠纳秒数
     * @param info
     *            休眠原因，不为空者日志输出
     */
    public static final void sleepNanoSec(long nanoSec, String info) {
        if (StringUtils.isNotEmpty(info)) {
            log.info("#sleepNanoSec({}) for {}!", nanoSec, info);
        }
        try {
            TimeUnit.NANOSECONDS.sleep(nanoSec);
        } catch (InterruptedException e) {
            log.warn("#sleepNanoSec({}) interrupted!", nanoSec);
            // ignore
        }
    }

    /**
     * @see #sleepNanoSec(long, String)
     */
    public static final void sleepNanoSec(long sec) {
        sleepNanoSec(sec, null);
    }

    /**
     * @see org.apache.commons.lang3.time.DateFormatUtils#format(java.util.Date, String)
     */
    public static final String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    public static final String formatDate8(Date date) {
        return DateFormatUtils.format(date, PATTERN_YYYYMMDD);
    }

    public static final String formatDateTime14(Date date) {
        return DateFormatUtils.format(date, PATTERN_YYYYMMDDHHMMSS);
    }

    public static final String formatTime6(Date date) {
        return DateFormatUtils.format(date, PATTERN_HHMMSS);
    }

    /**
     * @see org.apache.commons.lang3.time.DateFormatUtils#format(java.util.Calendar, String)
     */
    public static final String format(Calendar calendar, String pattern) {
        return DateFormatUtils.format(calendar, pattern);
    }

    public static final String formatDate8(Calendar calendar) {
        return DateFormatUtils.format(calendar, PATTERN_YYYYMMDD);
    }

    public static final String formatDateTime14(Calendar calendar) {
        return DateFormatUtils.format(calendar, PATTERN_YYYYMMDDHHMMSS);
    }

    public static final String formatTime6(Calendar calendar) {
        return DateFormatUtils.format(calendar, PATTERN_HHMMSS);
    }

    /**
     * @see org.apache.commons.lang3.time.DateFormatUtils#format(long, String)
     */
    public static final String format(long millis, String pattern) {
        return DateFormatUtils.format(millis, pattern);
    }

    public static final String formatDate8(long millis) {
        return DateFormatUtils.format(millis, PATTERN_YYYYMMDD);
    }

    public static final String formatDateTime14(long millis) {
        return DateFormatUtils.format(millis, PATTERN_YYYYMMDDHHMMSS);
    }

    public static final String formatTime6(long millis) {
        return DateFormatUtils.format(millis, PATTERN_HHMMSS);
    }

}
