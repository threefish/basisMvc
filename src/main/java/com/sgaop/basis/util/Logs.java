package com.sgaop.basis.util;

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
            return Logger.getLogger(sts[2].getClassName());
        } catch (Exception e) {
            return Logger.getRootLogger();
        }
    }
}
