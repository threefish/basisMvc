package com.sgaop.basis.aop;

import com.sgaop.basis.annotation.Aop;
import com.sgaop.basis.ioc.IocBeanContext;
import com.sgaop.basis.util.ClassTool;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


public class AbstractMethodInterceptor implements MethodInterceptor {

    public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("aop执行");
        Object result = proxy.invokeSuper(object, args);;
        return result;
    }

    /**
     * 在方法执行前拦截
     *
     * @param obj    被拦截的对象
     * @param method 被拦截的方法
     * @param args   被拦截的方法的参数
     * @return 如果继续往下走, 就返回true, 否则就退出AOP执行链
     */
    public boolean beforeInvoke(Object obj, Method method, Object... args) {
        System.out.println("拦截----------start---");
        return true;
    }


    /**
     * 在方法执行后拦截
     *
     * @param obj       被拦截的对象
     * @param returnObj 被拦截的方法的返回值的对象
     * @param method    被拦截的方法
     * @param args      被拦截方法的参数
     * @return 将会替代原方法返回值的值
     */
    public Object afterInvoke(Object obj, Object returnObj, Method method, Object... args) {
        System.out.println("拦截---------end----");
        return returnObj;
    }

    /**
     * 抛出Exception的时候拦截
     *
     * @param e      异常对象
     * @param obj    被拦截的对象
     * @param method 被拦截的方法
     * @param args   被拦截方法的返回值
     * @return 是否继续抛出异常
     */
    public boolean whenException(Exception e, Object obj, Method method, Object... args) {
        return true;
    }

    /**
     * 抛出Throwable的时候拦截
     *
     * @param e      异常对象
     * @param obj    被拦截的对象
     * @param method 被拦截的方法
     * @param args   被拦截方法的返回值
     * @return 是否继续抛出异常
     */
    public boolean whenError(Throwable e, Object obj, Method method, Object... args) {
        return true;
    }
}
