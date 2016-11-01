package com.sgaop.basis.aop.proxy;

import com.sgaop.basis.trans.TransAop;
import com.sgaop.basis.trans.TransactionProxy;
import com.sgaop.basis.util.ClassTool;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/10/11 0011
 * To change this template use File | Settings | File Templates.
 * 代理链
 */
public class ProxyChain {
    private final Class targetClass;
    private final Object targetObject;
    private final Method targetMethod;
    private final MethodProxy methodProxy;
    private final Object[] methodParams;
    //存放方法拦截器信息
    private final List<ProxyMethodFilter> proxyMethodFilters;
    //存放类拦截器信息
    private Set<String> allAop;
    private List<Proxy> proxyList = new ArrayList<>();

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
                String proxyBeanName = "";
                //事务切片
                if (proxy instanceof TransactionProxy) {
                    TransactionProxy transactionProxy = (TransactionProxy) proxy;
                    switch (transactionProxy.level) {
                        case Connection.TRANSACTION_NONE:
                            proxyBeanName = TransAop.NONE;
                            break;
                        case Connection.TRANSACTION_READ_COMMITTED:
                            proxyBeanName = TransAop.READ_COMMITTED;
                            break;
                        case Connection.TRANSACTION_READ_UNCOMMITTED:
                            proxyBeanName = TransAop.READ_UNCOMMITTED;
                            break;
                        case Connection.TRANSACTION_REPEATABLE_READ:
                            proxyBeanName = TransAop.REPEATABLE_READ;
                            break;
                        case Connection.TRANSACTION_SERIALIZABLE:
                            proxyBeanName = TransAop.SERIALIZABLE;
                            break;
                    }
                } else {
                    proxyBeanName = ClassTool.getIocBeanName(proxy.getClass());
                }
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