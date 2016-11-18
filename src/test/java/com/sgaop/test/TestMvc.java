package com.sgaop.test;

import com.google.gson.Gson;
import com.sgaop.basis.annotation.Inject;
import com.sgaop.basis.cache.MvcsManager;
import com.sgaop.basis.dao.Dao;
import com.sgaop.basis.scanner.ClassScanner;
import com.sgaop.basis.scanner.PropertiesHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/10/8 0008
 * To change this template use File | Settings | File Templates.
 */
public class TestMvc {

    @Inject
    private Dao dao;

    @BeforeClass
    public static void setUp() throws SQLException {
        //加载全局配置文件
        PropertiesHelper.init();
        ClassScanner.init();
    }

    @Test
    public void showWebMvcMap() {
        System.out.println("UrlMapping:" + new Gson().toJson(MvcsManager.urlMappingList()));
    }

}
