package com.sgaop.basis.dao;


import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 30695 on 2016/9/25 0025.
 */

public interface Dao {

    /**
     * 设置数据源
     *
     * @param dataSource
     * @throws SQLException
     */
    void setDataSource(DataSource dataSource) throws SQLException;


    /**
     * 插入一个对象,返回主键ID
     *
     * @param bean
     * @return
     */
    int insert(Object bean) throws SQLException;

    /**
     * 批量插入
     *
     * @param cls
     * @param list
     * @return
     */
    int[] insert(Class cls, ArrayList<Object> list) throws SQLException;


    /**
     * 更新一个对象
     *
     * @param bean
     * @return
     */
    boolean update(Object bean) throws SQLException;

    /**
     * 批量更新
     *
     * @param cls
     * @param list
     * @return
     */
    int[] update(Class cls, ArrayList<Object> list) throws SQLException;

    /**
     * 批量删除
     *
     * @param cls
     * @param list
     * @return
     */
    int[] delect(Class cls, ArrayList<Object> list) throws SQLException;

    /**
     * 删除一个对象
     *
     * @param bean
     * @return
     */
    boolean delect(Object bean) throws SQLException;


    /**
     * 查询全部
     *
     * @param cls
     * @param pager
     * @param order
     * @return
     */
    <Object> List<Object> queryList(Class cls, Pager pager, String order) throws SQLException;


    /**
     * 按条件查询全部
     *
     * @param cls
     * @param pager
     * @param whereSqlAndOrder
     * @param params
     * @return
     */
    <Object> List<Object> queryCndList(Class cls, Pager pager, String whereSqlAndOrder, Object... params) throws SQLException;

    /**
     * 按自定义sql条件查询全部
     *
     * @param cls
     * @param sql
     * @param params
     * @return
     */
    <Object> List<Object> querySqlList(Class cls, String sql, Object... params) throws SQLException;

    /**
     * 按sql条件单条记录
     *
     * @param cls
     * @param whereSql
     * @param params
     * @return
     */
    <Object> Object querySinge(Class cls, String whereSql, Object... params) throws SQLException;

    /**
     * 根据sql查询单个对象
     *
     * @param sql
     * @param params
     * @return
     */
    HashMap<String, Object> querySinge(String sql, Object... params) throws SQLException;

    /**
     * 根据sql查询多个对象
     *
     * @param sql
     * @param params
     * @return
     */
    List<HashMap<String, Object>> queryList(String sql, Object... params) throws SQLException;

    /**
     * 按主键查询单条记录
     *
     * @param cls
     * @param params
     * @return
     */
    <Object> Object querySingePK(Class cls, Object params) throws SQLException;
}
