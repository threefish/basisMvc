package com.sgaop.basis.util;

import com.sgaop.basis.annotation.IocBean;
import com.sgaop.basis.mvc.upload.TempFile;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/13 0013
 * To change this template use File | Settings | File Templates.
 */
public class ClassTool {

    private static final Logger log = Logger.getLogger(ClassTool.class);


    /**
     * 表单参数转换
     *
     * @param klazz
     * @param value
     * @return
     * @throws ParseException
     */
    public static Object ParamCast(Class<?> klazz, Object value) throws ParseException {
        Object val = null;
        if (value == null) {
            if (klazz.equals(String.class)) {
                val = "";
            } else if (klazz.equals(int.class) || klazz.equals(Integer.class) || klazz.equals(Long.class) || klazz.equals(long.class) || klazz.equals(double.class) || klazz.equals(Double.class) || klazz.equals(float.class) || klazz.equals(Float.class)) {
                val = 0;
            } else if (klazz.equals(String[].class)) {
                val = new String[]{};
            } else if (klazz.equals(boolean.class) || klazz.equals(Boolean.class)) {
                val = false;
            }
        } else if (value instanceof Object[]) {
            if (klazz.equals(String.class)) {
                val = ((Object[]) value)[0];
            } else if (klazz.equals(String[].class)) {
                val = String.valueOf(((Object[]) value)[0]).split(",");
            } else if (klazz.equals(int[].class)) {
                val = StringsTool.stringsToints(String.valueOf(((Object[]) value)[0]).split(","));
            } else if (klazz.equals(int.class)) {
                val = Integer.valueOf(String.valueOf(((Object[]) value)[0]));
            } else if (klazz.equals(double.class)) {
                val = Double.valueOf(String.valueOf(((Object[]) value)[0]));
            } else if (klazz.equals(long.class)) {
                val = Long.valueOf(String.valueOf(((Object[]) value)[0]));
            } else if (klazz.equals(float.class)) {
                val = Float.valueOf(String.valueOf(((Object[]) value)[0]));
            } else if (klazz.equals(boolean.class)) {
                String valStr = String.valueOf(((Object[]) value)[0]);
                if ("1".equals(valStr)) {
                    val = true;
                } else if ("0".equals(valStr)) {
                    val = false;
                } else {
                    val = Boolean.valueOf(valStr);
                }
            } else if (klazz.equals(Date.class)) {
                val = DateTool.parseDate(String.valueOf(((Object[]) value)[0]));
            } else if (klazz.equals(java.sql.Date.class)) {
                val = DateTool.parseSqlDate(String.valueOf(((Object[]) value)[0]));
            } else if (klazz.equals(Timestamp.class)) {
                val = new Timestamp(DateTool.parseDate(String.valueOf(((Object[]) value)[0])).getTime());
            } else if (klazz.equals(TempFile.class)) {
                val = ((Object[]) value)[0];
            } else if (klazz.equals(TempFile[].class)) {
                val = value;
            } else {
                throw new RuntimeException("没有识别到的类型[" + klazz.getName() + "]");
            }
        } else {
            if (klazz.equals(String.class)) {
                val = ((String[]) value)[0];
            } else if (klazz.equals(String[].class)) {
                val = value;
            } else if (klazz.equals(int[].class)) {
                val = value;
            } else if (klazz.equals(int.class)) {
                val = Integer.valueOf(((String[]) value)[0]);
            } else if (klazz.equals(double.class)) {
                val = Double.valueOf(((String[]) value)[0]);
            } else if (klazz.equals(long.class)) {
                val = Long.valueOf(((String[]) value)[0]);
            } else if (klazz.equals(float.class)) {
                val = Float.valueOf(((String[]) value)[0]);
            } else if (klazz.equals(boolean.class)) {
                val = Boolean.valueOf(((String[]) value)[0]);
            } else if (klazz.equals(Date.class)) {
                val = DateTool.parseDate(((String[]) value)[0]);
            } else if (klazz.equals(java.sql.Date.class)) {
                val = DateTool.parseSqlDate(((String[]) value)[0]);
            } else if (klazz.equals(Timestamp.class)) {
                val = new Timestamp(DateTool.parseDate(((String[]) value)[0]).getTime());
            } else {
                throw new RuntimeException("没有识别到的类型[" + klazz.getName() + "]");
            }
        }
        return val;
    }


