package com.sgaop.basis.dao.impl;

import com.sgaop.basis.cache.MvcsManager;
import com.sgaop.basis.dao.Dao;
import com.sgaop.basis.dao.DbType;
import com.sgaop.basis.dao.Pager;
import com.sgaop.basis.dao.bean.TableInfo;
import com.sgaop.basis.dao.entity.Record;
import com.sgaop.basis.trans.BasisTransaction;
import com.sgaop.basis.trans.Trans;
import com.sgaop.basis.util.DBUtil;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/6/20 0020
 * To change this template use File | Settings | File Templates.
 */
public class DaoImpl implements Dao {

    private DataSource dataSource;

    private Connection connection;

    public DbType dbtype;

    private static final Logger log = Logger.getRootLogger();


    @Override
    public void setDataSource(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        this.dbtype = DBUtil.getDataBaseType(dataSource.getConnection());
    }

    public Connection getConnection() throws SQLException {
        Connection connection = null;
        if (!Trans.isTransactionNone()) {
            BasisTransaction tn = (BasisTransaction) Trans.get();
            connection = tn.getConnection(this.dataSource);
        } else {
            if (this.connection == null || this.connection.isClosed()) {
                connection = this.connection = dataSource.getConnection();
            } else {
                connection = this.connection;
            }
        }
        return connection;
    }

    @Override
    public int insert(Object bean) throws Exception {
        Class cls = bean.getClass();
        TableInfo daoMethod = (TableInfo) MvcsManager.getTableCache(cls.getName());
        ArrayList<Object> list = new ArrayList<>();
        list.add(bean);
        return JdbcAccessor.doInsert(getConnection(), cls, daoMethod, list)[0];
    }

    /**
     * 批量插入
     *
     * @param cls
     * @param list
     * @return
     */
    public int[] insert(Class cls, ArrayList<Object> list) throws Exception {
        TableInfo daoMethod = (TableInfo) MvcsManager.getTableCache(cls.getName());
        return JdbcAccessor.doInsert(getConnection(), cls, daoMethod, list);
    }


    /**
     * 更新一个对象
     *
     * @param bean
     * @return
     */
    public boolean update(Object bean) throws Exception {
        Class cls = bean.getClass();
        TableInfo daoMethod = (TableInfo) MvcsManager.getTableCache(cls.getName());
        ArrayList<Object> list = new ArrayList<>();
        list.add(bean);
        return JdbcAccessor.doUpdateList(getConnection(), cls, daoMethod, list)[0] > 0;
    }

    /**
     * 批量更新
     *
     * @param cls
     * @param list
     * @return
     */
    public int[] update(Class cls, ArrayList<Object> list) throws Exception {
        TableInfo daoMethod = (TableInfo) MvcsManager.getTableCache(cls.getName());
        return JdbcAccessor.doUpdateList(getConnection(), cls, daoMethod, list);
    }

    /**
     * 批量删除
     *
     * @param cls
     * @param list
     * @return
     */
    public int[] delect(Class cls, ArrayList<Object> list) throws Exception {
        TableInfo daoMethod = (TableInfo) MvcsManager.getTableCache(cls.getName());
        return JdbcAccessor.doDelectList(getConnection(), cls, daoMethod, list);
    }

    /**
     * 删除一个对象
     *
     * @param bean
     * @return
     */
    public boolean delect(Object bean) throws Exception {
        Class cls = bean.getClass();
        TableInfo daoMethod = (TableInfo) MvcsManager.getTableCache(cls.getName());
        ArrayList<Object> list = new ArrayList<>();
        list.add(bean);
        return JdbcAccessor.doDelectList(getConnection(), cls, daoMethod, list)[0] > 0;
    }


    /**
     * 查询全部
     *
     * @param cls
     * @param pager
     * @param order
     * @return
     */
    public List<Object> queryList(Class cls, Pager pager, String order)  {
        TableInfo daoMethod = (TableInfo) MvcsManager.getTableCache(cls.getName());
        String sql = DBUtil.generateSelectSql(daoMethod, "", "");
        if (pager != null) {
            sql = DBUtil.generatePageSql(dbtype, daoMethod.getTableName(), sql, order, pager);
        }
        try {
            return JdbcAccessor.doLoadList(getConnection(), cls, daoMethod, sql);
        } catch (Exception e) {
            log.warn(e);
            return null;
        }
    }


