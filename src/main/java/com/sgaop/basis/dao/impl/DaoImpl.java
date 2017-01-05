package com.sgaop.basis.dao.impl;

import com.sgaop.basis.cache.MvcsManager;
import com.sgaop.basis.dao.Condition;
import com.sgaop.basis.dao.Dao;
import com.sgaop.basis.dao.DbType;
import com.sgaop.basis.dao.Pager;
import com.sgaop.basis.dao.bean.TableInfo;
import com.sgaop.basis.dao.entity.Record;
import com.sgaop.basis.trans.BasisTransaction;
import com.sgaop.basis.trans.Trans;
import com.sgaop.basis.util.DBUtil;
import com.sgaop.basis.util.Logs;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/23 0023
 * To change this template use File | Settings | File Templates.
 */
public class DaoImpl implements Dao {

    private DataSource dataSource;

    private Connection connection;

    public DbType dbtype;

    private static final Logger log = Logs.get();


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


    /**
     * 添加操作
     *
     * @param entity 对象实体
     * @return 返回插入实体
     */
    @Override
    public <E> int insert(E entity) {
        Class cls = entity.getClass();
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(cls.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, cls, entity);
        int id = 0;
        try {
            String insetSql = builder.getInsetSql();
            PreparedStatement pstm = getConnection().prepareStatement(insetSql, Statement.RETURN_GENERATED_KEYS);
            DBUtil.setParams(pstm, builder.getOneVals().toArray());
            pstm.executeUpdate();
            //打印sql
            DBUtil.showSql(pstm);
            //执行查询，并取得查询结果
            ResultSet rs = pstm.getGeneratedKeys();
            while (rs.next()) {
                id = rs.getInt(1);
            }
            DBUtil.close(pstm, rs, null);
        } catch (SQLException e) {
            log.debug(e);
        }
        return id;
    }

