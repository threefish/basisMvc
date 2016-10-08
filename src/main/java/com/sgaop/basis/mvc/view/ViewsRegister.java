package com.sgaop.basis.mvc.view;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/9/5 0005
 * To change this template use File | Settings | File Templates.
 * 视图注册器
 */
public class ViewsRegister {

    private static Map<String, Class<?>> regs = new HashMap();


    /**
     * 注册默认视图
     */
    public static void RegisterDefaultView() {

    }

    /**
     * @param regTypeStr 需要注册的类型
     * @param klass      执行类
     */
    public static void registerView(String regTypeStr, Class<?> klass) {
        regs.put(regTypeStr, klass);
    }


    /**
     * @param regTypeStr 检查是否注册了视图
     */
    public static boolean hasRegisterView(String regTypeStr) {
        if (regTypeStr.indexOf(":") > 0) {
            return regs.containsKey(splitView(regTypeStr)[0]);
        } else {
            new RuntimeException("返回自定义视图使用时必须 @OK(\"视图名称：视图值\") 结构");
        }
        return false;
    }

    /**
     * @param regTypeStr 返回视图执行类
     */
    public static Class<?> getViewClass(String regTypeStr) {
        return regs.get(splitView(regTypeStr)[0]);
    }


    private static String[] splitView(String regTypeStr) {
        return regTypeStr.split(":");
    }

}
