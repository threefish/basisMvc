package com.sgaop.web.frame.server.core;


import com.sgaop.web.frame.server.cache.CacheManager;
import com.sgaop.web.frame.server.constant.Constant;
import com.sgaop.web.frame.server.mvc.ActionMethod;
import com.sgaop.web.frame.server.mvc.view.ViewsRegister;
import com.sgaop.web.frame.server.scanner.ClassScanner;
import com.sgaop.web.frame.server.scanner.ProperScanner;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/8 0008
 * To change this template use File | Settings | File Templates.
 */
@WebListener
public class ServletInitListener implements ServletContextListener {
    private static final Logger logger = Logger.getRootLogger();

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //加载全局配置文件
        ProperScanner.init();
        //扫描关键注解
        ClassScanner.ScannerAllClass();
        //注册默认视图
        ViewsRegister.RegisterDefaultView();
        //执行自定义启动类
        handlerSetup(Constant.WEB_SETUP_INIT, servletContextEvent);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //执行自定义销毁类
        handlerSetup(Constant.WEB_SETUP_DESTROY, servletContextEvent);
    }

    private void handlerSetup(String web_setup, ServletContextEvent servletContextEvent) {
        if (web_setup != null) {
            try {
                ActionMethod actionMethod = (ActionMethod) CacheManager.getUrlCache(web_setup);
                if (actionMethod != null) {
                    Class<?> actionClass = actionMethod.getActionClass();
                    Method handlerMethod = actionMethod.getActionMethod();
                    Object beanInstance = actionClass.newInstance();
                    handlerMethod.setAccessible(true);
                    handlerMethod.invoke(beanInstance, servletContextEvent);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