    /**
     * 添加操作
     *
     * @param entitys 对象实体
     * @return 返回插入实体
     */
    @Override
    public <E> int[] insert(List<E> entitys) {
        Class cls = null;
        if (entitys.size() > 0) {
            cls = entitys.get(0).getClass();
        } else {
            log.error("插入对象数量不能为0");
            return null;
        }
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(cls.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, cls, entitys);
        int rowCount = entitys.size();
        int[] keys = new int[rowCount];
        try {
            String insetSql = builder.getInsetSql();
            PreparedStatement pstm = getConnection().prepareStatement(insetSql, Statement.RETURN_GENERATED_KEYS);
            LinkedList<LinkedList<Object>> linkedList = builder.getManyVals();
            for (LinkedList<Object> objectLinkedList : linkedList) {
                DBUtil.setParams(pstm, objectLinkedList.toArray());
                pstm.addBatch();
            }
            keys = pstm.executeBatch();
            //打印sql
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, null, null);
        } catch (SQLException e) {
            log.debug(e);
        }
        return keys;

    }

    /**
     * 更新表中一行数据
     *
     * @param entity 对象实体
     * @return 是否成功
     */
    @Override
    public <E> boolean update(E entity) {
        Class cls = entity.getClass();
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(cls.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, cls, entity);
        int id = 0;
        try {
            String insetSql = builder.getUpdatePkSql();
            PreparedStatement pstm = getConnection().prepareStatement(insetSql);

            LinkedList<Object> vals = builder.getOneVals();
            for (Object val : builder.getOnePKVals().toArray()) {
                vals.add(val);
            }
            DBUtil.setParams(pstm, vals.toArray());
            id = pstm.executeUpdate();
            //打印sql
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, null, null);
        } catch (SQLException e) {
            log.debug(e);
        }
        return id > 0;
    }

    /**
     * 更新表中一行数据
     *
     * @param entity    对象实体
     * @param condition 条件实体
     * @return 是否成功
     */
    @Override
    public <E> boolean update(E entity, Condition condition) {
        Class cls = entity.getClass();
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(cls.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, cls, entity);
        int id = 0;
        try {
            String updateSql = builder.getUpdateNoPkSql() + condition.toSql();
            PreparedStatement pstm = getConnection().prepareStatement(updateSql);
            LinkedList<Object> vals = builder.getNoPkVals();
            for (Object val : condition.valToArry()) {
                vals.add(val);
            }
            DBUtil.setParams(pstm, vals.toArray());
            id = pstm.executeUpdate();
            //打印sql
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, null, null);
        } catch (SQLException e) {
            log.debug(e);
        }
        return id > 0;
    }


    /**
     * 更新表中一行数据
     *
     * @param entity 对象实体
     * @param colum  字段
     * @param <E>    泛型
     */
    @Override
    public <E> boolean update(E entity, String colum, Object val) {
        Condition condition = new Condition();
        condition.and(colum, "=", val);
        Class cls = entity.getClass();
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(cls.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, cls, entity);
        int id = 0;
        try {
            String insetSql = builder.getUpdateNoPkSql() + condition.toSql();
            PreparedStatement pstm = getConnection().prepareStatement(insetSql);
            LinkedList<Object> vals = builder.getNoPkVals();
            for (Object cval : condition.valToArry()) {
                vals.add(cval);
            }
            DBUtil.setParams(pstm, vals.toArray());
            id = pstm.executeUpdate();
            //打印sql
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, null, null);
        } catch (SQLException e) {
            log.debug(e);
        }
        return id > 0;
    }

    /**
     * 批量更新表中数据
     *
     * @param entitys 对象实体
     * @return 是否成功
     */
    @Override
    public <E> int[] update(List<E> entitys) {
        Class cls = null;
        if (entitys.size() > 0) {
            cls = entitys.get(0).getClass();
        } else {
            log.error("插入对象数量不能为0");
            return null;
        }
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(cls.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, cls, entitys);
        int rowCount = entitys.size();
        int[] keys = new int[rowCount];
        try {
            String insetSql = builder.getUpdatePkSql();
            PreparedStatement pstm = getConnection().prepareStatement(insetSql, Statement.RETURN_GENERATED_KEYS);
            LinkedList<LinkedList<Object>> linkedList = builder.getManyVals();
            LinkedList<LinkedList<Object>> linkedPkList = builder.getManyPkVals();
            int i = 0;
            for (LinkedList<Object> objectLinkedList : linkedList) {
                LinkedList<Object> vals = linkedPkList.get(i);
                for (Object val : vals.toArray()) {
                    objectLinkedList.add(val);
                }
                DBUtil.setParams(pstm, objectLinkedList.toArray());
                pstm.addBatch();
                i++;
            }
            keys = pstm.executeBatch();
            //打印sql
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, null, null);
        } catch (SQLException e) {
            log.debug(e);
        }
        return keys;
    }


    /**
     * 删除表中一行数据
     *
     * @param entity 对象实体
     * @return 是否成功
     */
    @Override
    public <E> boolean delete(E entity) {
        Class cls = entity.getClass();
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(cls.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, cls, entity);
        String delSql = builder.getDelPkSql();
        int i = 0;
        try {
            PreparedStatement pstm = getConnection().prepareStatement(delSql);
            DBUtil.setParams(pstm, builder.getOnePKVals().toArray());
            i = pstm.executeUpdate();
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i > 0;
    }

    /**
     * 删除表中一行数据
     *
     * @param entity    对象实体
     * @param condition 条件实体
     * @return 是否成功
     */
    @Override
    public <E> boolean delete(E entity, Condition condition) {
        Class cls = entity.getClass();
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(cls.getName());
        try {
            PreparedStatement pstm = getConnection().prepareStatement("delete from " + tableInfo.getTableName() + condition.toSql());
            DBUtil.setParams(pstm, condition.valToArry());
            pstm.executeUpdate();
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, null, null);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除表中多行数据
     *
     * @param entitys 对象的集合
     * @return 是否成功
     */
    @Override
    public <E> boolean delete(List<E> entitys) {
        Class cls = null;
        int rowCount = entitys.size();
        if (rowCount > 0) {
            cls = entitys.get(0).getClass();
        } else {
            log.error("删除对象数量不能为0");
            return false;
        }
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(cls.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, cls, entitys);
        String delSql = builder.getDelPkSql();
        try {
            PreparedStatement pstm = getConnection().prepareStatement(delSql);
            LinkedList<LinkedList<Object>> linkedPkList = builder.getManyPkVals();
            for (LinkedList<Object> pkvals : linkedPkList) {
                DBUtil.setParams(pstm, pkvals.toArray());
                pstm.addBatch();
            }
            pstm.executeBatch();
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, null, null);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            log.error("不能删除无主键的数据，请使用其他方法删除");
            return false;
        }
    }

    /**
     * 删除表中多行数据
     *
     * @param klass  对象的集合
     * @param condition
     * @return 是否成功
     */
    @Override
    public boolean delete(Class klass, Condition condition) {
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(klass.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, klass, null);
        String delSql = builder.getDelPkSql() + condition.toSql();
        try {
            PreparedStatement pstm = getConnection().prepareStatement(delSql);
            DBUtil.setParams(pstm, condition.valToArry());
            pstm.execute();
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, null, null);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            log.error("不能删除无主键的数据，请使用其他方法删除");
            return false;
        }
    }

    /**
     * 以ID为主键的对象才能查询
     *
     * @param entityClass 实体类
     * @param id          主键ID
     * @return 要获得的异常返回null
     */
    @Override
    public <E> E fetch(Class<E> entityClass, long id) {
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(entityClass.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, entityClass, null);
        E E = null;
        try {
            PreparedStatement pstm = getConnection().prepareStatement(builder.getSelectSql() + " where id=?");
            DBUtil.setParams(pstm, id);
            ResultSet rs = pstm.executeQuery();
            Record record = DBUtil.getRecord(rs);
            E = DBUtil.RecordToEntity(entityClass, tableInfo, record);
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, rs, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return E;
    }

    /**
     * @param entityClass 实体类
     * @param condition   条件实体
     * @return 要获得的异常返回null
     */
    @Override
    public <E> E fetch(Class<E> entityClass, Condition condition) {
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(entityClass.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, entityClass, null);
        E E = null;
        try {
            PreparedStatement pstm = getConnection().prepareStatement(builder.getSelectSql() + condition.toSql());
            DBUtil.setParams(pstm, condition.valToArry());
            ResultSet rs = pstm.executeQuery();
            Record record = DBUtil.getRecord(rs);
            E = DBUtil.RecordToEntity(entityClass, tableInfo, record);
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, rs, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return E;
    }


    /**
     * @param entityClass 实体类
     * @param colum       字段名
     * @param value       属性值
     * @return 要获得的如果不存在返回null
     */
    @Override
    public <E> E fetch(Class<E> entityClass, String colum, Object value) {
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(entityClass.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, entityClass, null);
        E E = null;
        try {
            PreparedStatement pstm = getConnection().prepareStatement(builder.getSelectSql() + " where " + colum + "=?");
            DBUtil.setParams(pstm, value);
            ResultSet rs = pstm.executeQuery();
            Record record = DBUtil.getRecord(rs);
            E = DBUtil.RecordToEntity(entityClass, tableInfo, record);
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, rs, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return E;
    }


    /**
     * 查询所有数据
     *
     * @param entityClass 实体类
     * @param pager       分页信息
     * @return 数据列表
     */
    @Override
    public <E> List<E> query(Class<E> entityClass, Pager pager) {
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(entityClass.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, entityClass, null);
        List<E> E = new ArrayList<>();
        try {
            String sql = builder.getSelectSql();
            if (pager != null) {
                sql = DBUtil.generatePageSql(dbtype, tableInfo.getTableName(), sql, "", pager);
            }
            PreparedStatement pstm = getConnection().prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            List<Record> records = DBUtil.getRecords(rs);
            for (Record record : records) {
                E.add(DBUtil.RecordToEntity(entityClass, tableInfo, record));
            }
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, rs, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return E;
    }

    /**
     * 查询所有数据
     *
     * @param entityClass 实体类
     * @param pager       分页信息
     * @return 数据列表
     */
    @Override
    public <E> List<E> query(Class<E> entityClass, Pager pager, Condition condition) {
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(entityClass.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, entityClass, null);
        List<E> E = new ArrayList<>();
        try {
            String sql = builder.getSelectSql() + condition.toSql();
            if (pager != null) {
                sql = DBUtil.generatePageSql(dbtype, tableInfo.getTableName(), sql, "", pager);
            }
            PreparedStatement pstm = getConnection().prepareStatement(sql);
            DBUtil.setParams(pstm, condition.valToArry());
            ResultSet rs = pstm.executeQuery();
            List<Record> records = DBUtil.getRecords(rs);
            for (Record record : records) {
                E.add(DBUtil.RecordToEntity(entityClass, tableInfo, record));
            }
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, rs, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return E;
    }


    /**
     * 查询所有数据
     *
     * @param entityClass 实体类
     * @return 数据列表
     */
    @Override
    public <E> List<E> query(Class<E> entityClass) {
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(entityClass.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, entityClass, null);
        List<E> E = new ArrayList<>();
        try {
            PreparedStatement pstm = getConnection().prepareStatement(builder.getSelectSql());
            ResultSet rs = pstm.executeQuery();
            List<Record> records = DBUtil.getRecords(rs);
            for (Record record : records) {
                E.add(DBUtil.RecordToEntity(entityClass, tableInfo, record));
            }
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, rs, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return E;
    }

    /**
     * 查询所有数据
     *
     * @param entityClass 实体类
     * @param condition   条件实体
     * @return 数据列表
     */
    @Override
    public <E> List<E> query(Class<E> entityClass, Condition condition) {
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(entityClass.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, entityClass, null);
        List<E> E = new ArrayList<>();
        try {
            PreparedStatement pstm = getConnection().prepareStatement(builder.getSelectSql() + condition.toSql());
            DBUtil.setParams(pstm, condition.valToArry());
            ResultSet rs = pstm.executeQuery();
            List<Record> records = DBUtil.getRecords(rs);
            for (Record record : records) {
                E.add(DBUtil.RecordToEntity(entityClass, tableInfo, record));
            }
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, rs, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return E;
    }

    /**
     * 查询字段名等值的实体列表
     *
     * @param entityClass 实体类
     * @param colum       属性名
     * @param value       属性值
     * @return 数据列表
     */
    @Override
    public <E> List<E> query(Class<E> entityClass, String colum, Object value) {
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(entityClass.getName());
        JdbcBuilder builder = new JdbcBuilder(tableInfo, entityClass, null);
        List<E> E = new ArrayList<>();
        try {
            PreparedStatement pstm = getConnection().prepareStatement(builder.getSelectSql() + " where " + colum + "=?");
            DBUtil.setParams(pstm, value);
            ResultSet rs = pstm.executeQuery();
            List<Record> records = DBUtil.getRecords(rs);
            for (Record record : records) {
                E.add(DBUtil.RecordToEntity(entityClass, tableInfo, record));
            }
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, rs, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return E;
    }

    /**
     * 获得查询的对象实体总数
     *
     * @param entityClass 实体类
     * @return 对象实体总数 异常返回 0
     */
    @Override
    public int count(Class<?> entityClass) {
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(entityClass.getName());
        int count = 0;
        try {
            PreparedStatement pstm = getConnection().prepareStatement("SELECT  COUNT(*) FROM " + tableInfo.getTableName());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, rs, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 获得查询的对象实体总数
     *
     * @param entityClass 实体类
     * @param condition
     * @return 对象实体总数 异常返回 0
     */
    @Override
    public int count(Class<?> entityClass, Condition condition) {
        TableInfo tableInfo = (TableInfo) MvcsManager.getTableCache(entityClass.getName());
        int count = 0;
        try {
            PreparedStatement pstm = getConnection().prepareStatement("SELECT  COUNT(*) FROM " + tableInfo.getTableName() + condition.toSql());
            DBUtil.setParams(pstm, condition.valToArry());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, rs, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 根据实体条件查询数量
     *
     * @param entityClass 实体类
     * @param colum       属性名
     * @param value       属性值
     * @return 数量
     */
    @Override
    public int count(Class<?> entityClass, String colum, Object value) {
        Condition cnd = new Condition();
        cnd.and(colum, "=", value);
        return count(entityClass, cnd);
    }

    /**
     * 执行非查询的SQL语言 使用 ? 做参数
     *
     * @param sql    sql语句
     * @param values 参数值数组
     * @return 返回影响的行数 异常返回-1
     */
    @Override
    public int execute(String sql, Object... values) {
        int rs = 0;
        try {
            List<Record> records = new ArrayList<>();
            PreparedStatement pstm = getConnection().prepareStatement(sql);
            DBUtil.setParams(pstm, values);
            rs = pstm.executeUpdate();
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 执行查询的SQL语言 使用 ? 做参数
     *
     * @param sql    sql语句
     * @param values 参数值数组
     * @return 返回影响的行数 异常返回-1
     */
    @Override
    public List<Record> query(String sql, Object... values) {
        List<Record> records = new ArrayList<>();
        try {
            PreparedStatement pstm = getConnection().prepareStatement(sql);
            DBUtil.setParams(pstm, values);
            ResultSet rs = pstm.executeQuery();
            records = DBUtil.getRecords(rs);
            DBUtil.showSql(pstm);
            DBUtil.close(pstm, rs, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }
}