    /**
     * 按条件查询全部
     *
     * @param cls
     * @param pager
     * @param whereSqlAndOrder
     * @param params
     * @return
     */
    public List<Object> queryCndList(Class cls, Pager pager, String whereSqlAndOrder, Object... params)  {
        TableInfo daoMethod = (TableInfo) MvcsManager.getTableCache(cls.getName());
        String sql = DBUtil.generateSelectSql(daoMethod, "", whereSqlAndOrder);
        if (pager != null) {
            sql = DBUtil.generatePageSql(dbtype, daoMethod.getTableName(), sql, "", pager);
        }
        try {
            return JdbcAccessor.doLoadList(getConnection(), cls, daoMethod, sql, params);
        } catch (Exception e) {
            log.warn(e);
            return null;
        }
    }

    /**
     * 按自定义sql条件查询全部
     *
     * @param cls
     * @param sql
     * @param params
     * @return
     */
    public List<Object> querySqlList(Class cls, String sql, Object... params)  {
        TableInfo daoMethod = (TableInfo) MvcsManager.getTableCache(cls.getName());
        try {
            return JdbcAccessor.doLoadList(getConnection(), cls, daoMethod, sql, params);
        } catch (Exception e) {
            log.warn(e);
            return null;
        }
    }

    /**
     * 查询全部
     *
     * @param cls
     * @return
     */
    public List<Object> queryAll(Class cls)  {
        TableInfo daoMethod = (TableInfo) MvcsManager.getTableCache(cls.getName());
        String sql = DBUtil.generateSelectSql(daoMethod, "", "");
        try {
            return JdbcAccessor.doLoadList(getConnection(), cls, daoMethod, sql);
        } catch (Exception e) {
            log.warn(e);
            return null;
        }
    }

    /**
     * 按sql条件单条记录
     *
     * @param cls
     * @param whereSql
     * @param params
     * @return
     */
    public Object querySinge(Class cls, String whereSql, Object... params)  {
        TableInfo daoMethod = (TableInfo) MvcsManager.getTableCache(cls.getName());
        String sql = DBUtil.generateSelectSql(daoMethod, "", whereSql);
        try {
            return JdbcAccessor.doLoadSinge(getConnection(), cls, daoMethod, sql, params);
        } catch (Exception e) {
            log.warn(e);
            return null;
        }
    }

    /**
     * 根据sql查询单个对象
     *
     * @param sql
     * @param params
     * @return
     */
    public HashMap<String, Object> querySinge(String sql, Object... params)  {
        try {
            return JdbcAccessor.executeQuerySinge(getConnection(), sql, params);
        } catch (Exception e) {
            log.warn(e);
            return null;
        }
    }

    @Override
    public Record queryRecord(String sql, Object... params)  {
        try {
            return JdbcAccessor.executeQueryRecordSinge(getConnection(), sql, params);
        } catch (Exception e) {
            log.warn(e);
            return null;
        }
    }


    /**
     * 根据sql查询多个对象
     *
     * @param sql
     * @param params
     * @return
     */
    public List<HashMap<String, Object>> queryList(String sql, Object... params)  {
        try {
            return JdbcAccessor.executeQueryList(getConnection(), sql, params);
        } catch (Exception e) {
            log.warn(e);
            return null;
        }
    }

    @Override
    public List<Record> queryRecordList(String sql, Object... params)  {
        try {
            return JdbcAccessor.executeRecordQueryList(getConnection(), sql, params);
        } catch (Exception e) {
            log.warn(e);
            return null;
        }
    }

    /**
     * 按主键查询单条记录
     *
     * @param cls
     * @param params
     * @return
     */
    public Object querySingePK(Class cls, Object params)  {
        TableInfo daoMethod = (TableInfo) MvcsManager.getTableCache(cls.getName());
        String sql = DBUtil.generateSelectPKSql(daoMethod);
        try {
            return JdbcAccessor.doLoadSinge(getConnection(), cls, daoMethod, sql, params);
        } catch (Exception e) {
            log.warn(e);
            return null;
        }
    }

}
