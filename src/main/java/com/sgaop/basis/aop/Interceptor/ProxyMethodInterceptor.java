package com.sgaop.basis.aop.Interceptor;

import com.sgaop.basis.aop.proxy.Proxy;
import com.sgaop.basis.aop.proxy.ProxyChain;
import com.sgaop.basis.aop.proxy.ProxyMethodFilter;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/10/11 0011
 * To change this template use File | Settings | File Templates.
 */
public class ProxyMethodInterceptor implements MethodInterceptor {

    private Class superclass;

    private List<Proxy> proxys;

    private List<ProxyMethodFilter> proxyMethodFilters;

    public ProxyMethodInterceptor(Class superclass, List<Proxy> proxys, List<ProxyMethodFilter> proxyMethodFilters) {
        this.superclass = superclass;
        this.proxys = proxys;
        this.proxyMethodFilters = proxyMethodFilters;
    }

    /**
     * obj 代理对象实例
     * method 源对象的方法名
     * args 传递给方法的实际入参
     * proxyMethod 与源对象中的method相对应的代理对象中的方法
     */
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return new ProxyChain(superclass, obj, method, proxy, args, proxys, proxyMethodFilters).doProxyChain();
    }
}
