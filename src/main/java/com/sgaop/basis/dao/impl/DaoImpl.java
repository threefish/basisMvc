package com.sgaop.basis.dao.impl;

import com.sgaop.basis.cache.CacheManager;
import com.sgaop.basis.dao.Dao;
import com.sgaop.basis.dao.Pager;
import com.sgaop.basis.dao.bean.TableInfo;
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
    /**
     * 数据访问器
     */
    private static JdbcAccessor accessor;

    public void commit() throws SQLException {
        accessor.commit();
    }

    public void begin() {
        accessor.begin();
    }

    public void rollback() throws SQLException {
        accessor.rollback();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return accessor.dataSource.getConnection();
    }

    public void setDataSource(DataSource dataSource) throws SQLException {
        this.accessor = new JdbcAccessor(dataSource);
    }

    /**
     * 插入一个对象,返回主键ID
     *
     * @param cls
     * @param bean
     * @return
     */
    public int insert(Class cls, Object bean) throws SQLException {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(bean);
        return accessor.doInsert(cls, daoMethod, list)[0];
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
        return accessor.doInsert(cls, daoMethod, list);
    }


    /**
     * 更新一个对象
     *
     * @param cls
     * @param bean
     * @return
     */
    public boolean update(Class cls, Object bean) throws SQLException {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(bean);
        return accessor.doUpdateList(cls, daoMethod, list)[0] > 0;
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
        return accessor.doUpdateList(cls, daoMethod, list);
    }

    /**
     * 批量删除
     *
     * @param cls
     * @param list
     * @return
     */
    public int[] delect(Class cls, ArrayList<Object> list) {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        return accessor.doDelectList(cls, daoMethod, list);
    }

    /**
     * 删除一个对象
     *
     * @param cls
     * @param bean
     * @return
     */
    public boolean delect(Class cls, Object bean) {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(bean);
        return accessor.doDelectList(cls, daoMethod, list)[0] > 0;
    }


    /**
     * 查询全部
     *
     * @param cls
     * @param pager
     * @param <T>
     * @return
     */
    public <T> List<T> queryList(Class cls, Pager pager, String order) {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        String sql = DBUtil.generateSelectSql(daoMethod, "", "");
        if (pager != null) {
            sql = DBUtil.generatePageSql(accessor.dbtype, daoMethod.getTableName(), sql, order, pager);
        }
        return accessor.doLoadList(cls, daoMethod, sql);
    }


    /**
     * 按条件查询全部
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> List<T> queryCndList(Class cls, Pager pager, String whereSqlAndOrder, Object... params) {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        String sql = DBUtil.generateSelectSql(daoMethod, "", whereSqlAndOrder);
        if (pager != null) {
            sql = DBUtil.generatePageSql(accessor.dbtype, daoMethod.getTableName(), sql, "", pager);
        }
        return accessor.doLoadList(cls, daoMethod, sql, params);
    }

    /**
     * 按自定义sql条件查询全部
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> List<T> querySqlList(Class cls, String sql, Object... params) {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        return accessor.doLoadList(cls, daoMethod, sql, params);
    }

    /**
     * 按sql条件单条记录
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T querySinge(Class cls, String whereSql, Object... params) {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        String sql = DBUtil.generateSelectSql(daoMethod, "", whereSql);
        return accessor.doLoadSinge(cls, daoMethod, sql, params);
    }

    /**
     * 根据sql查询单个对象
     *
     * @param sql
     * @param params
     * @return
     */
    public HashMap<String, Object> querySinge(String sql, Object... params) throws SQLException {
        return accessor.executeQuerySinge(sql, params);
    }


    /**
     * 根据sql查询多个对象
     *
     * @param sql
     * @param params
     * @return
     */
    public List<HashMap<String, Object>> queryList(String sql, Object... params) {
        return accessor.executeQueryList(sql, params);
    }

    /**
     * 按主键查询单条记录
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T querySingePK(Class cls, Object params) {
        TableInfo daoMethod = (TableInfo) CacheManager.getTableCache(cls.getName());
        String sql = DBUtil.generateSelectPKSql(daoMethod);
        return accessor.doLoadSinge(cls, daoMethod, sql, params);
    }


}
