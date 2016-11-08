package com.sgaop.basis.trans;


import com.sgaop.basis.error.ComboException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/1 0001
 * To change this template use File | Settings | File Templates.
 */
public class BasisTransaction extends Transaction {

    private static AtomicLong TransIdMaker = new AtomicLong();

    private List<ConnInfo> list;

    private long id;


    private static class ConnInfo {
        ConnInfo(DataSource ds, Connection conn, int level, boolean restoreAutoCommit) throws SQLException {
            this.ds = ds;
            this.conn = conn;
            this.oldLevel = conn.getTransactionIsolation();
            if (this.oldLevel != level) {
                conn.setTransactionIsolation(level);
                this.restoreIsoLevel = true;
            }
            this.restoreAutoCommit = restoreAutoCommit;
        }

        DataSource ds;
        Connection conn;
        int oldLevel;
        boolean restoreIsoLevel;
        boolean restoreAutoCommit;
    }


    /**
     * 新建上下文并初始化自身的层次数据
     */
    public BasisTransaction() {
        list = new ArrayList<ConnInfo>();
        id = TransIdMaker.getAndIncrement();
    }

    /**
     * 提交事务
     */
    protected void commit() {
        ComboException ce = new ComboException();
        for (ConnInfo cInfo : list) {
            try {
                // 提交事务
                cInfo.conn.commit();
                // 恢复旧的事务级别
                if (cInfo.conn.getTransactionIsolation() != cInfo.oldLevel)
                    cInfo.conn.setTransactionIsolation(cInfo.oldLevel);
            } catch (SQLException e) {
                ce.add(e);
            }
        }
        // 如果有一个数据源提交时发生异常，抛出
        if (null != ce.getCause()) {
            throw ce;
        }
    }

    /**
     * 从数据源获取连接
     */
    @Override
    public Connection getConnection(DataSource dataSource) throws SQLException {
        for (ConnInfo p : list)
            if (p.ds == dataSource)
                return p.conn;
        Connection conn = dataSource.getConnection();
        boolean restoreAutoCommit = false;
        if (conn.getAutoCommit()) {
            conn.setAutoCommit(false);
            restoreAutoCommit = true;
        }
        list.add(new ConnInfo(dataSource, conn, getLevel(), restoreAutoCommit));
        return conn;
    }

    /**
     * 层次id
     */
    public long getId() {
        return id;
    }

    /**
     * 关闭事务,清理现场
     */
    @Override
    public void close() {
        ComboException ce = new ComboException();
        for (ConnInfo cInfo : list) {
            try {
                // 试图恢复旧的事务级别
                if (!cInfo.conn.isClosed()) {
                    if (cInfo.restoreIsoLevel)
                        cInfo.conn.setTransactionIsolation(cInfo.oldLevel);
                    if (cInfo.restoreAutoCommit)
                        cInfo.conn.setAutoCommit(true);
                }
            } catch (Throwable e) {
            } finally {
                try {
                    cInfo.conn.close();
                } catch (Exception e) {
                    ce.add(e);
                }
            }
        }
        // 清除数据源记录
        list.clear();
    }

    /**
     * 执行回滚操作
     */
    @Override
    protected void rollback() {
        for (ConnInfo cInfo : list) {
            try {
                cInfo.conn.rollback();
            } catch (Throwable e) {
            }
        }
    }
}
