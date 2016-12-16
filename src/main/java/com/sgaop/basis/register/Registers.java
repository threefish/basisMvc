package com.sgaop.basis.register;

import com.sgaop.basis.dao.Dao;
import com.sgaop.basis.dao.DaosRegister;
import com.sgaop.basis.mvc.view.ViewsRegister;
import com.sgaop.basis.quartz.QuartzRegister;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/12/14 0014
 * To change this template use File | Settings | File Templates.
 */
public class Registers {

    /**
     * 注册视图
     *
     * @param regTypeStr 注册类型
     * @param Viewklass  视图
     */
    public static void view(String regTypeStr, Class<?> Viewklass) {
        ViewsRegister.registerView(regTypeStr, Viewklass);
    }

    /**
     * 注册dao
     *
     * @param newIocBeanKey
     * @param klass
     * @param dataSource
     * @return
     */
    public static Dao dao(String newIocBeanKey, Class<?> klass, DataSource dataSource) {
        return DaosRegister.registerDao(newIocBeanKey, klass, dataSource);
    }

    /**
     * 注册任务
     *
     * @param newIocBeanKey
     * @param <T>
     * @return
     */
    public static <T> T Scheduler(String newIocBeanKey) {
        return QuartzRegister.registerScheduler(newIocBeanKey);
    }


}
