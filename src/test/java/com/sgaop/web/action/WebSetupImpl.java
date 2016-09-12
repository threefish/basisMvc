package com.sgaop.web.action;

import com.sgaop.web.action.view.FreeMarkerView;
import com.sgaop.web.frame.server.core.WebSetup;
import com.sgaop.web.frame.server.mvc.annotation.Setup;
import com.sgaop.web.frame.server.mvc.view.ViewsRegister;

import javax.servlet.ServletContextEvent;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/16 0016
 * To change this template use File | Settings | File Templates.
 */
@Setup
public class WebSetupImpl implements WebSetup {

    public void init(ServletContextEvent servletContextEvent) {
        //注册  freemarker 视图
        ViewsRegister.registerView("freemarker", FreeMarkerView.class);
    }

    public void destroy(ServletContextEvent servletContextEvent) {
        System.out.println("销毁了哦");
    }
}
