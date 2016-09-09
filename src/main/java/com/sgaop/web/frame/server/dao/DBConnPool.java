package com.sgaop.web.frame.server.dao;

import com.alibaba.druid.pool.DruidDataSource;
import com.sgaop.web.frame.server.cache.StaticCacheManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/4 0004
 * To change this template use File | Settings | File Templates.
 */
public class DBConnPool {

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
            dataSource.setPassword(StaticCacheManager.getCacheStr("db.password"));
            dataSource.setUsername(StaticCacheManager.getCacheStr("db.user"));
            dataSource.setUrl(StaticCacheManager.getCacheStr("db.jdbcUrl"));
            dataSource.setMaxActive(StaticCacheManager.getIntCache("db.maxActive"));
            dataSource.setDriverClassName(StaticCacheManager.getCacheStr("db.driverClassName"));
            dataSource.setValidationQuery(StaticCacheManager.getCacheStr("db.validationQuery"));
            dataSource.setValidationQueryTimeout(StaticCacheManager.getIntCache("db.validationQueryTimeout"));
            dataSource.setInitialSize(StaticCacheManager.getIntCache("db.initialSize"));
            dataSource.setMinIdle(StaticCacheManager.getIntCache("db.minIdle"));
            dataSource.setMaxWait(StaticCacheManager.getIntCache("db.maxWait"));
            dataSource.setTimeBetweenEvictionRunsMillis(StaticCacheManager.getIntCache("db.timeBetweenEvictionRunsMillis"));
            dataSource.setMinEvictableIdleTimeMillis(StaticCacheManager.getIntCache("db.minEvictableIdleTimeMillis"));
            dataSource.setTestWhileIdle(StaticCacheManager.getBooleanCache("db.testWhileIdle"));
            dataSource.setTestOnBorrow(StaticCacheManager.getBooleanCache("db.testOnBorrow"));
            dataSource.setTestOnReturn(StaticCacheManager.getBooleanCache("db.testOnReturn"));
            dataSource.setPoolPreparedStatements(StaticCacheManager.getBooleanCache("db.poolPreparedStatements"));
            dataSource.init();
            logger.debug("数据库连接成功");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.debug("数据库连接失败", e);
        }
    }
}
