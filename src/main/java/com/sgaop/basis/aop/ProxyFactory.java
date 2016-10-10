package com.sgaop.basis.aop;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/10/10 0010
 * To change this template use File | Settings | File Templates.
 */
public class ProxyFactory {

    public static Object createProxyInstance(Class superclass, List<Callback> callbacks) {
        Enhancer enhancer = new Enhancer();
        enhancer.setUseCache(true);
        enhancer.setCallbackFilter(new ProxyCallbackFilter());
        enhancer.setSuperclass(superclass);
        enhancer.setCallbacks(callbacks.toArray(new Callback[0]));
        return enhancer.create();
    }
}
