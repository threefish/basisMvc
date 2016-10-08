package com.sgaop.basis.util;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/13 0013
 * To change this template use File | Settings | File Templates.
 */
public class StringsTool {

    /**
     * 判断是否为空串
     *
     * @param str
     * @return true: null/""
     * false: 其它
     */
    public static boolean isNullorEmpty(String str) {
        if (str == null) {
            return true;
        }
        str = str.trim();
        if (str.length() < 1) {
            return true;
        }
        return false;
    }

    /**
     * 处理空值的字符串
     *
     * @param str
     * @return
     */
    public static String null2Empty(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }


    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * 处理空值的字符串
     *
     * @param str        源字符串
     * @param defaultStr 默认字符串
     * @return
     */
    public static String empty2Default(String str,
                                       String defaultStr) {

        if (isNullorEmpty(str)) {
            return defaultStr;
        }
        return str;
    }

    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        return Pattern.matches("-?\\d*\\.?\\d+(E\\d+)?", str);
    }

    /**
     * 判断是否是金额数字
     *
     * @param str
     * @return
     */
    public static boolean isAmtNumber(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        return Pattern.matches("-?\\d{1,3}(\\,\\d{3})*\\.\\d{0,8}", str);
    }

    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        return Pattern.matches("(-?[1-9]\\d*)|0", str);
    }

    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isPositiveInteger(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        return Pattern.matches("[1-9]\\d*", str);
    }

    /**
     * 判断是否符合帐务年月的格式
     *
     * @param str
     * @return
     */
    public static boolean isAcctYMFormat(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        return Pattern.matches("\\d{4}-\\d{2}", str);
    }

    /**
     * 判断是否符合系统规定的日期格式
     *
     * @param str
     * @return
     */
    public static boolean isSysDateFormat(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        if (str.length() < 10) {
            return false;
        }
        if (str.length() == 10) {
            return Pattern.matches("\\d{4}-\\d{2}-\\d{2}", str);
        } else {
            return Pattern.matches("\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}", str);
        }
    }

    /**
     * 判断是否符合系统规定的日期格式
     *
     * @param str
     * @return
     */
    public static boolean isDay(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        if (str.length() < 10) {
            return false;
        }
        return Pattern.matches("\\d{4}-\\d{2}-\\d{2}", str);
    }

    /**
     * 判断是否符合系统规定的日期格式
     *
     * @param str
     * @return
     */
    public static boolean isDayTime(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        if (str.length() < 19) {
            return false;
        }
        return Pattern.matches("\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}", str);
    }

}
