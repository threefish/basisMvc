package com.sgaop.basis.trans;

import com.sgaop.basis.log.Logs;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class Trans {

    private static final Logger log = Logs.get();


    /**
     * 这个类提供的均为静态方法.
     */
    Trans() {
    }

    static ThreadLocal<Transaction> trans = new ThreadLocal<Transaction>();
    static ThreadLocal<Integer> count = new ThreadLocal<Integer>();

    /**
     * 事务debug开关
     */
    public static boolean DEBUG = false;

    /**
     * @return 当前线程的事务，如果没有事务，返回 null
     */
    public static Transaction get() {
        return trans.get();
    }


    static void _begain(int level) throws Exception {
        Transaction tn = trans.get();
        if (null == tn) {
            tn = new BasisTransaction();
            tn.setLevel(level);
            trans.set(tn);
            count.set(0);
            if (DEBUG)
                log.debug(String.format("Start New Transaction id=%d, level=%d", tn.getId(), level));
        } else {
            if (DEBUG)
                log.debug(String.format("Attach Transaction    id=%d, level=%d", tn.getId(), level));
        }
        int tCount = count.get() + 1;
        count.set(tCount);
    }

    static void _commit() throws Exception {
        count.set(count.get() - 1);
        Transaction tn = trans.get();
        if (count.get() == 0) {
            if (DEBUG)
                log.debug("Transaction Commit id=" + tn.getId());
            tn.commit();
        } else {
            if (DEBUG)
                log.debug(String.format("Transaction delay Commit id=%d, count=%d", tn.getId(), count.get()));
        }
    }

    static void _depose() {
        if (count.get() == 0)
            try {
                if (DEBUG)
                    log.debug(String.format("Transaction depose id=%d, count=%s", trans.get().getId(), count.get()));
                trans.get().close();
            } catch (Throwable e) {
                throw e;
            } finally {
                trans.set(null);
            }
    }

    static void _rollback(Integer num) {
        count.set(num);
        if (count.get() == 0) {
            if (DEBUG)
                log.debug(String.format("Transaction rollback id=%s, count=%s", trans.get().getId(), num));
            trans.get().rollback();
        } else {
            if (DEBUG)
                log.debug(String.format("Transaction delay rollback id=%s, count=%s", trans.get().getId(), num));
        }
    }

    /**
     * 是否在事务中
     *
     * @return 真, 如果在不事务中
     */
    public static boolean isTransactionNone() {
        Transaction t = trans.get();
        return null == t || t.getLevel() == Connection.TRANSACTION_NONE;
    }

    /* ===========================下面暴露几个方法给喜欢 try...catch...finally 的人 ===== */

    /**
     * 开始一个事务，级别为 TRANSACTION_READ_COMMITTED
     * <p>
     * 你需要手工用 try...catch...finally 来保证你提交和关闭这个事务
     *
     * @throws Exception
     */
    public static void begin() throws Exception {
        Trans._begain(Connection.TRANSACTION_READ_COMMITTED);
    }

    /**
     * 开始一个指定事务
     * <p>
     * 你需要手工用 try...catch...finally 来保证你提交和关闭这个事务
     *
     * @param level 指定级别
     * @throws Exception
     */
    public static void begin(int level) throws Exception {
        Trans._begain(level);
    }

    /**
     * 提交事务，执行它前，你必需保证你已经手工开始了一个事务
     *
     * @throws Exception
     */
    public static void commit() throws Exception {
        Trans._commit();
    }

    /**
     * 回滚事务，执行它前，你必需保证你已经手工开始了一个事务
     *
     * @throws Exception
     */
    public static void rollback() throws Exception {
        Integer c = Trans.count.get();
        if (c == null)
            c = Integer.valueOf(0);
        else if (c > 0)
            c--;
        Trans._rollback(c);
    }

    /**
     * 关闭事务，执行它前，你必需保证你已经手工开始了一个事务
     *
     * @throws Exception
     */
    public static void close() throws Exception {
        Trans._depose();
    }

    /**
     * 如果在事务中,则返回事务的连接,否则直接从数据源取一个新的连接
     */
    public static Connection getConnectionAuto(DataSource ds) throws SQLException {
        if (get() == null)
            return ds.getConnection();
        else
            return get().getConnection(ds);
    }

    /**
     * 自动判断是否关闭当前连接
     *
     * @param conn 数据库连接
     */
    public static void closeConnectionAuto(Connection conn) throws SQLException {
        if (get() == null && null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw e;
            }
        }
    }

    /**
     * 强制清理事务上下文
     *
     * @param rollbackOrCommit 检测到未闭合的事务时回滚还是提交，true为回滚，false为提交。
     */
    public static void clear(boolean rollbackOrCommit) {
        Integer c = Trans.count.get();
        if (c == null)
            return;
        if (c > 0) {
            for (int i = 0; i < c; i++) {
                try {
                    if (rollbackOrCommit)
                        Trans.rollback();
                    else
                        Trans.commit();
                    Trans.close();
                } catch (Exception e) {
                }
            }
        }
        Trans.count.set(null);
        Transaction t = get();
        if (t != null)
            t.close();
        Trans.trans.set(null);
    }
}