package com.sgaop.basis.cache;


import com.sgaop.basis.mvc.ActionMethod;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/4 0005
 * To change this template use File | Settings | File Templates.
 */
public class MvcsManager {
    /**
     * MVC action相关类缓存
     */
    private volatile static HashMap<String, ActionMethod> getUrlClassMap = new HashMap();
    private volatile static HashMap<String, ActionMethod> postUrlClassMap = new HashMap();
    private volatile static HashMap<String, ActionMethod> headUrlClassMap = new HashMap();
    private volatile static HashMap<String, ActionMethod> deleteUrlClassMap = new HashMap();
    private volatile static HashMap<String, ActionMethod> putUrlClassMap = new HashMap();


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
    private MvcsManager() {
        super();
    }

    /**
     * 获取请求信息
     *
     * @param key
     * @return
     */
    public static ActionMethod getUrlCache(String key, String methodType) {
        ActionMethod actionMethod = null;
        switch (methodType) {
            case "POST":
                actionMethod = postUrlClassMap.get(key);
                break;
            case "GET":
                actionMethod = getUrlClassMap.get(key);
                break;
            case "DELETE":
                actionMethod = deleteUrlClassMap.get(key);
                break;
            case "HEAD":
                actionMethod = headUrlClassMap.get(key);
                break;
            case "PUT":
                actionMethod = putUrlClassMap.get(key);
                break;
        }
        return actionMethod;
    }

    /**
     * 设置请求信息
     *
     * @param key
     * @param method
     */
    public static void putUrlCache(String key, ActionMethod method) {
        switch (method.getMethod()) {
            case "POST":
                checkUrl(key, postUrlClassMap);
                postUrlClassMap.put(key, method);
                break;
            case "GET":
                checkUrl(key, getUrlClassMap);
                getUrlClassMap.put(key, method);
                break;
            case "DELETE":
                checkUrl(key, deleteUrlClassMap);
                deleteUrlClassMap.put(key, method);
                break;
            case "HEAD":
                checkUrl(key, headUrlClassMap);
                headUrlClassMap.put(key, method);
                break;
            case "PUT":
                checkUrl(key, putUrlClassMap);
                putUrlClassMap.put(key, method);
                break;
        }
    }

    private static void checkUrl(String pathKey, Map data) {
        if (data.containsKey(pathKey)) {
            throw new RuntimeException("URL地址已经存在"+ pathKey );
        }
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
        List<Map> list = new ArrayList();
        getMapList(list, getUrlClassMap.entrySet().iterator());
        getMapList(list, postUrlClassMap.entrySet().iterator());
        getMapList(list, headUrlClassMap.entrySet().iterator());
        getMapList(list, deleteUrlClassMap.entrySet().iterator());
        getMapList(list, putUrlClassMap.entrySet().iterator());
        return list;
    }

    private static void getMapList(List<java.util.Map> list, Iterator iterator) {
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
    }
} 
 