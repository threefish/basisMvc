package com.sgaop.basis.dao.bean;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/6/24 0024
 * To change this template use File | Settings | File Templates.
 */
public class TableInfo {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 主键组
     */
    private String[] pkName;

    /**
     * 表列字段
     */
    private List<String> colums = new ArrayList<String>();

    /**
     * 实体类字段与表列的关系
     */
    private LinkedHashMap<String, TableFiled> daoFileds = new LinkedHashMap<String, TableFiled>();


    public void addDaoFiled(String colums, TableFiled tableFiled) {
        daoFileds.put(colums, tableFiled);
    }

    public TableFiled getDaoFiled(String colum) {
        return daoFileds.get(colum);
    }

    public String[] getPkName() {
        return pkName;
    }

    public void setPkName(String[] pkName) {
        this.pkName = pkName;
    }

    public String getTableName() {
        return tableName;
    }


    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColums() {
        return colums;
    }

    public String getColum() {
        String columsStr = "";
        for (String colum : colums) {
            if (columsStr.equals("")) {
                columsStr = colum;
            } else {
                columsStr += "," + colum;
            }
        }
        return columsStr;
    }

    public void addColums(String colum) {
        colums.add(colum);
    }
}
