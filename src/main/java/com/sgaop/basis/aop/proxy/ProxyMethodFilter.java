package com.sgaop.basis.aop.proxy;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/10/11 0011
 * To change this template use File | Settings | File Templates.
 */
public class ProxyMethodFilter {
    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 拥有的aop代理名称
     */
    private Set<String> aopNames;

    public ProxyMethodFilter(String methodName, Set<String> aopNames) {
        this.methodName = methodName;
        this.aopNames = aopNames;
    }

    public String getMethodName() {
        return methodName;
    }

    public Set<String> getAopNames() {
        return aopNames;
    }
}
