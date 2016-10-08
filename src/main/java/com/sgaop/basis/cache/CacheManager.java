package com.sgaop.basis.cache;


import com.sgaop.basis.mvc.ActionMethod;

import java.util.*;

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
     * 系统执行关类缓存
     */
    private volatile static HashMap setupClassMap = new HashMap();

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
     * 载入系统执行类缓存
     *
     * @param key
     * @param obj
     */
    public static void putSetupCache(String key, Object obj) {
        setupClassMap.put(key, obj);
    }

    /**
     * 获得系统执行类缓存
     *
     * @param key
     * @return
     */
    public static Object getSetupCache(String key) {
        return setupClassMap.get(key);
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


    /**
     * urlMapping映射关系
     */
    public static List<Map> urlMappingList() {
        Iterator iterator = cacheUrlClassMap.entrySet().iterator();
        List<Map> list = new ArrayList();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String url = entry.getKey().toString();
            ActionMethod actionMethod = (ActionMethod) entry.getValue();
            HashMap map = new HashMap();
            map.put("url", url);
            map.put("HttpMethod", actionMethod.getMethod());
            map.put("ClassMethod", actionMethod.getActionMethod().getName());
            map.put("note", actionMethod.getNote());
            map.put("ok", actionMethod.getOK());
            map.put("class", actionMethod.getKlass());
            list.add(map);
        }
        return list;
    }
} 
 