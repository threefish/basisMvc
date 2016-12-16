package com.sgaop.basis.i18n;

import com.sgaop.basis.cache.PropertiesManager;
import com.sgaop.basis.constant.Constant;
import com.sgaop.basis.mvc.Mvcs;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/12/15 0015
 * To change this template use File | Settings | File Templates.
 */
public class LanguageManager {

    private volatile static HashMap<String, HashMap<String, String>> languageMap = new HashMap();

    private volatile static HashMap<String, Path> pathMap = new HashMap();

    /**
     * @param i18n  语言编码
     * @param key
     * @param value
     */
    private synchronized static void add(Path path, String i18n, String key, String value) {
        HashMap<String, String> data = languageMap.get(i18n);
        if (data == null) {
            data = new HashMap<>();
            pathMap.put(i18n, path);
        }
        data.put(key, value);
        languageMap.put(i18n, data);
    }


    /**
     * @param i18n 语言编码
     * @param key
     */
    public static String get(String i18n, String key) {
        String value = languageMap.get(i18n).get(key);
        return value == null ? "" : value;
    }

    /**
     * @param key
     */
    public static String getVal(String key) {
        String value = languageMap.get(Mvcs.getI18nLang()).get(key);
        return value == null ? "" : value;
    }

    /**
     * @param i18n 语言编码
     */
    public static HashMap<String, String> get(String i18n) {
        HashMap<String, String> value = new HashMap<>();
        //是否开发模式
        if (PropertiesManager.getBooleanCache("isDevelop")) {
            try (InputStreamReader in = new InputStreamReader(new FileInputStream(pathMap.get(i18n).toFile()), Constant.utf8)) {
                Properties props = new Properties();
                props.load(in);
                Enumeration enu = props.propertyNames();
                while (enu.hasMoreElements()) {
                    String key = (String) enu.nextElement();
                    value.put(key, (String) props.get(key));
                }
            } catch (Exception e) {
                e.getMessage();
            }
        } else {
            value = languageMap.get(i18n);
        }
        return value == null ? new HashMap<>() : value;
    }

    /**
     * 载入
     *
     * @param i18n       语言编码
     * @param properties
     */
    public synchronized static void add(Path path, String i18n, Properties properties) {
        Enumeration enu = properties.propertyNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            add(path, i18n, key, (String) properties.get(key));
        }
    }

}
