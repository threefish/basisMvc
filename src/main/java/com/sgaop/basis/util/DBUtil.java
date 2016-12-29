package com.sgaop.basis.util;

import com.alibaba.druid.pool.DruidPooledPreparedStatement;
import com.sgaop.basis.dao.DbType;
import com.sgaop.basis.dao.Pager;
import com.sgaop.basis.dao.bean.TableFiled;
import com.sgaop.basis.dao.bean.TableInfo;
import com.sgaop.basis.dao.entity.Record;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/27 0027
 * To change this template use File | Settings | File Templates.
 */
public class DBUtil {

    private static final Logger logger = Logger.getRootLogger();

    /**
     * 判断数据库类型
     *
     * @param dbconn
     * @return
     */
    public static DbType getDataBaseType(Connection dbconn) {
        try {
            String driverName = dbconn.getMetaData().getDriverName().toUpperCase();
            //通过driverName是否包含关键字判断
            if (driverName.indexOf("MYSQL") != -1) {
                return DbType.mysql;
            } else if (driverName.indexOf("SQL SERVER") != -1) {
                //sqljdbc与sqljdbc4不同，sqlserver中间有空格
                return DbType.mssql;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成分页语句
     *
     * @param sql
     * @param order
     * @param pager
     */
    public static String generatePageSql(DbType dbType, String tableName, String sql, String order, Pager pager) {
        if (dbType.equals(DbType.mysql)) {
            return generatePageMySql(sql, order, pager);
        } else if (dbType.equals(DbType.oracle)) {
            return generatePageOracle(sql, tableName, order, pager);
        } else {
            logger.error("暂不支持该数据库类型的分页功能!");
            return sql;
        }
    }

    /**
     * 生成MySql分页语句
     *
     * @param sql
     * @param order
     * @param pager
     */
    public static String generatePageMySql(String sql, String order, Pager pager) {
        if (order == null) {
            order = "";
        }
        return new StringBuffer().append(sql).append(" " + order).append(" limit ").append(pager.getPageStart()).append(",").append(pager.getPageSize()).toString();
    }

    /**
     * 生成Oracle分页语句
     *
     * @param sql
     * @param order
     * @param pager
     */
    public static String generatePageOracle(String sql, String table, String order, Pager pager) {
        if (order == null) {
            order = "";
        }
        return new StringBuffer().append("select a.* from (select rownum rn, t.* from ").append(table).append(" t")
                .append(sql).append(order).append(") a where a.rn >= ").append(pager.getPageStart()).append(" and a.rn < ").append(pager.getPageEnd()).toString();
    }


    /**
     * 生成查询语句
     *
     * @param daoMethod
     * @param selectSql
     * @param whereSql
     */
    public static String generateSelectSql(TableInfo daoMethod, String selectSql, String whereSql, String orderSql) {
        String sql = "";
        if (selectSql.trim().equals("")) {
            sql = "select " + daoMethod.getColum() + " from " + daoMethod.getTableName();
        }
        if (!whereSql.trim().equals("")) {
            sql += " where " + whereSql;
        }
        if (!orderSql.trim().equals("")) {
            sql += " " + orderSql;
        }
        return sql;
    }

    /**
     * 生成查询语句
     *
     * @param daoMethod
     * @param whereSql
     */
    public static String generateCountSql(TableInfo daoMethod, String whereSql) {
        String sql = "select count(*) from " + daoMethod.getTableName();
        if (!"".equals(whereSql)) {
            sql += " where " + whereSql;
        }
        return sql;
    }

    /**
     * 生成主键查询语句
     *
     * @param daoMethod
     */
    public static String generateSelectPKSql(TableInfo daoMethod) {
        if (daoMethod.getPkName() == null || "".equals(daoMethod.getPkName())) {
            throw new RuntimeException(daoMethod.getTableName() + " 未设置[主键]不能使用主键查询！请使用自定义条件查询！");
        }
        return "select " + daoMethod.getColum() + " from " + daoMethod.getTableName() + " where " + daoMethod.getPkName() + "=? ";
    }


    /**
     * 设置参数
     *
     * @param pstm
     * @param params
     * @throws SQLException
     */
    public static void setParams(PreparedStatement pstm, Object... params) throws SQLException {
        if (params != null) {
            for (int x = 1; x <= params.length; x++) {
                pstm.setObject(x, params[x - 1]);
            }
        }
    }

    /**
     * 释放数据库连接资源
     *
     * @param stmt
     * @param rs
     */
    public static void close(Statement stmt, ResultSet rs, Connection conn) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (Exception ex) {
            logger.debug(ex.getMessage(), ex);
        }
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (Exception ex) {
            logger.debug(ex.getMessage(), ex);
        }
//        try {
//            if (conn != null && !conn.isClosed() && conn.getAutoCommit()) {
//                 不自动关闭连接
//                conn.close();
//            }
//        } catch (Exception ex) {
//            logger.debug(ex.getMessage(), ex);
//        }
    }


    /**
     * 打印sql语句
     *
     * @param statement
     */
    public static void showSql(Statement statement) {
        String sql = "";
        if (statement instanceof DruidPooledPreparedStatement) {
            sql = ((DruidPooledPreparedStatement) statement).getKey().getSql();
        } else {
            sql = statement.toString();
            sql = sql.substring(sql.indexOf(":") + 1, sql.length());
        }
        logger.debug(sql);
    }

    public static <E> E RecordToEntity(Class cls, TableInfo tableInfo, Record record) throws Exception {
        if (record.size() == 0) {
            return null;
        }
        Object obj = cls.newInstance();
        for (String colum : tableInfo.getColums()) {
            TableFiled tableFiled = tableInfo.getDaoFiled(colum);
            Object value = record.get(colum.toLowerCase());
            if (value != null) {
                String methodName = tableFiled.get_setMethodName();
                ClassTool.invokeMethod(cls.getDeclaredField(tableFiled.getFiledName()), methodName, cls, obj, value);
            }
        }
        return (E) obj;
    }


    public static <T> T MapToEntity(Class cls, TableInfo tableInfo, HashMap<String, Object> data) throws Exception {
        Object obj = cls.newInstance();
        for (String colum : tableInfo.getColums()) {
            TableFiled tableFiled = tableInfo.getDaoFiled(colum);
            Object value = data.get(colum);
            if (value != null) {
                String methodName = tableFiled.get_setMethodName();
                ClassTool.invokeMethod(cls.getDeclaredField(tableFiled.getFiledName()), methodName, cls, obj, value);
            }
        }
        return (T) obj;
    }


    /**
     * 执行查询，并取得查询单个结果，将结果装入HashMap中
     *
     * @return
     */
    public static Record getRecord(ResultSet rs) throws Exception {
        Record data = new Record();
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        int max = 0;
        while (rs.next() && max == 0) {
            for (int i = 1; i <= columnCount; i++) {
                data.put(String.valueOf(meta.getColumnName(i)).toLowerCase(), rs.getObject(i));
            }
            max++;
        }
        return data;
    }

    /**
     * 执行查询，并取得查询单个结果，将结果装入HashMap中
     *
     * @return
     */
    public static List<Record> getRecords(ResultSet rs) throws Exception {
        List<Record> records = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        while (rs.next()) {
            Record record = new Record();
            for (int i = 1; i <= columnCount; i++) {
                record.put(String.valueOf(meta.getColumnName(i)).toLowerCase(), rs.getObject(i));
            }
            records.add(record);
        }
        return records;
    }


    public static String arryToString(String[] ary) {
        StringBuffer sb = new StringBuffer();
        int len = ary.length;
        for (int i = 0; i < len; i++) {
            if (i != len - 1) {
                sb.append(ary[i] + ",");
            } else {
                sb.append(ary[i]);
            }
        }
        return sb.toString();
    }

    public static String clobToString(Clob cl) throws Exception {
        String res = "";
        Reader is = null;
        if (cl == null) {
            return "";
        }
        try {
            is = cl.getCharacterStream();// 得到流
            BufferedReader br = new BufferedReader(is);
            String s = br.readLine();
            StringBuffer sb = new StringBuffer();
            while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
                sb.append(s);
                s = br.readLine();
                if (s != null) {
                    sb.append("\r\n");
                }
            }
            res = sb.toString();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            is.close();
        }
    }


}
