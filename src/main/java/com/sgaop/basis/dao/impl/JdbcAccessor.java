package com.sgaop.basis.dao.impl;

import com.sgaop.basis.dao.DbType;
import com.sgaop.basis.dao.bean.TableFiled;
import com.sgaop.basis.dao.bean.TableInfo;
import com.sgaop.basis.trans.Transaction;
import com.sgaop.basis.util.ClassTool;
import com.sgaop.basis.util.DBUtil;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/6/30 0030
 * To change this template use File | Settings | File Templates.
 */
public class JdbcAccessor {

    /**
     * 提交事务
     *
     * @throws SQLException
     */
    public static void commit(Connection conn) throws SQLException {
        conn.commit();
    }

    /**
     * 设置事务为非自动提交
     */
    public static void begin(int level, Connection conn) throws SQLException {
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(level);
    }

    /**
     * 回滚事务
     *
     * @throws SQLException
     */
    public static void rollback(Connection conn) throws SQLException {
        conn.rollback();
    }


    /**
     * 恢复事务
     *
     * @throws SQLException
     */
    public static void resumConn(int level, Connection conn) throws SQLException {
        //恢复事务隔离级别
        conn.setAutoCommit(true);
        conn.setTransactionIsolation(level);
        Close(conn);
    }

    /**
     * 关闭连接
     *
     * @throws SQLException
     */
    public static void Close(Connection conn) throws SQLException {
        DBUtil.close(conn);
    }

    /**
     * 执行查询，并取得查询结果集合，将结果装入HashMap中
     *
     * @param sql
     * @param params
     * @return
     */
    public static List<HashMap<String, Object>> executeQueryList(Connection conn, String sql, Object... params) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        try {
            pstm = conn.prepareStatement(sql);
            //设置参数
            DBUtil.setParams(pstm, params);
            //打印sql
            DBUtil.showSql(pstm.toString());
            //执行查询，并取得查询结果
            rs = pstm.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            while (rs.next()) {
                HashMap<String, Object> dataMap = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    dataMap.put(String.valueOf(meta.getColumnName(i)).toLowerCase(), rs.getObject(i));
                }
                data.add(dataMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(pstm, rs, conn);
        }
        return data;
    }


    /**
     * 执行查询，并取得查询单个结果，将结果装入HashMap中
     *
     * @param sql
     * @param params
     * @return
     */
    public static HashMap<String, Object> executeQuerySinge(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        HashMap<String, Object> data = new HashMap<String, Object>();
        pstm = conn.prepareStatement(sql);
        //设置参数
        DBUtil.setParams(pstm, params);
        //打印sql
        DBUtil.showSql(pstm.toString());
        //执行查询，并取得查询结果
        rs = pstm.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        int max = 0;
        while (rs.next() && max == 0) {
            for (int i = 1; i <= columnCount; i++) {
                data.put(String.valueOf(meta.getColumnName(i)).toLowerCase(), rs.getObject(i));
            }
            max++;
        }
        DBUtil.close(pstm, rs, conn);
        return data;
    }

