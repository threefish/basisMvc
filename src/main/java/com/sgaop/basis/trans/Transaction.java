package com.sgaop.basis.trans;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/7 0007
 * To change this template use File | Settings | File Templates.
 * 事务上下文
 */
public abstract class Transaction {

    private int level;

    /**
     * 创建事务上下文
     */
    protected Transaction() {
    }

    /**
     * 获取事务等级, 可能的值为0,1,2,4,8
     *
     * @return 当前的事务等级
     */
    public int getLevel() {
        return level;
    }

    /**
     * 设置事务等级
     *
     * @param level 事务等级
     */
    public void setLevel(int level) {
        if (this.level <= 0)
            this.level = level;
    }

    /**
     * 层次id
     *
     * @return 当前事物的层次id
     */
    public abstract long getId();

    /**
     * 提交
     */
    protected abstract void commit();

    /**
     * 回滚
     */
    protected abstract void rollback();

    /**
     * 获取该连接池所关联的连接, 同一个DataSource在一个事务中使用同一个连接
     *
     * @param dataSource 数据源
     * @return 数据库连接
     * @throws SQLException 获取连接失败或其他异常
     */
    public abstract Connection getConnection(DataSource dataSource) throws SQLException;

    /**
     * 关闭事务,清理现场
     */
    public abstract void close();

}

