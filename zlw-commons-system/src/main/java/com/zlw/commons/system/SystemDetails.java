package com.zlw.commons.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * 输出服务器信息
 * @author fukui
 *
 */
public class SystemDetails {
	
    private static final Logger log = LoggerFactory.getLogger(SystemDetails.class);

    /**
     * 输出系统基本信息
     */
    public static void outputDetails() {
        timeZone();
        currentTime();
        os();
        getLocalip();
    }

    /**
     * 输出系统时区
     */
    private static void timeZone() {
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = cal.getTimeZone();
        log.info("系统时区:" + timeZone.getDisplayName());
    }

    /**
     * 输出系统时间
     */
    private static void currentTime() {
        String fromFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(fromFormat);
        Date myDate = new Date();
        log.info("系统时间:" + format.format(myDate));
    }

    /**
     * 输出系统基本配置
     */
    private static void os() {
        String osName = System.getProperty("os.name"); //操作系统名称
        log.info("当前系统:" + osName);
        String osArch = System.getProperty("os.arch"); //操作系统构架
        log.info("当前系统架构" + osArch);
        String osVersion = System.getProperty("os.version"); //操作系统版本
        log.info("当前系统版本:" + osVersion);
    }

    /**
     * 取得本机ip地址 注意：Spring RmiServiceExporter取得本机ip的方法：InetAddress.getLocalHost()
     */
    private static void getLocalip() {
        try {
            log.info("服务暴露的ip: " + java.net.InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
