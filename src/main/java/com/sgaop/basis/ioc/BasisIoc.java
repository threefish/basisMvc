package com.sgaop.basis.ioc;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/9 0009
 * To change this template use File | Settings | File Templates.
 */
public class BasisIoc {

    private static IocBeanContext iocBeanContext = IocBeanContext.me();

    public static Object getBean(String beanKey) {
        return iocBeanContext.getBean(beanKey);
    }

}
