package com.sgaop.basis.quartz;

import com.sgaop.basis.annotation.IocBean;
import com.sgaop.basis.ioc.Ioc;
import com.sgaop.basis.util.ClassTool;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;


/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/15 0015
 * To change this template use File | Settings | File Templates.
 */
//@IocBean
public class BasisJobFactory implements JobFactory {

    private static final Logger log = Logger.getRootLogger();

    protected SimpleJobFactory simple = new SimpleJobFactory();

    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        try {
            return Ioc.getBean(ClassTool.getIocBeanName(bundle.getJobDetail().getJobClass()));
        } catch (Exception e) {
            log.warn("Not ioc bean? fallback to SimpleJobFactory", e);
            return simple.newJob(bundle, scheduler);
        }
    }

}
