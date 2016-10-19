package com.sgaop.basis.aop.proxy;

import java.lang.annotation.Annotation;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/10/19 0019
 * To change this template use File | Settings | File Templates.
 */
public class ProxyClassFiter {

    /**
     * IOC
     */
    private String beanName;

    /**
     * 排序号，越小表示越先执行
     *
     * @return
     */
    private Integer no;

    /**
     * 按注解批量获取需要切片的类
     *
     * @return
     */
    Class<? extends Annotation> annotation;


    public ProxyClassFiter(String beanName, Integer no, Class<? extends Annotation> annotation) {
        this.beanName = beanName;
        this.no = no;
        this.annotation = annotation;
    }

    public String getBeanName() {
        return beanName;
    }

    public Integer getNo() {
        return no;
    }


    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

}
