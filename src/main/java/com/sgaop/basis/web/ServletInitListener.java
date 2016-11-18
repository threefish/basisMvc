package com.sgaop.basis.web;


import com.google.gson.Gson;
import com.sgaop.basis.cache.MvcsManager;
import com.sgaop.basis.constant.Constant;
import com.sgaop.basis.dao.Dao;
import com.sgaop.basis.ioc.IocBeanContext;
import com.sgaop.basis.mvc.view.ViewsRegister;
import com.sgaop.basis.scanner.ClassScanner;
import com.sgaop.basis.scanner.PropertiesHelper;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/8 0008
 * To change this template use File | Settings | File Templates.
 */
public class ServletInitListener implements ServletContextListener {
    private static final Logger logger = Logger.getRootLogger();


    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //加载全局配置文件
        PropertiesHelper.init();
        //扫描关键注解
        ClassScanner.init();
        //注册默认视图
        ViewsRegister.RegisterDefaultView();
        //执行自定义启动类
        handlerInit(servletContextEvent);
        //初始化IOC
        IocBeanContext.me().init(ClassScanner.classes);
        logger.info("环境初始化成功");
        logger.debug("UrlMapping:" + new Gson().toJson(MvcsManager.urlMappingList()));
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //执行自定义销毁类
        handlerDestroy(servletContextEvent);
        logger.info("环境销毁成功");
    }

    private void handlerInit(ServletContextEvent servletContextEvent) {
        try {
            WebSetup setup = (WebSetup) MvcsManager.getSetupCache(Constant.WEB_SETUP);
            setup.init(servletContextEvent);
            MvcsManager.putSetupCache(Constant.WEB_SETUP,setup);
        } catch (Exception e) {
            logger.error("环境初始化时发生错误！", e);
            throw new RuntimeException("环境初始化时发生错误！", e);
        }
    }

    private void handlerDestroy(ServletContextEvent servletContextEvent) {
        try {
            WebSetup setup= (WebSetup)MvcsManager.getSetupCache(Constant.WEB_SETUP);
            setup.destroy(servletContextEvent);
        } catch (Exception e) {
            logger.error("环境销毁时发生错误！",e);
            throw new RuntimeException("环境销毁时发生错误！", e);
        }
    }
}
