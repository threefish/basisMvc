package com.sgaop.basis.dao;

import com.sgaop.basis.dao.entity.Record;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/23 0023
 * To change this template use File | Settings | File Templates.
 */
public interface Dao {

    /**
     * 设置数据源
     *
     * @param dataSource 数据源
     * @throws SQLException
     */
    void setDataSource(DataSource dataSource) throws SQLException;

    /**
     * 添加操作
     *
     * @param entity 对象实体
     * @param <E>    泛型
     * @return 主键ID
     */
    <E> int insert(E entity);

    /**
     * 添加操作
     *
     * @param entitys 对象实体
     * @param <E>     泛型
     * @return 返回插入实体
     */
    <E> int[] insert(List<E> entitys);

    /**
     * 更新表中一行数据
     *
     * @param entity 对象实体
     * @param <E>    泛型
     * @return 是否成功
     */
    <E> boolean update(E entity);


    /**
     * 更新表中一行数据
     *
     * @param entity    对象实体
     * @param condition 条件实体
     * @param <E>       泛型
     * @return 是否成功
     */
    <E> boolean update(E entity, Condition condition);


    /**
     * 更新表中一行数据
     *
     * @param entity 对象实体
     * @param colum  字段
     * @param <E>    泛型
     * @return 是否成功
     */
    <E> boolean update(E entity, String colum, Object val);

    /**
     * 批量更新表中数据
     *
     * @param entitys 对象实体
     * @param <E>     泛型
     * @return 是否成功
     */
    <E> int[] update(List<E> entitys);


    /**
     * 删除表中一行数据
     *
     * @param entity 对象实体
     * @param <E>    泛型
     * @return 是否成功
     */
    <E> boolean delete(E entity);

    /**
     * 删除表中一行数据
     *
     * @param entity    对象实体
     * @param condition 条件实体
     * @param <E>       泛型
     * @return 是否成功
     */
    <E> boolean delete(E entity, Condition condition);

    /**
     * 删除表中多行数据
     *
     * @param entitys 对象的集合
     * @param <E>     泛型
     * @return 是否成功
     */
    <E> boolean delete(List<E> entitys);


    /**
     * 删除表中多行数据
     *
     * @param klass 表
     * @return 是否成功
     */
    boolean delete(Class klass, Condition condition);

    /**
     * @param entityClass 实体类
     * @param id          主键ID
     * @param <E>         泛型
     * @return 要获得的异常返回null
     */
    <E> E fetch(Class<E> entityClass, long id);


    /**
     * @param entityClass 实体类
     * @param condition   条件实体
     * @param <E>         泛型
     * @return 要获得的异常返回null
     */
    <E> E fetch(Class<E> entityClass, Condition condition);


    /**
     * @param entityClass 实体类
     * @param colum       字段名
     * @param value       属性值
     * @param <E>         泛型
     * @return 要获得的如果不存在返回null
     */
    <E> E fetch(Class<E> entityClass, String colum, Object value);


    /**
     * 查询所有数据
     *
     * @param entityClass 实体类
     * @param <E>         泛型
     * @return 数据列表
     */

    <E> List<E> query(Class<E> entityClass);


    /**
     * 查询所有数据
     *
     * @param entityClass 实体类
     * @param <E>         泛型
     * @return 数据列表
     */

    <E> List<E> query(Class<E> entityClass, Pager pager);

    /**
     * 查询所有数据
     *
     * @param entityClass 实体类
     * @param <E>         泛型
     * @return 数据列表
     */

    <E> List<E> query(Class<E> entityClass, Pager pager, Condition condition);


    /**
     * 查询所有数据
     *
     * @param entityClass 实体类
     * @param condition   条件实体
     * @param <E>         泛型
     * @return 数据列表
     */
    <E> List<E> query(Class<E> entityClass, Condition condition);


    /**
     * 查询字段名等值的实体列表
     *
     * @param entityClass 实体类
     * @param colum       属性名
     * @param value       属性值
     * @param <E>         泛型
     * @return 数据列表
     */
    <E> List<E> query(Class<E> entityClass, String colum, Object value);


    /**
     * 获得查询的对象实体总数
     *
     * @param entityClass 实体类
     * @return 对象实体总数 异常返回 0
     */
    int count(Class<?> entityClass);

    /**
     * 获得查询的对象实体总数
     *
     * @param entityClass 实体类
     * @param entityClass 条件实体
     * @return 对象实体总数 异常返回 0
     */
    int count(Class<?> entityClass, Condition condition);


    /**
     * 根据实体条件查询数量
     *
     * @param entityClass 实体类
     * @param colum       属性名
     * @param value       属性值
     * @return 数量
     */
    int count(Class<?> entityClass, String colum, Object value);


    /**
     * 执行非查询的SQL语言 使用 ? 做参数
     *
     * @param sql    sql语句
     * @param values 参数值数组
     * @return 返回影响的行数 异常返回-1
     */
    int execute(String sql, Object... values);


    /**
     * 执行查询的SQL语言 使用 ? 做参数
     *
     * @param sql    sql语句
     * @param values 参数值数组
     * @return 返回影响的行数 异常返回-1
     */
    List<Record> query(String sql, Object... values);


}
