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
    private List<Proxy> proxyList = new ArrayList<Proxy>();
    private int proxyIndex = 0;

    public ProxyChain(Class targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList, List<ProxyMethodFilter> proxyMethodFilters) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
        this.proxyMethodFilters = proxyMethodFilters;
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

    public Object doProxyChain() throws Throwable {
        Object methodResult;
        Set<String> aops = getProxyMethodChain();
        if (aops.size() == 0 || proxyList.size()==0) {
            methodResult = methodProxy.invokeSuper(targetObject, methodParams);
        } else {
            if (proxyIndex < proxyList.size()) {
                Proxy proxy = proxyList.get(proxyIndex++);
                if(aops.contains(ClassTool.getIocBeanName(proxy.getClass()))){
                    methodResult = proxy.doProxy(this);
                }else{
                    methodResult = doProxyChain();
                }
            } else {
                methodResult = methodProxy.invokeSuper(targetObject, methodParams);
            }
        }
        return methodResult;
    }


    private Set<String> getProxyMethodChain() {
        for (ProxyMethodFilter proxyMethodFilter : proxyMethodFilters) {
            if (targetMethod.getName().equals(proxyMethodFilter.getMethodName())) {
                return proxyMethodFilter.getAopNames();
            }
        }
        return new HashSet<>();
    }


}