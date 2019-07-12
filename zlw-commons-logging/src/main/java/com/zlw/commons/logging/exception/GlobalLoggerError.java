package com.zlw.commons.logging.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 将异常信息输入到Log中，不在控制台打印
 *
 * @author fukui
 */
public class GlobalLoggerError {

    private static Logger logger = LoggerFactory.getLogger(GlobalLoggerError.class);

    public static void error(Throwable e) {
        StringWriter stringWriter = null;
        PrintWriter printWriter = null;
        try {
            stringWriter = new StringWriter();
            printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            printWriter.flush();
            stringWriter.flush();
        } finally {
            if (stringWriter != null) {
                try {
                    stringWriter.close();
                } catch (IOException e1) {
                    logger.error("", e);
                }
            }
            if (printWriter != null) {
                printWriter.close();
            }
        }
        logger.error(stringWriter.toString());
    }
}
