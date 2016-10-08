package com.sgaop.test;

import com.google.gson.Gson;
import com.sgaop.basis.dao.impl.DaoImpl;
import com.sgaop.bean.tbUser;
import com.sgaop.basis.dao.DBConnPool;
import com.sgaop.basis.dao.Dao;
import com.sgaop.basis.dao.Pager;
import com.sgaop.basis.scanner.ClassScanner;
import com.sgaop.basis.scanner.ProperScanner;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/9/12 0012
 * To change this template use File | Settings | File Templates.
 * <p>
 * 写这个测试时并没有按照顺序来所以， 最好单个执行，全部执行时，有些数据或许因为被清除掉了而导致报错
 */
public class TestAllDao {
    private static Dao dao;
    private static tbUser tb = new tbUser();
    private static ArrayList<Object> list = new ArrayList<Object>();


    @BeforeClass
    public static void setUp() throws SQLException {
        //加载全局配置文件
        ProperScanner.init();
        ClassScanner.ScannerAllClass();

        dao = new DaoImpl();

        /**
         * 设置临时对象1
         */
        tb.setCt(new java.sql.Timestamp(new Date().getTime()));
        tb.setId(1);
        tb.setIp("127.0.0.1");
        tb.setLock(true);
        tb.setTkx("的撒旦");
        tb.setUa("的绝对是");
        tb.setOs("的绝对是1");

        /**
         * 设置临时对象2
         */
        tbUser tb2 = new tbUser();
        tb2.setCt(new java.sql.Timestamp(new Date().getTime()));
        tb2.setId(2);
        tb2.setIp("127.0.0.2");
        tb2.setLock(true);
        tb2.setUa("的绝对是2");
        tb2.setOs("的绝对是2");

        list.add(tb);
        list.add(tb2);
    }

    /**
     * 批量查询对象，没有条件（select 字段名 from 表名）
     */
    @Test
    public void queryList() {
        dao.queryList(tbUser.class, null, "");
    }

    /**
     * 批量查询对象，没有条件（select 字段名 from 表名）
     */
    @Test
    public void queryListPager() {
        System.out.println(new Gson().toJson(dao.queryList(tbUser.class, new Pager(2, 5), "order by id desc")));
    }


    /**
     * 批量查询对象，有条件（select 字段名 from 表名 where 条件=查询值）
     */
    @Test
    public void queryList2() {
        dao.queryCndList(tbUser.class, new Pager(2, 5), "tk=?", "十分士大夫");
    }

    /**
     * 查询单个对象，有条件（select 字段名 from 表名 where 条件=查询值）
     */
    @Test
    public void querySinge() {
        try {
            dao.querySinge("id=?", tbUser.class, 10);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询单个对象，按主键条件（select 字段名 from 表名 where 主键=查询值）
     */
    @Test
    public void querySingePK() {
        dao.querySingePK(tbUser.class, 10);
    }

    /**
     * 插入一个实体对象
     */
    @Test
    public void insertData() {
        try {
            dao.insert(tbUser.class, tb);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量插入实体对象
     */
    @Test
    public void insertListData() throws SQLException {
        dao.insert(tbUser.class, list);
    }

    /**
     * 批量更新实体对象
     */
    @Test
    public void updateListData() throws SQLException {
        int[] x = dao.update(tbUser.class, list);
        System.out.println(new Gson().toJson(x));
    }

    /**
     * 批量删除实体对象
     */
    @Test
    public void delectListData() {
        int[] x = dao.delect(tbUser.class, list);
        System.out.println(new Gson().toJson(x));
    }


    /**
     * 根据sql查询
     */
    @Test
    public void queryBySql() {
        System.out.println(new Gson().toJson(dao.queryList("select * from tb_user where id=?", 9)));
    }

    /**
     * 根据sql查询
     */
    @Test
    public void queryBySql2() throws SQLException {
        System.out.println(new Gson().toJson(dao.querySinge("select * from tb_user where id=?", 9)));
    }


    /**
     * 使用事务
     */
    @Test
    public void trans()  {
        dao.begin(false);
        try {
            dao.insert(tbUser.class, list);
            dao.insert(tbUser.class, list);
            dao.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            dao.rollback();
        }
    }

}
