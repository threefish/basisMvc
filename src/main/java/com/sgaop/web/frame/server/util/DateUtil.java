package com.sgaop.web.frame.server.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String ENG_DATE_FROMAT = "EEE, d MMM yyyy HH:mm:ss z";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY = "yyyy";
    public static final String MM = "MM";
    public static final String DD = "dd";
    public static final String YYYY_MM_DD_HH_MM_ZH = "yyyy年MM月dd日  HH:mm";


    /**
     * 将timestamp转换成date
     *
     * @param tt
     * @return
     */
    public static Date timestampToDate(Timestamp tt) {
        return new Date(tt.getTime());
    }

    /**
     * 比较两个时间的大小
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean befor(Date a, Date b) {
        return a.before(b);
    }

    /**
     * @param date
     * @描述 —— 格式化日期对象
     */
    public static Date date2date(Date date, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        String str = sdf.format(date);
        try {
            date = sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    /**
     * @param date
     * @描述 —— 时间对象转换成字符串
     */
    public static String date2string(Date date, String formatStr) {
        String strDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        strDate = sdf.format(date);
        return strDate;
    }

    /**
     * @param timestamp
     * @描述 —— sql时间对象转换成字符串
     */
    public static String timestamp2string(Timestamp timestamp, String formatStr) {
        String strDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        strDate = sdf.format(timestamp);
        return strDate;
    }

    /**
     * @param dateString
     * @param formatStr
     * @描述 —— 字符串转换成时间对象
     */
    public static Date string2date(String dateString, String formatStr) {
        Date formateDate = null;
        DateFormat format = new SimpleDateFormat(formatStr);
        try {
            formateDate = format.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
        return formateDate;
    }


    /**
     * @param date
     * @描述 —— Date类型转换为Timestamp类型
     */
    public static Timestamp date2timestamp(Date date) {
        if (date == null)
            return null;
        return new Timestamp(date.getTime());
    }

    /**
     * 获取本月开始日期和今天结束日期
     *
     * @return
     */
    public static Date[] getNowMonth() {
        Date now = new Date();
        String date1 = getFullYear(now) + "-" + getMonth(now) + "-01 00:00:00";
        String date2 = getFullYear(now) + "-" + getMonth(now) + "-" + getDay(now) + " 23:59:59";
        Date[] dates = new Date[2];
        dates[0] = string2date(date1, YYYY_MM_DD_HH_MM_SS);
        dates[1] = string2date(date2, YYYY_MM_DD_HH_MM_SS);
        return dates;
    }

    /**
     * 获取近期num个月日期
     * num = -1;  是近三月
     *
     * @return
     */
    public static Date[] getNumMonth(int num) {
        Calendar clnow = Calendar.getInstance();
        Date now = new Date();
        String date1 = getFullYear(now) + "-" + (clnow.get(Calendar.MONTH) + num) + "-01 00:00:00";
        String date2 = getFullYear(now) + "-" + getMonth(now) + "-" + getDay(now) + " 23:59:59";
        Date[] dates = new Date[2];
        dates[0] = string2date(date1, YYYY_MM_DD_HH_MM_SS);
        dates[1] = string2date(date2, YYYY_MM_DD_HH_MM_SS);
        return dates;
    }


    /**
     * 获取上num个年度
     * num = -1;
     *
     * @return
     */
    public static Date[] getNumYear(int num) {
        Date now = new Date();
        String date1 = getFullYear(num) + "-01-01 00:00:00";
        String date2 = getFullYear(num) + "-12-31 23:59:59";
        Date[] dates = new Date[2];
        dates[0] = string2date(date1, YYYY_MM_DD_HH_MM_SS);
        dates[1] = string2date(date2, YYYY_MM_DD_HH_MM_SS);
        return dates;
    }

    /**
     * 获取上num个年度
     * num = -1;
     *
     * @return
     */
    public static Date getNumHour(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - num);
        return calendar.getTime();
    }

    /**
     * 年
     *
     * @param date
     * @return
     */
    public static String getFullYear(Date date) {
        return new SimpleDateFormat("yyyy").format(date);
    }

    /**
     * 取得当前年往前、往后的年
     *
     * @param num
     * @return
     */
    public static String getFullYear(int num) {
        Calendar now = Calendar.getInstance();
        return String.valueOf(now.get(Calendar.YEAR) + num);
    }


    /**
     * 月
     *
     * @param date
     * @return
     */
    public static String getMonth(Date date) {
        return new SimpleDateFormat("MM").format(date);
    }

    /**
     * 日
     *
     * @param date
     * @return
     */
    public static String getDay(Date date) {
        return new SimpleDateFormat("dd").format(date);
    }

    /**
     * 天
     *
     * @param date
     * @return
     */
    public static String getHour(Date date) {
        return new SimpleDateFormat("HH").format(date);
    }

    /**
     * 分
     *
     * @param date
     * @return
     */
    public static String getMinute(Date date) {
        return new SimpleDateFormat("mm").format(date);
    }

    /**
     * 秒
     *
     * @param date
     * @return
     */
    public static String getSecond(Date date) {
        return new SimpleDateFormat("ss").format(date);
    }

    /**
     * @param time
     * @描述 —— 指定时间距离当前时间的中文信息
     */
    public static String getLnow(long time) {
        Calendar cal = Calendar.getInstance();
        long timel = cal.getTimeInMillis() - time;
        if (timel / 1000 < 60) {
            return "1分钟以内";
        } else if (timel / 1000 / 60 < 60) {
            return timel / 1000 / 60 + "分钟前";
        } else if (timel / 1000 / 60 / 60 < 24) {
            return timel / 1000 / 60 / 60 + "小时前";
        } else {
            return timel / 1000 / 60 / 60 / 24 + "天前";
        }
    }


}
