package com.sgaop.web.frame.server.dao.annotation;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/6/24 0024
 * To change this template use File | Settings | File Templates.
 */

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Pk {
    /**
     * 字段名称
     *
     * @return
     */
    String value() default "";
}
