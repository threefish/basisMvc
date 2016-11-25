package com.sgaop;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.gson.Gson;
import com.sgaop.basis.cache.PropertiesManager;
import com.sgaop.basis.dao.Condition;
import com.sgaop.basis.dao.DaosRegister;
import com.sgaop.basis.dao.Dao;
import com.sgaop.basis.dao.impl.DaoImpl;
import com.sgaop.basis.ioc.IocBeanContext;
import com.sgaop.basis.scanner.ClassHelper;
import com.sgaop.basis.scanner.PropertiesScans;
import com.sgaop.bean.tbUser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/23 0023
 * To change this template use File | Settings | File Templates.
 */
public class AllDao {

    private static Dao dao;

    @BeforeClass
    public static void setUp() throws SQLException {
        //加载全局配置文件
        PropertiesScans.init();
        ClassHelper.init();
        DruidDataSource dataSource = new DruidDataSource();
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
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(60 * 60 * 1000);
        Properties properties = new Properties();
        String[] strings = PropertiesManager.getCacheStr("db.connectionProperties").split("=");
        properties.setProperty(strings[0], strings[1]);
        dataSource.setConnectProperties(properties);
        try {
            dataSource.setFilters(PropertiesManager.getCacheStr("db.filters"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dao = DaosRegister.registerDao("dao", DaoImpl.class, dataSource);
        //初始化IOC
        IocBeanContext.me().init(ClassHelper.classes);
    }

    @Test
    public void insert() {
        tbUser tbUser2 = new tbUser();
        tbUser2.setCt(new Timestamp(new Date().getTime()));
        tbUser2.setIp("2222");
        tbUser2.setId(1);
        tbUser2.setLock(true);
        tbUser2.setUa("xxxx");
        tbUser2.setTkx("HC");
        tbUser2.setOs("web");
        dao.insert(tbUser2);


        tbUser tb = dao.fetch(tbUser.class, 1);
        System.out.println(tb.getTkx());
    }


    @Test
    public void select() {
        tbUser tb = dao.fetch(tbUser.class, 1);
        System.out.println(new Gson().toJson(tb));
        Condition condition = new Condition();
        condition.and("id", "=", 1);
        tbUser tb2 = dao.fetch(tbUser.class, condition);
        System.out.println(new Gson().toJson(tb2));
    }

    @Test
    public void selectList() {
        List<tbUser> tbUserList = dao.query(tbUser.class);
        System.out.println(new Gson().toJson(tbUserList));
        Condition condition = new Condition();
        condition.and("id", "=", 1);
        condition.or("id", "=", 28);
        List<tbUser> tbUserList2 = dao.query(tbUser.class,condition);
        System.out.println(new Gson().toJson(tbUserList2));
    }


    @Test
    public void del() {
        tbUser tbUser2 = new tbUser();
        tbUser2.setCt(new Timestamp(new Date().getTime()));
        tbUser2.setIp("2222");
        tbUser2.setId(1);
        tbUser2.setLock(true);
        tbUser2.setUa("xxxx");
        tbUser2.setTkx("HC");
        tbUser2.setOs("webXXX");
        dao.delete(tbUser2);
    }

    @Test
    public void delList() {
        tbUser tbUser2 = new tbUser();
        tbUser2.setCt(new Timestamp(new Date().getTime()));
        tbUser2.setIp("2222");
        tbUser2.setId(2);
        tbUser2.setLock(true);
        tbUser2.setUa("xxxx");
        tbUser2.setTkx("HC");
        tbUser2.setOs("webXXX");

        tbUser tbUser = new tbUser();
        tbUser.setCt(new Timestamp(new Date().getTime()));
        tbUser.setIp("2222");
        tbUser.setId(27);
        tbUser.setLock(true);
        tbUser.setUa("xxxx");
        tbUser.setTkx("12312sdf");
        tbUser.setOs("webXXX");
        List<tbUser> datalist = new ArrayList();
        datalist.add(tbUser);
        datalist.add(tbUser2);
        dao.delete(datalist);
    }


    @Test
    public void count() {
        System.out.println(dao.count(tbUser.class));
    }


    @Test
    public void update() {
        tbUser tbUser2 = new tbUser();
        tbUser2.setCt(new Timestamp(new Date().getTime()));
        tbUser2.setIp("2222123");
        tbUser2.setId(1);
        tbUser2.setLock(true);
        tbUser2.setUa("xxxx");
        tbUser2.setTkx("HCxx");
        tbUser2.setOs("webXXX");
        dao.update(tbUser2);
    }

    @Test
    public void updateCnd() {
        tbUser tbUser2 = new tbUser();
        tbUser2.setCt(new Timestamp(new Date().getTime()));
        tbUser2.setIp("2222123");
        tbUser2.setLock(true);
        tbUser2.setId(1);
        tbUser2.setUa("xxxx");
        tbUser2.setTkx("HCxx");
        tbUser2.setOs("webXXX");
        Condition cnd=new Condition();
        cnd.and("id","=",1);
        dao.update(tbUser2,cnd);
    }

    @Test
    public void updateList() {
        tbUser tbUser2 = new tbUser();
        tbUser2.setCt(new Timestamp(new Date().getTime()));
        tbUser2.setIp("1111");
        tbUser2.setId(1);
        tbUser2.setLock(true);
        tbUser2.setUa("xxxx");
        tbUser2.setTkx("HC");
        tbUser2.setOs("webHC");


        tbUser tbUser = new tbUser();
        tbUser.setCt(new Timestamp(new Date().getTime()));
        tbUser.setIp("2222");
        tbUser.setId(2);
        tbUser.setLock(true);
        tbUser.setUa("xxxx");
        tbUser.setTkx("HC");
        tbUser.setOs("webHC");
        List<tbUser> datalist = new ArrayList();
        datalist.add(tbUser);
        datalist.add(tbUser2);
        dao.update(datalist);
    }


    @Test
    public void insertList() {
        tbUser tbUser = new tbUser();
        tbUser.setCt(new Timestamp(new Date().getTime()));
        tbUser.setIp("1111");
        tbUser.setLock(true);
        tbUser.setUa("xxxx");
        tbUser.setTkx("12312sdf");
        tbUser.setOs("web");
        tbUser tbUser2 = new tbUser();
        tbUser2.setCt(new Timestamp(new Date().getTime()));
        tbUser2.setIp("2222");
        tbUser2.setLock(true);
        tbUser2.setUa("xxxx");
        tbUser2.setTkx("12312sdf");
        tbUser2.setOs("web");
        List<tbUser> datalist = new ArrayList();
        datalist.add(tbUser);
        datalist.add(tbUser2);
        dao.insert(datalist);
    }


}

