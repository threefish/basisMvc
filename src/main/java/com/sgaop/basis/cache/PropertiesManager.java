package com.sgaop.basis.cache;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/4 0015
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesManager {

    private volatile static HashMap propertiesMap = new HashMap();

    /**
     * 单实例构造方法
     */
    private PropertiesManager() {
        super();
    }

    /**
     * 得到缓存。同步静态方法
     *
     * @param key
     * @return
     */
    public synchronized static Object getCache(String key) {
        return propertiesMap.get(key);
    }

    /**
     * 得到缓存
     *
     * @param key
     * @return
     */
    public static String getCacheStr(String key) {
        return propertiesMap.get(key).toString();
    }

    /**
     * 得到缓存
     *
     * @param key
     * @return
     */
    public static long getLongCache(String key) {
        return Long.parseLong(propertiesMap.get(key).toString());
    }


    /**
     * 得到缓存
     *
     * @param key
     * @return
     */
    public static int getIntCache(String key) {
        return Integer.parseInt(propertiesMap.get(key).toString());
    }

    /**
     * 得到缓存
     *
     * @param key
     * @return
     */
    public static boolean getBooleanCache(String key) {
        return Boolean.parseBoolean(propertiesMap.get(key).toString());
    }

    /**
     * 判断是否存在一个缓存
     *
     * @param key
     * @return
     */
    public synchronized static boolean hasCache(String key) {
        return propertiesMap.containsKey(key);
    }

    /**
     * 清除所有缓存
     */
    public synchronized static void clearAll() {
        propertiesMap.clear();
    }

    /**
     * 清除指定的缓存
     *
     * @param key
     */
    public synchronized static void clearOnly(String key) {
        propertiesMap.remove(key);
    }

    /**
     * 载入缓存
     *
     * @param key
     * @param obj
     */
    public synchronized static void putCache(String key, Object obj) {
        propertiesMap.put(key, obj);
    }

    /**
     * 通过配置文件载入缓存
     *
     * @param properties
     */
    public synchronized static void putCache(Properties properties) {
        Enumeration enu = properties.propertyNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            putCache(key, properties.get(key));
        }
    }
} 
 