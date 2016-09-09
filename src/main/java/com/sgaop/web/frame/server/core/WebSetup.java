package com.sgaop.web.frame.server.core;

import javax.servlet.ServletContextEvent;


/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/16 0016
 * To change this template use File | Settings | File Templates.
 */
public interface WebSetup {

    void init(ServletContextEvent servletContextEvent);

    void destroy(ServletContextEvent servletContextEvent);
}
