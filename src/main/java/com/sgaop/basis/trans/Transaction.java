package com.sgaop.basis.trans;


import com.sgaop.basis.dao.impl.JdbcAccessor;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/1 0001
 * To change this template use File | Settings | File Templates.
 */
public class Transaction {

    static ThreadLocal<Boolean> trans = new ThreadLocal<>();
    static ThreadLocal<TransInfo> level = new ThreadLocal<>();

    /**
     * 这个类提供的均为静态方法.
     */
    Transaction() {
    }

    /**
     * @return 当前线程的事务，如果没有事务，返回 false
     */
    public static Boolean get() {
        if (trans.get() == null) {
            return false;
        } else {
            return true;
        }
    }


    public static TransInfo getLevel() {
        return level.get();
    }

    public static void setLevel(int levels) {
        level.set(new TransInfo(levels));
    }

    public static void set(boolean flag) {
        trans.set(flag);
    }

    /**
     * 销毁
     */
    public static void destroy() {
        trans.remove();
        level.remove();
    }


    public static class TransInfo {
        int newLevel;
        int oldLevel;
        JdbcAccessor jdbcAccessor;

        public TransInfo(int newLevel) {
            this.newLevel = newLevel;
        }

        public JdbcAccessor getJdbcAccessor() {
            return jdbcAccessor;
        }

        public void setJdbcAccessor(JdbcAccessor jdbcAccessor) {
            this.jdbcAccessor = jdbcAccessor;
        }

        public int getNewLevel() {
            return newLevel;
        }

        public void setNewLevel(int newLevel) {
            this.newLevel = newLevel;
        }

        public int getOldLevel() {
            return oldLevel;
        }

        public void setOldLevel(int oldLevel) {
            this.oldLevel = oldLevel;
        }
    }
}
