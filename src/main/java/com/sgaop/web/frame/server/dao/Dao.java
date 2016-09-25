package com.sgaop.web.frame.server.dao;


import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 30695 on 2016/9/25 0025.
 */

public interface Dao {

    void commit();

    void begin(boolean autoCommit);

    void rollback();

    void setDataSource(DataSource dataSource)throws SQLException;

    /**
     * 插入一个对象,返回主键ID
     *
     * @param cls
     * @param bean
     * @return
     */
    int insert(Class cls, Object bean) throws SQLException;

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
     * @param cls
     * @param bean
     * @return
     */
    boolean update(Class cls, Object bean) throws SQLException;

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
    int[] delect(Class cls, ArrayList<Object> list);

    /**
     * 删除一个对象
     *
     * @param cls
     * @param bean
     * @return
     */
    boolean delect(Class cls, Object bean);


    /**
     * 查询全部
     *
     * @param cls
     * @param pager
     * @param <T>
     * @return
     */
    <T> List<T> queryList(Class cls, Pager pager, String order);


    /**
     * 按条件查询全部
     *
     * @param cls
     * @param <T>
     * @return
     */
    <T> List<T> queryCndList(Class cls, Pager pager, String whereSqlAndOrder, Object... params);

    /**
     * 按自定义sql条件查询全部
     *
     * @param cls
     * @param <T>
     * @return
     */
    <T> List<T> querySqlList(Class cls, String sql, Object... params);

    /**
     * 按sql条件单条记录
     *
     * @param cls
     * @param <T>
     * @return
     */
    <T> T querySinge(Class cls, String whereSql, Object... params);

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
    List<HashMap<String, Object>> queryList(String sql, Object... params);

    /**
     * 按主键查询单条记录
     *
     * @param cls
     * @param <T>
     * @return
     */
    <T> T querySingePK(Class cls, Object params);
}
