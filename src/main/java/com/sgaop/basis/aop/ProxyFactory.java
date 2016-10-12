package com.sgaop.basis.aop;

import com.sgaop.basis.aop.Interceptor.ProxyMethodInterceptor;
import com.sgaop.basis.aop.proxy.Proxy;
import com.sgaop.basis.aop.proxy.ProxyMethodFilter;
import net.sf.cglib.proxy.Enhancer;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/10/10 0010
 * To change this template use File | Settings | File Templates.
 * 代理工厂
 */
public class ProxyFactory {

    public static Object createProxyInstance(Class superclass, List<Proxy> proxys, List<ProxyMethodFilter> proxyMethodFilters, Set<String> allAop) {
        Enhancer enhancer = new Enhancer();
        enhancer.setUseCache(true);
        enhancer.setSuperclass(superclass);
        enhancer.setCallback(new ProxyMethodInterceptor(superclass, proxys, proxyMethodFilters, allAop));
        return enhancer.create();
    }

}
