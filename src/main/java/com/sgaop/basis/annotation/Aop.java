package com.sgaop.basis.annotation;

import java.lang.annotation.*;

/**
 * 将一个方法关联到一个或几个切片。
 * <p>
 * 或者说，让一组拦截器控制这个方法。
 * <p>
 * 这个注解接受一组值，每个值，就是一个容器内对象的名称，在 Ioc 容器中，<br>
 * 你可以任意声明这个对象，只要这个类继承了AopProxy 或者实现了Proxy 接口
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Aop {

    String[] value();

}
