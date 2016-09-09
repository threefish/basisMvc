package com.sgaop.web.frame.server.cache;


import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/4 0005
 * To change this template use File | Settings | File Templates.
 */
public class CacheManager {
    /**
     * MVC action相关类缓存
     */
    private volatile static HashMap cacheUrlClassMap = new HashMap();

    /**
     * DAO table相关类缓存
     */
    private volatile static HashMap cacheTableClassMap = new HashMap();

    /**
     * 单实例构造方法
     */
    private CacheManager() {
        super();
    }

    /**
     * 得到缓存
     *
     * @param key
     * @return
     */
    public static Object getUrlCache(String key) {
        return cacheUrlClassMap.get(key);
    }

    /**
     * 载入缓存
     *
     * @param key
     * @param obj
     */
    public static void putUrlCache(String key, Object obj) {
        cacheUrlClassMap.put(key, obj);
    }


    /**
     * 得到缓存
     *
     * @param key
     * @return
     */
    public static Object getTableCache(String key) {
        return cacheTableClassMap.get(key);
    }

    /**
     * 载入缓存
     *
     * @param key
     * @param obj
     */
    public static void putTableCache(String key, Object obj) {
        cacheTableClassMap.put(key, obj);
    }
} 
 