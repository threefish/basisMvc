package com.sgaop.basis.aop;

import com.sgaop.basis.aop.proxy.Proxy;
import com.sgaop.basis.aop.proxy.ProxyChain;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/10/11 0010
 * To change this template use File | Settings | File Templates.
 * AOP代理
 */
public abstract class InterceptorProxy implements Proxy {

    private static final Logger log = Logger.getRootLogger();


    /**
     * 执行代理链
     *
     * @param proxyChain 代理链
     * @return
     * @throws Throwable
     */
    @Override
    public final Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();
        try {
            before(cls, method, params);
            result = proxyChain.doProxyChain();
            after(cls, method, params, result);
        } catch (Throwable e) {
            exception(cls, method, params, e);
            throw e;
        } finally {
            finalize();
        }
        return result;
    }

    /**
     * 在业务执行以前执行
     *
     * @param cls
     * @param method
     * @param params
     * @throws Throwable
     */
    protected abstract void before(Class<?> cls, Method method, Object[] params) throws Throwable;

    /**
     * 在业务执行以后执行
     *
     * @param cls
     * @param method
     * @param params
     * @throws Throwable
     */
    protected abstract void after(Class<?> cls, Method method, Object[] params, Object result) throws Throwable;

    /**
     * 在业务执行出错时执行
     *
     * @param cls
     * @param method
     * @param params
     * @param e
     */
    protected abstract void exception(Class<?> cls, Method method, Object[] params, Throwable e);

    /**
     * 在业务最终完成后执行
     */
    protected abstract void finalize();
}