package com.sgaop.basis.dao.impl;

import com.sgaop.basis.dao.bean.TableFiled;
import com.sgaop.basis.dao.bean.TableInfo;
import com.sgaop.basis.util.ClassTool;
import com.sgaop.basis.util.Logs;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/23 0023
 * To change this template use File | Settings | File Templates.
 */
public class JdbcBuilder {

    private static final Logger log = Logs.get();

    public JdbcBuilder(TableInfo tableInfo, Class klass, Object bean) {
        this.tableInfo = tableInfo;
        this.klass = klass;
        this.bean = bean;
    }

    private TableInfo tableInfo;

    private Class klass;

    private Object bean;


    /**
     * 取得基本查询语句
     *
     * @return
     */
    public String getSelectSql() {
        return "select " + tableInfo.getColum() + " from " + tableInfo.getTableName();
    }


    /**
     * 取得插入语句
     *
     * @return
     */
    public String getInsetSql() {
        StringBuffer sb = new StringBuffer("insert into " + tableInfo.getTableName() + "(");
        StringBuffer sb2 = new StringBuffer(" values(");
        for (String colum : tableInfo.getColums()) {
            sb.append(colum + ",");
            sb2.append("?,");
        }
        sb.delete(sb.length() - 1, sb.length());
        sb2.delete(sb2.length() - 1, sb2.length());
        sb.append(")");
        sb2.append(")");
        sb.append(sb2.toString());
        return sb.toString();
    }


    /**
     * 取得刪除语句
     *
     * @return
     */
    public String getDelPkSql() {
        StringBuffer sb = new StringBuffer("delete from " + tableInfo.getTableName());
        if(tableInfo.getPkName()!=null){
            sb.append(" where ");
            String[] pks = tableInfo.getPkName();
            if (pks.length == 0) {

            }
            int i = 0;
            for (String pk : pks) {
                if (i == 0) {
                    sb.append(pk + "=? ");
                } else {
                    sb.append("and " + pk + "=? ");
                }
                i++;
            }
        }
        return sb.toString();
    }


    /**
     * 取得更新语句
     *
     * @return
     */
    public String getUpdateSql() {
        StringBuffer sb = new StringBuffer("update " + tableInfo.getTableName() + " set ");
        for (String colum : tableInfo.getColums()) {
            sb.append(colum + "=?,");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    /**
     * 取得不含主键更新语句
     *
     * @return
     */
    public String getUpdateNoPkSql() {
        StringBuffer sb = new StringBuffer("update " + tableInfo.getTableName() + " set ");
        for (String colum : tableInfo.getColums()) {
            boolean b = true;
            if (tableInfo.getPkName() != null) {
                sw:
                for (String pk : tableInfo.getPkName()) {
                    if (colum.equals(pk)) {
                        b = false;
                        break sw;
                    }
                }
            }
            if (b) {
                sb.append(colum + "=?,");
            }
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }


    /**
     * 取得更新语句
     *
     * @return
     */
    public String getUpdatePkSql() {
        StringBuffer sb = new StringBuffer("update " + tableInfo.getTableName() + " set ");
        for (String colum : tableInfo.getColums()) {
            sb.append(colum + "=?,");
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append(" where ");
        if (tableInfo.getPkName() == null) {
            log.error("未设置主键");
            throw new RuntimeException("未设置主键");
        } else {
            String pks = "";
            for (String str : tableInfo.getPkName()) {
                if ("".equals(pks)) {
                    pks = str + "=?";
                } else {
                    pks += " and " + str + "=?";
                }
            }
            sb.append(pks);
        }
        return sb.toString();
    }


    /**
     * 取得单条数据值
     *
     * @return
     */
    public LinkedList<Object> getOneVals() {
        return getVals(bean);
    }


    /**
     * 取得单条数据值
     *
     * @return
     */
    public LinkedList<Object> getOnePKVals() {
        return getPkVals(bean);
    }


    /**
     * 取得主键数据值
     *
     * @return
     */
    public LinkedList<Object> getNoPkVals() {
        LinkedList<Object> vals = new LinkedList<>();
        for (String colum : tableInfo.getColums()) {
            boolean b = true;
            if (tableInfo.getPkName() != null) {
                sw:
                for (String pk : tableInfo.getPkName()) {
                    if (colum.equals(pk)) {
                        b = false;
                        break sw;
                    }
                }
            }
            if (b) {
                TableFiled tableFiled = tableInfo.getDaoFiled(colum);
                String methodName = tableFiled.get_getMethodName();
                Object value = ClassTool.invokeGetMethod(klass, bean, methodName);
                vals.add(value);
            }
        }
        return vals;
    }

    /**
     * 取得单条数据主键值
     *
     * @return
     */
    private LinkedList<Object> getPkVals(Object obj) {
        LinkedList<Object> vals = new LinkedList<>();
        for (String colum : tableInfo.getPkName()) {
            TableFiled tableFiled = tableInfo.getDaoFiled(colum);
            String methodName = tableFiled.get_getMethodName();
            Object value = ClassTool.invokeGetMethod(klass, obj, methodName);
            vals.add(value);
        }
        return vals;
    }


    /**
     * 取得多条数据主键值
     *
     * @return
     */
    public LinkedList<LinkedList<Object>> getManyPkVals() {
        LinkedList<LinkedList<Object>> valslist = new LinkedList<>();
        for (Object obj : (List) bean) {
            valslist.add(getPkVals(obj));
        }
        return valslist;
    }


    private LinkedList<Object> getVals(Object obj) {
        LinkedList<Object> vals = new LinkedList<>();
        for (String colum : tableInfo.getColums()) {
            TableFiled tableFiled = tableInfo.getDaoFiled(colum);
            String methodName = tableFiled.get_getMethodName();
            Object value = ClassTool.invokeGetMethod(klass, obj, methodName);
            vals.add(value);
        }
        return vals;
    }

    /**
     * 取得多条数据值
     *
     * @return
     */
    public LinkedList<LinkedList<Object>> getManyVals() {
        LinkedList<LinkedList<Object>> valslist = new LinkedList<>();
        for (Object obj : (List) bean) {
            valslist.add(getVals(obj));
        }
        return valslist;
    }

}
