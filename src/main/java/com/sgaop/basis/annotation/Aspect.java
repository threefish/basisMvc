package com.sgaop.basis.annotation;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/10/12 0012
 * To change this template use File | Settings | File Templates.
 */

import java.lang.annotation.*;

/**
 * 将多个类关联到一个切片。
 * 必须是IOC容器中的
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Aspect {

    /**
     * 按注解批量获取需要切片的类
     *
     * @return
     */
    Class<? extends Annotation> annotation();

    /**
     * 排序号，越小表示越先执行
     *
     * @return
     */
    int No();


}
