package com.sgaop.basis.trans;

import com.sgaop.basis.aop.proxy.Proxy;
import com.sgaop.basis.aop.proxy.ProxyChain;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/1 0001
 * To change this template use File | Settings | File Templates.
 */
public class TransactionProxy implements Proxy {

    /**
     * 事务隔离级别
     */
    public int level;

    public TransactionProxy(int level) {
        this.level = level;
    }

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object re = null;
        try {
            Trans.begin(level);
            re = proxyChain.invokeSuper();
            Trans.commit();
        } catch (Throwable e) {
            Trans.rollback();
            throw e;
        } finally {
            Trans.close();
        }
        return re;
    }

}
