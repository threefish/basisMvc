package com.sgaop.web.frame.server.ioc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * @author malongbo
 * @date 2014/12/29
 */
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
    
    String value() default "";
    
}
