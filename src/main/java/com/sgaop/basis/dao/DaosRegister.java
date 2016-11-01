package com.sgaop.basis.dao;

import com.sgaop.basis.dao.impl.DaoImpl;
import com.sgaop.basis.ioc.IocBeanContext;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/1 0001
 * To change this template use File | Settings | File Templates.
 */
public class DaosRegister {

    private static final Logger log = Logger.getRootLogger();

    /**
     * 注册数据源
     *
     * @param newIocBeanKey 将这个新数据源放入ioc中需要一个IOC名称
     * @param klass         数据源的执行类
     * @param dataSource    数据源
     */
    public static void registerDao(String newIocBeanKey, Class<?> klass, DataSource dataSource) {
        try {
            Dao dao = (Dao) klass.newInstance();
            dao.setDataSource(dataSource);
            IocBeanContext.me().setBean(newIocBeanKey, dao);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("注册数据源时发生错误!", e);
        }
    }
}