    /**
     * 取得setter的方法名
     *
     * @param methodName
     * @return
     */
    public static String setMethodName(String methodName, Class clss) {
        return "set" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
    }

    /**
     * 取得getter的方法名
     *
     * @param methodName
     * @return
     */
    public static String getMethodName(String methodName, Class clss) {
        if (clss == boolean.class) {
            return "is" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        } else {
            return "get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        }
    }


    /**
     * 执行一个方法
     *
     * @param field
     * @param clss
     * @param bean
     * @param value
     */
    public static void invokeMethod(Field field, String methodName, Class clss, Object bean, Object value) {
        if (value == null) {
            return;
        }
        try {
            Method method = clss.getMethod(methodName, field.getType());
            method.setAccessible(true);
            method.invoke(bean, value);
        } catch (Exception e) {
            log.debug(e);
        }
    }

    /**
     * 执行一个方法,取得值
     *
     * @param clss
     * @param pojo
     * @param methodName
     * @return
     */
    public static Object invokeGetMethod(Class clss, Object pojo, String methodName) {
        Object value = null;
        try {
            Method method = clss.getMethod(methodName);
            method.setAccessible(true);
            value = method.invoke(pojo);
        } catch (Exception e) {
            log.debug(e);
        }
        return value;
    }

    /**
     * 获取IocBean的名称
     * 第一个字母会变成小写，其他不变
     *
     * @param klass
     * @return
     */
    public static String getIocBeanName(Class<?> klass) {
        IocBean iocBean = klass.getAnnotation(IocBean.class);
        if (iocBean != null) {
            if (iocBean.value().equals("")) {
                String beanKey = klass.getSimpleName();
                return beanKey.substring(0, 1).toLowerCase() + beanKey.substring(1, beanKey.length());
            } else {
                return iocBean.value();
            }
        }
        return null;
    }

    /**
     * 类参数转换
     */
    public static Object coverParam(Class<?> field, int counmType, Object value) {
        switch (counmType) {
            case Types.BIGINT:
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.TINYINT:
            case Types.NUMERIC:
            case Types.DATE:
            case Types.TIMESTAMP:
                value = coverParam(field, value);
                break;
        }
        return value;
    }

    /**
     * 类参数转换
     */
    public static Object coverParam(Class<?> klazz, Object value) {
        Object val = null;
        try {
            if (klazz.equals(String.class)) {
                val = String.valueOf(value);
            } else if (klazz.equals(String[].class)) {
                val = (String[]) value;
            } else if (klazz.equals(int[].class)) {
                val = (int[]) value;
            } else if (klazz.equals(int.class)) {
                val = Integer.parseInt(String.valueOf(value));
            } else if (klazz.equals(double.class)) {
                val = Double.parseDouble(String.valueOf(value));
            } else if (klazz.equals(long.class)) {
                val = Long.parseLong(String.valueOf(value));
            } else if (klazz.equals(float.class)) {
                val = Float.parseFloat(String.valueOf(value));
            } else if (klazz.equals(boolean.class)) {
                val = Boolean.parseBoolean(String.valueOf(value));
            } else if (klazz.equals(Date.class)) {
                val = DateUtil.string2date(String.valueOf(value), DateUtil.YYYY_MM_DD_HH_MM_SS);
            } else if (klazz.equals(java.sql.Date.class)) {
                val = DateUtil.string2javaDate(String.valueOf(value), DateUtil.YYYY_MM_DD_HH_MM_SS);
            } else if (klazz.equals(Timestamp.class)) {
                val = new Timestamp(DateUtil.string2date(String.valueOf(value), DateUtil.YYYY_MM_DD_HH_MM_SS).getTime());
            } else {
                throw new RuntimeException("没有识别到的类型[" + klazz.getName() + "]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    /**
     * 实例化类
     *
     * @param cls
     * @return
     */
    public static Object getInstance(Class cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            log.error("请检查"+ cls+ "是否含有无参构造函数");
        }
        return null;
    }
}
