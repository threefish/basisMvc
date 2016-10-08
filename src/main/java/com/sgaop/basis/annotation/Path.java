package com.sgaop.basis.annotation;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/8 0008
 * To change this template use File | Settings | File Templates.
 */

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Path {
    /**
     * 需要映射的路径,可以多个
     */
    String[] value() default {};

    /**
     * 描述
     *
     * @return
     */
    String note() default "";
}
