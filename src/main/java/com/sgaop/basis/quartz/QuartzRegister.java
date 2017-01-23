package com.sgaop.basis.quartz;

import com.sgaop.basis.ioc.IocBeanContext;
import com.sgaop.basis.util.ClassTool;
import com.sgaop.basis.log.Logs;
import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;


/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/15 0015
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("all")
public class QuartzRegister {

    private static final Logger log = Logs.get();

    /**
     * 注册定时任务管理器
     *
     * @param newIocBeanKey 放入ioc中需要一个IOC名称
     * @return Scheduler
     */
    public static <T> T registerScheduler(String newIocBeanKey) {
        Scheduler scheduler = null;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            BasisJobFactory basisJobFactory = new BasisJobFactory();
            IocBeanContext.me().setBean(ClassTool.getIocBeanName(BasisJobFactory.class), basisJobFactory);
            scheduler.setJobFactory(basisJobFactory);
            IocBeanContext.me().setBean(newIocBeanKey, scheduler);
        } catch (Exception e) {
            log.error("注册定时任务管理器发生错误!", e);
        }
        return (T) scheduler;
    }

}
