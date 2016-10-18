package com.sgaop.basis.dao.factory;

import com.alibaba.druid.pool.DruidDataSource;
import com.sgaop.basis.cache.PropertiesManager;
import org.apache.log4j.Logger;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/4 0004
 * To change this template use File | Settings | File Templates.
 */
public class DataSourceFactory {

    private static final Logger logger = Logger.getRootLogger();


    private static DruidDataSource dataSource = new DruidDataSource();

    static {
        init();
    }

    public static DruidDataSource getDataSource() {
        return dataSource;
    }


    private static void init() {
        try {
            dataSource.setPassword(PropertiesManager.getCacheStr("db.password"));
            dataSource.setUsername(PropertiesManager.getCacheStr("db.user"));
            dataSource.setUrl(PropertiesManager.getCacheStr("db.jdbcUrl"));
            dataSource.setMaxActive(PropertiesManager.getIntCache("db.maxActive"));
            dataSource.setDriverClassName(PropertiesManager.getCacheStr("db.driverClassName"));
            dataSource.setValidationQuery(PropertiesManager.getCacheStr("db.validationQuery"));
            dataSource.setValidationQueryTimeout(PropertiesManager.getIntCache("db.validationQueryTimeout"));
            dataSource.setInitialSize(PropertiesManager.getIntCache("db.initialSize"));
            dataSource.setMinIdle(PropertiesManager.getIntCache("db.minIdle"));
            dataSource.setMaxWait(PropertiesManager.getIntCache("db.maxWait"));
            dataSource.setTimeBetweenEvictionRunsMillis(PropertiesManager.getIntCache("db.timeBetweenEvictionRunsMillis"));
            dataSource.setMinEvictableIdleTimeMillis(PropertiesManager.getIntCache("db.minEvictableIdleTimeMillis"));
            dataSource.setTestWhileIdle(PropertiesManager.getBooleanCache("db.testWhileIdle"));
            dataSource.setTestOnBorrow(PropertiesManager.getBooleanCache("db.testOnBorrow"));
            dataSource.setTestOnReturn(PropertiesManager.getBooleanCache("db.testOnReturn"));
            dataSource.setPoolPreparedStatements(PropertiesManager.getBooleanCache("db.poolPreparedStatements"));
            dataSource.init();
            logger.debug("数据库连接成功");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.debug("数据库连接失败", e);
        }
    }
}
