package com.sgaop.basis.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/13 0013
 * To change this template use File | Settings | File Templates.
 */
public class DateTool {
    /**
     * 秒
     */
    public static final long DT_S = 1000;
    /**
     * 分
     */
    public static final long DT_MINIT = DT_S * 60;
    /**
     * 时
     */
    public static final long DT_H = DT_MINIT * 60;
    /**
     * 天
     */
    public static final long DT_D = DT_H * 24;
    /**
     * 月
     */
    public static final long DT_MONTH = DT_D * 30;
    /**
     * 年
     */
    public static final long DT_Y = DT_D * 365;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat dateFormatS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    private static SimpleDateFormat dateFormatEn = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    private static SimpleDateFormat dateFormatCn = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINESE);
    private static SimpleDateFormat dateFormatYMEn = new SimpleDateFormat("MMM-yyyy", Locale.ENGLISH);
    private static SimpleDateFormat dateFormatYMCn = new SimpleDateFormat("yyyy年MM月", Locale.CHINESE);
    private static SimpleDateFormat dateFormatMDEn = new SimpleDateFormat("dd-MMM", Locale.ENGLISH);
    private static SimpleDateFormat dateFormatMDCn = new SimpleDateFormat("MM-dd", Locale.CHINESE);

    /**
     * 把字符串转化为Date
     *
     * @param dateStr
     * @return
     */
    public static Date parseDate(String formatStr, String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
        return dateFormat.parse(dateStr);
    }

    /**
     * 把字符串转化为Date
     *
     * @param dateStr
     * @return
     */
    public static Date parseDate(String dateStr) throws ParseException {
        if (dateStr == null || "".equals(dateStr)) {
            return null;
        }
        return dateFormat.parse(dateStr);
    }

    /**
     * 当前日期转化成java.sql.Date对象
     *
     * @return
     */
    public static java.sql.Timestamp parseTimeStamp() throws ParseException {
        return parseTimeStamp(null);
    }

    /**
     * 把字符串转化为java.sql.Date对象
     *
     * @param longDate
     * @return
     */
    public static java.sql.Timestamp parseTimeStamp(long longDate) throws ParseException {
        Date utilDate = new Date(longDate);
        java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());
        return sqlDate;
    }

    /**
     * 把字符串转化为java.sql.Date对象
     *
     * @param dateStr
     * @return
     */
    public static java.sql.Timestamp parseTimeStamp(String dateStr) throws ParseException {
        Date utilDate = null;
        if (dateStr == null) {
            utilDate = new Date();
        } else {
            utilDate = parseDate(dateStr);
        }
        java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());
        return sqlDate;
    }

    /**
     * 当前日期转化成java.sql.Date对象
     *
     * @return
     */
    public static java.sql.Date parseSqlDate() throws ParseException {
        return parseSqlDate(null);
    }

    /**
     * 把字符串转化为java.sql.Date对象
     *
     * @param dateStr
     * @return
     */
    public static java.sql.Date parseSqlDate(String dateStr) throws ParseException {
        Date utilDate = null;
        if (dateStr == null) {
            utilDate = new Date();
        } else {
            utilDate = parseDate(dateStr);
        }
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        return sqlDate;
    }


    /**
     * 取得当前的时间，格式为 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurDateTimeStr() {
        return dateFormat.format(new Date());
    }

    /**
     * 取得当前的时间，格式为 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurDateTimeStr(String format) {
        if (format == null) {
            return getCurDateTimeStr();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date());
    }


    /**
     * 取得的时间串，格式为 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getDateTimeStr(Date date) {
        if (date == null) {
            return getCurDateTimeStr();
        }
        return dateFormat.format(date);
    }


    /**
     * Date清零
     *
     * @param date
     * @param clearNum 1=毫秒, 2=秒, 3=分钟, 4=小时, 5=天, 6=月份
     * @return
     */
    private static Calendar clearDate(Date date, int clearNum) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        //毫秒
        if (clearNum > 0) {
            cal.set(Calendar.MILLISECOND, 0);
        }
        //秒

        if (clearNum > 1) {
            cal.set(Calendar.SECOND, 0);
        }
        //分钟
        if (clearNum > 2) {
            cal.set(Calendar.MINUTE, 0);
        }
        //小时
        if (clearNum > 3) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
        }
        //天

        if (clearNum > 4) {
            cal.set(Calendar.DATE, 0);
        }
        //月份
        if (clearNum > 5) {
            cal.set(Calendar.MONTH, 0);
        }
        return cal;
    }

    /**
     * 取得指定日期的起始时间串
     *
     * @return
     */
    public static String[] getDateLimitStr() throws Exception {
        return getDateLimitStr(new Date());
    }

    /**
     * 取得指定日期当周的起始时间串
     *
     * @return
     */
    public static String[] getWeekLimitStr() throws Exception {
        return getWeekLimitStr(new Date());
    }

    /**
     * 取得指定日期当月的起始时间串
     *
     * @return
     */
    public static String[] getMonthLimitStr() throws Exception {
        return getMonthLimitStr(new Date());
    }

    /**
     * 取得指定日期当年的起始时间串
     *
     * @return
     */
    public static String[] getYearLimitStr() throws Exception {
        return getYearLimitStr(new Date());
    }

    /**
     * 取得指定日期的起始时间串
     *
     * @param date
     * @return
     */
    public static String[] getDateLimitStr(Date date) throws Exception {
        Date[] rtDateArray = getDateLimit(date);
        return new String[]{getDateTimeStr(rtDateArray[0]), getDateTimeStr(rtDateArray[1])};
    }

    /**
     * 取得指定日期当周的起始时间串
     *
     * @param date
     * @return
     */
    public static String[] getWeekLimitStr(Date date) throws Exception {
        Date[] rtDateArray = getWeekLimit(date);
        return new String[]{getDateTimeStr(rtDateArray[0]), getDateTimeStr(rtDateArray[1])};
    }

    /**
     * 取得指定日期当月的起始时间串
     *
     * @param date
     * @return
     */
    public static String[] getMonthLimitStr(Date date) throws Exception {
        Date[] rtDateArray = getMonthLimit(date);
        return new String[]{getDateTimeStr(rtDateArray[0]), getDateTimeStr(rtDateArray[1])};
    }

    /**
     * 取得指定日期当年的起始时间串
     *
     * @param date
     * @return
     */
    public static String[] getYearLimitStr(Date date) throws Exception {
        Date[] rtDateArray = getYearLimit(date);
        return new String[]{getDateTimeStr(rtDateArray[0]), getDateTimeStr(rtDateArray[1])};
    }

    /**
     * 取得指定日期的起始时间
     *
     * @param date
     * @return
     */
    public static Date[] getDateLimit(Date date) throws Exception {
        Calendar cal = clearDate(date, 4);
        Date date1 = cal.getTime();

        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.SECOND, -1);
        Date date2 = cal.getTime();

        return new Date[]{date1, date2};
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = clearDate(date, 4);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = clearDate(date, 4);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 000);
        return c.getTime();
    }

    /**
     * 取得指定日期的当周的起始时间
     *
     * @param date
     * @return
     */
    public static Date[] getWeekLimit(Date date) throws Exception {
        Date date1 = getFirstDayOfWeek(date);
        Date date2 = getLastDayOfWeek(date);
        return new Date[]{date1, date2};
    }

    /**
     * 取得指定日期的当月起始时间
     *
     * @param date
     * @return
     */
    public static Date[] getMonthLimit(Date date) throws Exception {
        Calendar cal = clearDate(date, 5);
        cal.set(Calendar.DATE, 1);
        Date date1 = cal.getTime();

        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.SECOND, -1);
        Date date2 = cal.getTime();

        return new Date[]{date1, date2};
    }

    /**
     * 取得指定日期的当年起始时间
     *
     * @param date
     * @return
     */
    public static Date[] getYearLimit(Date date) throws Exception {
        Calendar cal = clearDate(date, 6);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DATE, 1);
        Date date1 = cal.getTime();

        cal.add(Calendar.YEAR, 1);
        cal.add(Calendar.SECOND, -1);
        Date date2 = cal.getTime();

        return new Date[]{date1, date2};
    }

    /**
     * 取得天数间隔
     *
     * @return
     */
    public static int getDaySpan(String toDateStr) throws Exception {
        return (int) ((parseDate(toDateStr).getTime() - new Date().getTime()) / DT_D);
    }

    /**
     * 取得天数间隔
     *
     * @param toDate
     * @return
     */
    public static int getDaySpan(Date toDate) {
        return (int) ((toDate.getTime() - new Date().getTime()) / DT_D);
    }

    /**
     * 取得天数间隔
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public static int getDaySpan(Date fromDate, Date toDate) {
        return (int) ((toDate.getTime() - fromDate.getTime()) / DT_D);
    }

    /**
     * 取得前一天的时间
     *
     * @param dateStr
     * @return
     */
    public static Date getDayBefore(String dateStr, int dayCnt) throws Exception {
        return getDayBefore(parseDate(dateStr), dayCnt);
    }

    /**
     * 取得前一天的时间
     *
     * @param date
     * @return
     */
    public static Date getDayBefore(Date date, int dayCnt) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, 0 - dayCnt);
        return cal.getTime();
    }

    /**
     * 取得后一天的时间字
     *
     * @param dateStr
     * @return
     */
    public static Date getDayAfter(String dateStr, int dayCnt) throws Exception {
        return getDayAfter(parseDate(dateStr), dayCnt);
    }

    /**
     * 取得后一天的时间
     *
     * @param date
     * @return
     */
    public static Date getDayAfter(Date date, int dayCnt) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, dayCnt);
        return cal.getTime();
    }

    /**
     * 取得指定天数差的时间字
     *
     * @param dateStr
     * @return
     */
    public static Date getDayDiff(String dateStr, int dayCnt) throws Exception {
        return getDayDiff(parseDate(dateStr), dayCnt);
    }

    /**
     * 取得指定天数差的时间
     *
     * @param date
     * @return
     */
    public static Date getDayDiff(Date date, int dayCnt) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, dayCnt);
        return cal.getTime();
    }

    /**
     * 取得前一天的时间字
     *
     * @param dateStr
     * @return
     */
    public static Date getYestday(String dateStr) throws Exception {
        return getYestday(parseDate(dateStr));
    }

    /**
     * 取得前一天的时间
     *
     * @param date
     * @return
     */
    public static Date getYestday(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    /**
     * 取得前一天的时间字符串
     *
     * @param dateStr
     * @return
     */
    public static String getYestdayStr(String dateStr) throws Exception {
        return getYestdayStr(parseDate(dateStr));
    }

    /**
     * 取得前一天的时间字符串
     *
     * @param date
     * @return
     */
    public static String getYestdayStr(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return getDateTimeStr(cal.getTime());
    }

    /**
     * 取得前一月的时间字符串
     *
     * @return
     */
    public static String getMonthBefore(String dateStr, int diff) throws Exception {
        return getMonthBefore(parseDate(dateStr), diff);
    }

    /**
     * 取得前一月的时间字符串
     *
     * @param date
     * @return
     */
    public static String getMonthBefore(Date date, int diff) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 0 - diff);
        return getDateTimeStr(cal.getTime());
    }

    /**
     * 取得前一月的时间字符串
     *
     * @return
     */
    public static String getMonthAfter(String dateStr, int diff) throws Exception {
        return getMonthAfter(parseDate(dateStr), diff);
    }

    /**
     * 取得前一月的时间字符串
     *
     * @param date
     * @return
     */
    public static String getMonthAfter(Date date, int diff) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 0 + diff);
        return getDateTimeStr(cal.getTime());
    }
}
