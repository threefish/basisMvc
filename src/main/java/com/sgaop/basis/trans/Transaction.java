package com.sgaop.basis.trans;


import com.sgaop.basis.dao.impl.JdbcAccessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/1 0001
 * To change this template use File | Settings | File Templates.
 */
public class Transaction {

    static ThreadLocal<Boolean> trans = new ThreadLocal<>();

    static ThreadLocal<Integer> levels = new ThreadLocal<>();

    static ThreadLocal<HashMap<String, ConnectionWarper>> connLocal = new ThreadLocal<>();


    /**
     * 这个类提供的均为静态方法.
     */
    Transaction() {
    }

    /**
     * @return 当前线程的事务，如果没有事务，返回 false
     */
    public static Boolean beanginTrans() {
        if (trans.get() == null) {
            return false;
        } else {
            return true;
        }
    }


    public static void beanginTrans(int level) {
        trans.set(true);
        levels.set(level);
    }

    public static int getLevel() {
        return levels.get();
    }


    public static void commit() throws SQLException {
        for (Map.Entry connInfo : connLocal.get().entrySet()) {
            ConnectionWarper cw = (ConnectionWarper) connInfo.getValue();
            if (cw.isTrans) {
                cw.jdbcAccessor.commit();
            }
        }
    }

    public static void rollBack() throws SQLException {
        for (Map.Entry connInfo : connLocal.get().entrySet()) {
            ConnectionWarper cw = (ConnectionWarper) connInfo.getValue();
            if (cw.isTrans) {
                cw.jdbcAccessor.rollback();
            }
        }
    }

    public static void resumConn() throws SQLException {
        for (Map.Entry connInfo : connLocal.get().entrySet()) {
            ConnectionWarper cw = (ConnectionWarper) connInfo.getValue();
            if (cw.isTrans) {
                cw.jdbcAccessor.resumConn(cw.oldLevel);
            }
        }
    }

    /**
     * 销毁
     */
    public static void destroy() {
        trans.remove();
        connLocal.remove();
    }


    /**
     * 添加连接
     *
     * @param conn
     */
    public static void setConn(Connection conn, int oldLevel, int newLevel, JdbcAccessor jdbcAccessor) {
        String key = conn.toString();
        HashMap<String, ConnectionWarper> sets = connLocal.get();
        if (sets == null) {
            sets = new HashMap<>();
        }
        if (!sets.containsKey(key)) {
            sets.put(key, new ConnectionWarper(key, true, newLevel, oldLevel, jdbcAccessor));
            connLocal.set(sets);
        }
    }


    /**
     * 取得事务连接
     *
     * @param conn
     */
    public static ConnectionWarper getConn(Connection conn) {
        HashMap<String, ConnectionWarper> sets = connLocal.get();
        if (sets != null && sets.containsKey(conn.toString()))
            return sets.get(conn.toString());
        return null;
    }


    public static int size() {
        HashMap<String, ConnectionWarper> sets = connLocal.get();
        if (sets != null)
            return sets.size();
        return 0;
    }


    public static void destory() {
        connLocal.remove();
    }


    private static class ConnectionWarper {
        String connkey;
        boolean isTrans;
        int newLevel;
        int oldLevel;
        JdbcAccessor jdbcAccessor;

        public ConnectionWarper(String connkey, boolean isTrans, int newLevel, int oldLevel, JdbcAccessor jdbcAccessor) {
            this.connkey = connkey;
            this.isTrans = isTrans;
            this.newLevel = newLevel;
            this.oldLevel = oldLevel;
            this.jdbcAccessor = jdbcAccessor;
        }
    }
}
