package com.sgaop.web.action;

import com.google.gson.Gson;
import com.sgaop.bean.TestbuildBean;
import com.sgaop.web.frame.server.dao.DBConnPool;
import com.sgaop.web.frame.server.mvc.AjaxResult;
import com.sgaop.web.frame.server.mvc.Mvcs;
import com.sgaop.web.frame.server.mvc.annotation.*;
import com.sgaop.web.frame.server.mvc.upload.TempFile;
import com.sgaop.web.frame.server.util.IoTool;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/8 0008
 * To change this template use File | Settings | File Templates.
 */
@WebController("/mainController")
public class MainController {

    /**
     * 返回freemarker自定义视图
     *
     * @return
     */
    @OK("freemarker:TestFreeMarker.ftl")
    @GET
    @Path("/freemarker")
    public Map freemarkerTest() {
        System.out.println("---freemarkerTest");
        Map data1 = new HashMap();
        data1.put("name", "张三");
        data1.put("age", 11);
        return data1;
    }

    /**
     * 参数绑定对象加单文件上传
     *
     * @param bean
     * @param docName
     * @return
     */
    @OK("json")
    @POST
    @Path("/buildBeanFile")
    public AjaxResult buildBeanFile(@Parameter("data>>") TestbuildBean bean, @Parameter("docName") TempFile docName) {
        System.out.println(new Gson().toJson(bean));
        try {
            if (docName != null) {
                System.out.println(docName.getName());
                IoTool.writeFile(docName.getInputStream(), "d:\\temp\\" + docName.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AjaxResult(true, "呵呵呵", bean);
    }

    /**
     * 参数绑定对象加同名文件批量上传
     *
     * @param bean
     * @param docName
     * @return
     */
    @OK("json")
    @POST
    @Path("/buildBeanFiles")
    public AjaxResult buildBeanFiles(@Parameter("data>>") TestbuildBean bean, @Parameter("docName") TempFile[] docName) {
        System.out.println(new Gson().toJson(bean));
        for (TempFile file : docName) {
            System.out.println(file.getName());
            System.out.println(file.getContentType());
        }
        return new AjaxResult(true, "批量文件上传", bean);
    }

    /**
     * 参数绑定对象
     *
     * @param bean
     * @return
     * @throws SQLException
     */
    @OK("json")
    @POST
    @Path("/buildBean")
    public AjaxResult buildBean(@Parameter("data>>") TestbuildBean bean) throws SQLException {
        System.out.println(new Gson().toJson(bean));
        Connection connection = DBConnPool.getDataSource().getConnection();
        System.out.println(connection);
        return new AjaxResult(true, "呵呵呵", bean);
    }

    /**
     * 文件下载
     *
     * @return
     */
    @OK("file")
    @GET
    @Path("/dowload")
    public File dowloadFile() {
        return new File("D:\\TEMP\\模版说明.docx");
    }


    //@OK("rd:testpage.jsp")//重定向
    //@OK("json")//返回JSON对象
    @OK("jsp:testpage.jsp")//返回jsp页面
    //@OK("fw:testpage.jsp")//转发
    @GET//请求方式
    @Path//默认使用方法名
    public AjaxResult index(
            @Parameter("id") int id,
            @Parameter("name") String name,
            @Parameter("age") int age,
            @Parameter("doubleNum") double doubleNum,
            @Parameter("flag") boolean flag,
            @Parameter("ids") String[] ids,
            HttpServletRequest request) {
        System.out.println(Mvcs.getReqMap().get("name").toString());
        System.out.println("----" + id + "----" + name + "----" + age);
        System.out.println("mian index");
        request.setAttribute("test", "测试request.setAttribute");
        return new AjaxResult(true, "呵呵呵", "json哦");
    }
}

