package com.sgaop.web.frame.server.dao;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/6/24 0024
 * To change this template use File | Settings | File Templates.
 */
public class TableFiled {

    /**
     * 数据库表列名称
     */
    private String columName;

    /**
     * 实体类字段名称
     */
    private String filedName;

    /**
     * 执行设置属性值的方法名
     */
    private String _setMethodName;

    /**
     * 取得属性值的方法名
     */
    private String _getMethodName;

    public String getColumName() {
        return columName;
    }

    public void setColumName(String columName) {
        this.columName = columName;
    }

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public String get_setMethodName() {
        return _setMethodName;
    }

    public void set_setMethodName(String _setMethodName) {
        this._setMethodName = _setMethodName;
    }

    public String get_getMethodName() {
        return _getMethodName;
    }

    public void set_getMethodName(String _getMethodName) {
        this._getMethodName = _getMethodName;
    }
}
