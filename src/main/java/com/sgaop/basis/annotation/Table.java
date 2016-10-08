package com.sgaop.basis.annotation;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/6/24 0024
 * To change this template use File | Settings | File Templates.
 */

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Table {
    /**
     * 表名
     *
     * @return
     */
    String value() default "";
}
