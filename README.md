# **basis  MVC**
- 基于servlet与java注解的Web MVC 快速开发框架
- _**basis** 意思： n. 基础；底部；主要成分；基本原则或原理_

## 基于Java注解和servlet3.0+实现通过注解方式访问的web MVC框架
# 示例项目[basisMvcSample](https://github.com/threefish/basisMvcSample "示例项目")
###  规范了包名，所以重新创建了项目进行提交分享[点击这里可以查看原始项目的更新日志](https://github.com/threefish/basisMvcSample "示例项目")
### 已实现
#### 参数类

    - 实现缓存扫描注解action
    - 实现参数自动绑定
    - 参数与javabean自动绑定
    - 文件上传自动绑定

#### 文件类
    - 实现文件过滤器
    - 上传文件格式控制
    - 上传文件大小限制

#### 基础视图控制器
    - jsp页面返回
    - json对象返回
    - 404、500页面
    - 文件下载
#### 用户自定义视图控制器
     - 用户自定义视图（如freemaker）

#### dao功能  简易的orm实现
    - table-List<对象>
    - table-对象
    - table-Map
    - table-List<Map>
    - 对象的增删改查

- @Setup 启动执行任务
- 绑定durid连接池

### 正在添加中
- aop的实现
- ioc的实现
- dao事务控制
- 暂未想到的功能.....


### 规范
    - WEB-INF下的jsp必须通过action访问
    - 静态资源文件目录需要配置
    - 配置文件相关可以查看jar包中的 resources 目录以作规范

### 示例CODE
```java
package com.sgaop.web.action;

import com.google.gson.Gson;
import com.sgaop.web.frame.server.dao.DBConnPool;
import com.sgaop.web.frame.server.mvc.Mvcs;
import com.sgaop.web.frame.server.mvc.annotation.*;
import com.sgaop.web.frame.server.mvc.upload.TempFile;
import com.sgaop.web.frame.server.mvc.AjaxResult;
import com.sgaop.web.frame.server.util.IoTool;
import com.sgaop.bean.TestbuildBean;

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

```