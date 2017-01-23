package com.sgaop.basis.log;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/12/29 0029
 * To change this template use File | Settings | File Templates.
 */
public class Logs {

    public static Logger get() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        try {
            if (null == sts)
                return Logger.getRootLogger();
            return Logger.getLogger(sts[2].getClassName());
        } catch (Exception e) {
            return Logger.getRootLogger();
        }
    }

    public static void trace(String msg) {
        trace(msg, null);
    }

    public static void trace(String msg, Throwable e) {
        get().trace(msg, e);
    }

    public static void debug(String msg) {
        debug(msg, null);
    }

    public static void debug(Throwable e) {
        get().debug(e);
    }

    public static void debug(String msg, Throwable e) {
        get().debug(msg, e);
    }

    public static void info(String msg) {
        info(msg, null);
    }

    public static void info(String msg, Throwable e) {
        get().info(msg, e);
    }

    public static void warn(String msg) {
        warn(msg, null);
    }

    public static void warn(Throwable e) {
        get().warn(e);
    }

    public static void warn(String msg, Throwable e) {
        get().warn(msg, e);
    }

    public static void error(String msg) {
        error(msg, null);
    }

    public static void error(Throwable e) {
        get().error(e);
    }

    public static void error(String msg, Throwable e) {
        get().error(msg, e);
    }

}
