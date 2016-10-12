package com.sgaop.basis.aop.proxy;

import com.sgaop.basis.util.ClassTool;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/10/11 0011
 * To change this template use File | Settings | File Templates.
 */
public class ProxyChain {

    private final Class targetClass;
    private final Object targetObject;
    private final Method targetMethod;
    private final MethodProxy methodProxy;
    private final Object[] methodParams;
    private final List<ProxyMethodFilter> proxyMethodFilters;
    private List<Proxy> proxyList = new ArrayList<>();
    private Set<String> allAop;
    private int proxyIndex = 0;

    public ProxyChain(Class targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList, List<ProxyMethodFilter> proxyMethodFilters, Set<String> allAop) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
        this.proxyMethodFilters = proxyMethodFilters;
        this.allAop = allAop;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    /**
     * 执行代理链
     *
     * @return
     * @throws Throwable
     */
    public Object doProxyChain() throws Throwable {
        Object methodResult = null;
        Set<String> aops = getProxyMethodChain();
        boolean doRun = false;
        if (allAop.size() == 0 && aops.size() == 0 && proxyList.size() == 0) {
            doRun = true;
        } else {
            if (proxyIndex < proxyList.size()) {
                Proxy proxy = proxyList.get(proxyIndex++);
                String proxyBeanName = ClassTool.getIocBeanName(proxy.getClass());
                //首先判断是否是类切片
                if (allAop.contains(proxyBeanName)) {
                    methodResult = proxy.doProxy(this);
                    //判断是否是方法切片
                } else if (aops.contains(proxyBeanName)) {
                    methodResult = proxy.doProxy(this);
                } else {
                    methodResult = doProxyChain();
                }
            } else {
                doRun = true;

            }
        }
        if (doRun) {
            methodResult = invokeSuper();
        }
        return methodResult;
    }

    /**
     * 执行被拦截的业务
     *
     * @return
     * @throws Throwable
     */
    public Object invokeSuper() throws Throwable {
        return methodProxy.invokeSuper(targetObject, methodParams);
    }

    /**
     * 判断当前方法是否拥有aop
     *
     * @return
     */
    private Set<String> getProxyMethodChain() {
        for (ProxyMethodFilter proxyMethodFilter : proxyMethodFilters) {
            if (targetMethod.getName().equals(proxyMethodFilter.getMethodName())) {
                return proxyMethodFilter.getAopNames();
            }
        }
        return new HashSet<>();
    }


}