    /**
     * 将查询出来的数据装入实体对象
     *
     * @param cls
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T doLoadSinge(Connection conn, Class cls, TableInfo daoMethod, String sql, Object... params) {
        Object obj = null;
        try {
            HashMap<String, Object> data = executeQuerySinge(conn, sql, params);
            if (data.size() == 0) {
                return null;
            }
            obj = cls.newInstance();
            for (String colum : daoMethod.getColums()) {
                TableFiled tableFiled = daoMethod.getDaoFiled(colum);
                String colums = tableFiled.getColumName();
                Object value = data.get(colums);
                Field field = cls.getDeclaredField(tableFiled.getFiledName());
                String methodName = tableFiled.get_setMethodName();
                ClassTool.invokeMethod(field, methodName, cls, obj, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (T) obj;
    }


    /**
     * 将查询出来的数据装入实体对象
     *
     * @param cls
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> doLoadList(Connection conn, Class cls, TableInfo daoMethod, String sql, Object... params) {
        List<T> dataList = new ArrayList<T>();
        try {
            List<HashMap<String, Object>> data = executeQueryList(conn, sql, params);
            if (data.size() == 0) {
                return null;
            }
            for (HashMap<String, Object> map : data) {
                Object obj = cls.newInstance();
                for (String colum : daoMethod.getColums()) {
                    TableFiled tableFiled = daoMethod.getDaoFiled(colum);
                    Object value = map.get(colum);
                    String methodName = tableFiled.get_setMethodName();
                    ClassTool.invokeMethod(cls.getDeclaredField(tableFiled.getFiledName()), methodName, cls, obj, value);
                }
                dataList.add((T) obj);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return dataList;
    }


    /**
     * 批量插入对象
     *
     * @param cls
     * @param daoMethod
     * @param listBean
     * @return
     */
    public static int[] doInsert(Connection conn, Class cls, TableInfo daoMethod, ArrayList<Object> listBean) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int rowCount = listBean.size();
        int[] keys = new int[rowCount];
        for (Object bean : listBean) {
            LinkedHashMap<String, Object> columMap = new LinkedHashMap<>();
            StringBuffer sb = new StringBuffer("insert into " + daoMethod.getTableName() + "(");
            StringBuffer sb2 = new StringBuffer(" values(");
            for (String colum : daoMethod.getColums()) {
                TableFiled tableFiled = daoMethod.getDaoFiled(colum);
                String methodName = tableFiled.get_getMethodName();
                Object value = ClassTool.invokeGetMethod(cls, bean, methodName);
                sb.append(colum + ",");
                sb2.append("?,");
                columMap.put(tableFiled.getColumName(), value);
            }
            sb.delete(sb.length() - 1, sb.length());
            sb2.delete(sb2.length() - 1, sb2.length());
            sb.append(")");
            sb2.append(")");
            sb.append(sb2.toString());
            if (pstm == null || pstm.isClosed()) {
                pstm = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            }
            int i = 1;
            for (Map.Entry entry : columMap.entrySet()) {
                Object val = entry.getValue();
                pstm.setObject(i, val);
                i++;
            }
            //打印sql
            DBUtil.showSql(pstm.toString());
            if(listBean.size()>1){
                pstm.addBatch();
            }
        }
        if (listBean.size() > 1) {
            pstm.executeBatch();
        }else{
            pstm.executeUpdate();
        }
        //执行查询，并取得查询结果
        rs = pstm.getGeneratedKeys();
        int i = 0;
        while (rs.next() && i < rowCount) {
            keys[i] = rs.getInt(1);
            i++;
        }
        DBUtil.close(pstm, rs, conn);
        return keys;
    }

    /**
     * 批量更新
     *
     * @param cls
     * @param daoMethod
     * @param listBean
     * @return
     */
    public static int[] doUpdateList(Connection conn, Class cls, TableInfo daoMethod, ArrayList<Object> listBean) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int rowCount = listBean.size();
        int[] keys = new int[rowCount];
        for (Object bean : listBean) {
            LinkedHashMap<String, Object> columMap = new LinkedHashMap<>();
            StringBuffer sb = new StringBuffer("update " + daoMethod.getTableName() + " beanginTrans ");
            for (String colum : daoMethod.getColums()) {
                if (!colum.equals(daoMethod.getPkName())) {
                    TableFiled tableFiled = daoMethod.getDaoFiled(colum);
                    String methodName = tableFiled.get_getMethodName();
                    Object value = ClassTool.invokeGetMethod(cls, bean, methodName);
                    sb.append(colum + "=?,");
                    columMap.put(tableFiled.getColumName(), value);
                }
            }
            sb.delete(sb.length() - 1, sb.length());
            sb.append(" where " + daoMethod.getPkName() + "=?");
            if (pstm == null || pstm.isClosed()) {
                pstm = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            for (Map.Entry entry : columMap.entrySet()) {
                Object val = entry.getValue();
                pstm.setObject(i, val);
                i++;
            }
            //设置主键值
            pstm.setObject(i, ClassTool.invokeGetMethod(cls, bean, daoMethod.getDaoFiled(daoMethod.getPkName()).get_getMethodName()));
            //打印sql
            DBUtil.showSql(pstm.toString());
            pstm.addBatch();
        }
        keys = pstm.executeBatch();

        DBUtil.close(pstm, rs, conn);
        return keys;
    }


    /**
     * 批量删除
     *
     * @param cls
     * @param daoMethod
     * @param listBean
     * @return
     */
    public static int[] doDelectList(Connection conn, Class cls, TableInfo daoMethod, ArrayList<Object> listBean) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int rowCount = listBean.size();
        int[] keys = new int[rowCount];
        try {
            for (Object bean : listBean) {
                StringBuffer sb = new StringBuffer("delete from " + daoMethod.getTableName());
                sb.append(" where " + daoMethod.getPkName() + "=?");
                if (pstm == null || pstm.isClosed()) {
                    pstm = conn.prepareStatement(sb.toString());
                }
                //设置主键值
                pstm.setObject(1, ClassTool.invokeGetMethod(cls, bean, daoMethod.getDaoFiled(daoMethod.getPkName()).get_getMethodName()));
                //打印sql
                DBUtil.showSql(pstm.toString());
                pstm.addBatch();
            }
            keys = pstm.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(pstm, rs, conn);
        }
        return keys;
    }


}
