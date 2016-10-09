package com.sgaop.basis.ioc;


import com.google.gson.Gson;
import com.sgaop.basis.annotation.Inject;
import com.sgaop.basis.annotation.IocBean;
import com.sgaop.basis.util.StringsTool;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/10/8 0008
 * To change this template use File | Settings | File Templates.
 */
public class IocBeanContext {

    private static final Logger logger = Logger.getRootLogger();
    private static IocBeanContext me = new IocBeanContext();
    //存放bean
    private Map<String, Object> beans = new HashMap<String, Object>();
    //记录依赖关系
    private Map<String, String> dependencies = new HashMap<String, String>();

    /**
     * 获取实体对象
     *
     * @return IocBeanContext Instance
     */
    public static IocBeanContext me() {

        return me;
    }

    /**
     * 获取bean对象
     *
     * @param name beanName
     * @return
     */
    public Object getBean(String name) {
        return beans.get(name);
    }

    /**
     * 存放bean对象
     */
    public void setBean(String key,Object bean) {
        this.beans.put(key, bean);
    }

    /**
     * 初始化各对象及依赖
     *
     * @param classes 扫描到的类集合
     */
    public void init(Set<Class<?>> classes) {
        if (classes == null || classes.size() == 0) {
            return;
        }
        //创建bean,扫描依赖关系
        this.createBeansAndScanDependencies(classes);
        //注入依赖
        this.injectBeans();
        logger.debug("IocMapping:" + new Gson().toJson(dependencies));
    }

    /**
     * 扫描注解,创建对象,记录依赖关系
     *
     * @param classes 类集合
     */
    private void createBeansAndScanDependencies(Set<Class<?>> classes) {
        Iterator<Class<?>> iterator = classes.iterator();
        while (iterator.hasNext()) {
            Class<?> item = iterator.next();
            IocBean annotation = item.getAnnotation(IocBean.class);
            if (annotation != null) {
                String beanName = annotation.value();
                try {
                    this.setBean(annotation.value(), item.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                /*
                记录依赖关系
                 */
                Field[] fields = item.getDeclaredFields();

                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    Inject fieldAnnotation = field.getAnnotation(Inject.class);
                    if (fieldAnnotation != null) {
                        //获取依赖的bean的名称,如果为null,则使用字段名称
                        String resourceName = fieldAnnotation.value();
                        if (StringsTool.isEmpty(resourceName)) {
                            resourceName = field.getName();
                        }
                        this.dependencies.put(beanName.concat(".").concat(field.getName()), resourceName);
                    }

                }
            }
        }
    }

    /**
     * 扫描依赖关系并注入bean
     */
    private void injectBeans() {
        Iterator<Map.Entry<String, String>> iterator = dependencies.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> item = iterator.next();
            String key = item.getKey();
            String value = item.getValue();//依赖对象的值
            String[] split = key.split("\\.");//数组第一个值表示bean对象名称,第二个值为字段属性名称
            try {
//                PropertyUtils.setProperty(beans.get(split[0]), split[1], beans.get(value));
                Object object = beans.get(split[0]);
                Field field = object.getClass().getDeclaredField(split[1]);
                field.setAccessible(true);
                field.set(object, beans.get(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * MVC扫描依赖关系并注入bean
     */
    public void injectBean(Object beanInstance, String klass) {
        try {
            String[] split = klass.split("\\.");
            Object object = beans.get(split[0]);
            if(object!=null){
                Field field = object.getClass().getDeclaredField(split[1]);
                field.setAccessible(true);
                field.set(beanInstance, beans.get(split[1]));
            } else{
                logger.error("没有"+ split[1]+"的实现");
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
