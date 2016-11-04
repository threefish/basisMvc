package com.sgaop.basis.dao.impl;

import com.sgaop.basis.cache.CacheManager;
import com.sgaop.basis.dao.Dao;
import com.sgaop.basis.dao.DbType;
import com.sgaop.basis.dao.Pager;
import com.sgaop.basis.dao.bean.TableInfo;
import com.sgaop.basis.trans.Transaction;
import com.sgaop.basis.util.DBUtil;

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

    private DataSource dataSource = null;

    private Connection conn = null;

    public DbType dbtype;

    public void commit() throws SQLException {
        JdbcAccessor.commit(getConn());
    }

    public void begin() throws SQLException {
        JdbcAccessor.begin(Transaction.getLevel(), getConn());
    }

    public void rollback() throws SQLException {
        JdbcAccessor.rollback(getConn());
    }

    public Connection getConn() throws SQLException {
        if (this.conn == null || this.conn.isClosed()) {
            this.conn = dataSource.getConnection();
        }
        if (Transaction.beanginTrans() && this.conn.getAutoCommit()) {
            this.conn.setAutoCommit(false);
            int oldLevel = this.conn.getTransactionIsolation();
            this.conn.setTransactionIsolation(Transaction.getLevel());
            Transaction.setConn(this.conn, oldLevel, Transaction.getLevel());
        }
        return this.conn;
    }

    public void setDataSource(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        this.dbtype = DBUtil.getDataBaseType(dataSource.getConnection());
    }

    /**
     * 插入一个对象,返回主键ID
     *
     * @param bean
     * @return
     */
    public int insert(Object bean) throws SQLException {
        Class cls = bean.getClass();
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        ArrayList<Object> list = new ArrayList<>();
        list.add(bean);
        return JdbcAccessor.doInsert(getConn(), cls, daoMethod, list)[0];
    }

    /**
     * 批量插入
     *
     * @param cls
     * @param list
     * @return
     */
    public int[] insert(Class cls, ArrayList<Object> list) throws SQLException {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        return JdbcAccessor.doInsert(getConn(), cls, daoMethod, list);
    }


    /**
     * 更新一个对象
     *
     * @param bean
     * @return
     */
    public boolean update(Object bean) throws SQLException {
        Class cls = bean.getClass();
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        ArrayList<Object> list = new ArrayList<>();
        list.add(bean);
        return JdbcAccessor.doUpdateList(getConn(), cls, daoMethod, list)[0] > 0;
    }

    /**
     * 批量更新
     *
     * @param cls
     * @param list
     * @return
     */
    public int[] update(Class cls, ArrayList<Object> list) throws SQLException {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        return JdbcAccessor.doUpdateList(getConn(), cls, daoMethod, list);
    }

    /**
     * 批量删除
     *
     * @param cls
     * @param list
     * @return
     */
    public int[] delect(Class cls, ArrayList<Object> list) throws SQLException {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        return JdbcAccessor.doDelectList(getConn(), cls, daoMethod, list);
    }

    /**
     * 删除一个对象
     *
     * @param bean
     * @return
     */
    public boolean delect(Object bean) throws SQLException {
        Class cls = bean.getClass();
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        ArrayList<Object> list = new ArrayList<>();
        list.add(bean);
        return JdbcAccessor.doDelectList(getConn(), cls, daoMethod, list)[0] > 0;
    }


    /**
     * 查询全部
     *
     * @param cls
     * @param pager
     * @param <T>
     * @return
     */
    public <T> List<T> queryList(Class cls, Pager pager, String order) throws SQLException {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        String sql = DBUtil.generateSelectSql(daoMethod, "", "");
        if (pager != null) {
            sql = DBUtil.generatePageSql(dbtype, daoMethod.getTableName(), sql, order, pager);
        }
        return JdbcAccessor.doLoadList(getConn(), cls, daoMethod, sql);
    }


    /**
     * 按条件查询全部
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> List<T> queryCndList(Class cls, Pager pager, String whereSqlAndOrder, Object... params) throws SQLException {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        String sql = DBUtil.generateSelectSql(daoMethod, "", whereSqlAndOrder);
        if (pager != null) {
            sql = DBUtil.generatePageSql(dbtype, daoMethod.getTableName(), sql, "", pager);
        }
        return JdbcAccessor.doLoadList(getConn(), cls, daoMethod, sql, params);
    }

    /**
     * 按自定义sql条件查询全部
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> List<T> querySqlList(Class cls, String sql, Object... params) throws SQLException {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        return JdbcAccessor.doLoadList(getConn(), cls, daoMethod, sql, params);
    }

    /**
     * 按sql条件单条记录
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T querySinge(Class cls, String whereSql, Object... params) throws SQLException {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        String sql = DBUtil.generateSelectSql(daoMethod, "", whereSql);
        return JdbcAccessor.doLoadSinge(getConn(), cls, daoMethod, sql, params);
    }

    /**
     * 根据sql查询单个对象
     *
     * @param sql
     * @param params
     * @return
     */
    public HashMap<String, Object> querySinge(String sql, Object... params) throws SQLException {
        return JdbcAccessor.executeQuerySinge(getConn(), sql, params);
    }


    /**
     * 根据sql查询多个对象
     *
     * @param sql
     * @param params
     * @return
     */
    public List<HashMap<String, Object>> queryList(String sql, Object... params) throws SQLException {
        return JdbcAccessor.executeQueryList(getConn(), sql, params);
    }

    /**
     * 按主键查询单条记录
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T querySingePK(Class cls, Object params) throws SQLException {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        String sql = DBUtil.generateSelectPKSql(daoMethod);
        return JdbcAccessor.doLoadSinge(getConn(), cls, daoMethod, sql, params);
    }


}
