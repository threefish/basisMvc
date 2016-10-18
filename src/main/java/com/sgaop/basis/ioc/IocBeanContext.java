package com.sgaop.basis.ioc;


import com.google.gson.Gson;
import com.sgaop.basis.annotation.Aop;
import com.sgaop.basis.annotation.Aspect;
import com.sgaop.basis.annotation.Inject;
import com.sgaop.basis.aop.ProxyFactory;
import com.sgaop.basis.aop.proxy.Proxy;
import com.sgaop.basis.aop.proxy.ProxyMethodFilter;
import com.sgaop.basis.constant.Constant;
import com.sgaop.basis.util.ClassTool;
import com.sgaop.basis.util.StringsTool;
import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

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

    //全局AOP拦截器
    private Map<String, Class<? extends Annotation>> aopSet = new HashMap<>();

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
    public void setBean(String key, Object bean) {
        if (this.beans.containsKey(key)) {
            String str = String.format("IOC 中 %s 已经存在,不应该再次加入一个同名的bean", key);
            logger.error(str);
            throw new RuntimeException(str);
        } else {
            this.beans.put(key, bean);
        }
    }

    /**
     * 存放bean代理对象
     */
    public void setProxyBean(String key, Object bean) {
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
        this.injectBeans(classes);
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
            String beanName = ClassTool.getIocBeanName(item);
            if (beanName != null) {
                try {
                    this.setBean(beanName, item.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                /**
                 * 记录依赖关系
                 */
                Field[] fields = item.getDeclaredFields();
                for (Field field : fields) {
                    Inject fieldAnnotation = field.getAnnotation(Inject.class);
                    if (fieldAnnotation != null) {
                        //获取依赖的bean的名称,如果为null,则使用字段名称
                        String resourceName = fieldAnnotation.value();
                        if (StringsTool.isEmpty(resourceName)) {
                            resourceName = field.getName();
                        }
                        this.dependencies.put(beanName.concat(Constant.IOC_SEPARATOR).concat(field.getName()), resourceName);
                    }
                }

                /**
                 * 记录是全局AOP信息
                 */
                Aspect aspect = item.getAnnotation(Aspect.class);
                if (aspect != null) {
                    aopSet.put(beanName, aspect.annotation());
                }
            }
        }
    }

    /**
     * 扫描依赖关系并注入bean
     */
    private void injectBeans(Set<Class<?>> classes) {
        Iterator<Map.Entry<String, String>> iterator = dependencies.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> item = iterator.next();
            String key = item.getKey();
            String value = item.getValue();//依赖对象的值
            String[] split = key.split(Constant.IOC_SEPARATOR_REG);//数组第一个值表示bean对象名称,第二个值为字段属性名称
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
        /**
         * 注入AOP相关
         */
        Iterator<Class<?>> aopIterator = classes.iterator();
        while (aopIterator.hasNext()) {
            Class<?> item = aopIterator.next();
            String iocBeanName = ClassTool.getIocBeanName(item);
            if (iocBeanName != null) {
                Object beanInstance = this.getBean(iocBeanName);
                Method[] methods = item.getMethods();
                List<Proxy> proxys = new ArrayList<>();
                List<ProxyMethodFilter> proxyMethodFilters = new ArrayList<>();
                Set<String> allAop = new HashSet<>();
                /**
                 * 有全局AOP代理
                 */
                if (aopSet.size() != 0) {
                    for (Map.Entry entry : aopSet.entrySet()) {
                        String aopPorxyBeanName = (String) entry.getKey();
                        Class<? extends Annotation> ann = (Class<? extends Annotation>) entry.getValue();
                        //通过注解判断当前类是否需要代理
                        if (this.getAopBean(item, ann)) {  //需要代理
                            setProxy(aopPorxyBeanName, proxys, allAop);
                        }
                    }
                }
                /**
                 * 针对方法AOP代理
                 */
                for (Method method : methods) {
                    Aop aop = method.getAnnotation(Aop.class);
                    if (aop != null && aop.value().length > 0) {
                        Set<String> sets = new HashSet<>();
                        for (String aopPorxyBeanName : aop.value()) {
                            setProxy(aopPorxyBeanName, proxys, sets);
                        }
                        proxyMethodFilters.add(new ProxyMethodFilter(method.getName(), sets));
                    }
                }
                if (proxys.size() != 0) {
                    beanInstance = ProxyFactory.createProxyInstance(item, proxys, proxyMethodFilters, allAop);
                }
                Field[] fields = item.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Inject inject = field.getAnnotation(Inject.class);
                    if (iocBeanName != null && inject != null) {
                        String resName = inject.value().equals("") ? field.getName() : inject.value();
                        this.injectBean(field, beanInstance, resName);
                    }
                }
                this.setProxyBean(iocBeanName, beanInstance);
            }
        }
    }


    private void setProxy(String aopPorxyBeanName, List<Proxy> proxys, Set<String> proxyList) {
        //取得代理bean
        Proxy proxy = (Proxy) IocBeanContext.me().getBean(aopPorxyBeanName);
        //添加AOP代理
        if (!proxys.contains(aopPorxyBeanName)) {
            if (proxy != null) {
                //添加代理
                proxys.add(proxy);
                proxyList.add(aopPorxyBeanName);
            } else {
                String msg = String.format("IOC 中没有找到%s", aopPorxyBeanName);
                logger.error(msg);
                throw new RuntimeException(msg);
            }
        }
    }

    /**
     * 通过class判断是否需要aop代理
     *
     * @param item
     */
    private boolean getAopBean(Class<?> item, Class<? extends Annotation> ann) {
        if (item.getAnnotation(ann) == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * MVC扫描依赖关系并注入bean
     */
    public void injectBean(Field field, Object beanInstance, String beanKey) {
        try {
            field.set(beanInstance, beans.get(beanKey));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * MVC扫描依赖关系并注入bean
     */
    public void injectBean(Object beanInstance, String klass) {
        try {
            String[] split = klass.split("\\.");
            Object object = beans.get(split[0]);
            if (object != null) {
                Field field = object.getClass().getDeclaredField(split[1]);
                field.setAccessible(true);
                field.set(beanInstance, beans.get(split[1]));
            } else {
                logger.error("没有" + split[1] + "的实现");
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
