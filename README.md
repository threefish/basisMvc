# **basis  MVC**
- 基于servlet与java注解的Web MVC 快速开发框架
- _**basis** 意思： n. 基础；底部；主要成分；基本原则或原理_

## 代码可能不够好，重复造轮子是为了更加充分的了解框架底层实现原理，写这个这是一种尝试，一种突破自我的方式。欢迎提出建议！谢谢！

## 基于Java注解和servlet实现通过注解方式访问的web MVC框架

# 示例项目[basisEdp](https://github.com/threefish/basisEdp "示例项目")

###  规范了包名，所以重新创建了项目进行提交分享[点击这里可以查看更早前的更新日志](https://github.com/threefish/WebFrameWork "更早前的更新日志")

#### TODO 真实项目的开发  [basisEdp](https://github.com/threefish/basisEdp "真实项目")

### 现已实现以下功能
#### IOC、AOP、ORM、MVC
#### 集成quartz,beetl
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

#### 用户自定义视图模版引擎控制器
     - 顾名思义，就是可以由开发者自定义的视图模版引擎 （如freemaker，beetl，vm等等）

#### dao功能  简易的orm实现
    - table-List<对象>
    - table-对象
    - table-Map
    - table-List<Map>
    - 对象的增删改查
    - dao事务控制

- @Setup 启动执行任务
- 绑定durid连接池



### 规范
    - WEB-INF下的jsp必须通过action访问
    - 静态资源文件目录需要配置
    - 配置文件相关可以查看jar包中的 resources 目录以作规范
    
###webxml设置
```xml
  <!--basis框架过滤器-->
    <listener>
        <listener-class>com.sgaop.basis.web.ServletInitListener</listener-class>
    </listener>
    <filter>
        <filter-name>basisFilter</filter-name>
        <filter-class>com.sgaop.basis.web.BasisMvcWebFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>basisFilter</filter-name>
        <url-pattern>/*</url-pattern>
        <dispatcher>REQUEST</dispatcher>
        <!--内部转发需要设置 FORWARD-->
        <dispatcher>FORWARD</dispatcher>
        <dispatcher>INCLUDE</dispatcher>
    </filter-mapping>
```
### 注册数据源和自定义视图就这么简单
```java
 public void init(ServletContextEvent servletContextEvent) {
        ViewsRegister.registerView("freemarker", FreeMarkerView.class);
        ViewsRegister.registerView("beetl", BeetlView.class);
        //注册数据源1
        DaosRegister.registerDao("daoA", DaoImpl.class, getDsA());
        //注册数据源2
        DaosRegister.registerDao("daoB", DaoImpl.class, getDsB());
    }
```


### 示例CODE
```java
package com.sample.action;

import com.sample.entity.Topic;
import com.sgaop.basis.annotation.*;
import com.sgaop.basis.dao.Dao;
import com.sgaop.basis.mvc.AjaxResult;
import com.sgaop.basis.trans.TransAop;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/8 0008
 * To change this template use File | Settings | File Templates.
 *
 * @OK("rd:testpage.jsp")//重定向
 * @OK("json")//返回JSON对象
 * @OK("fw:testpage.jsp")//转发
 * @GET//请求方式
 * @Path//默认使用方法名
 * @Control 标识这是一个可访问的webAction
 */
@IocBean("MainAction")
@Action("/main")
public class MainAction {

    //注入数据源A
    @Inject("daoA")
    private Dao daoA;

    //注入数据源B
    @Inject("daoB")
    private Dao daoB;
    
    //注入配置文件
    @Inject("java:db.jdbcUrl")
    private String jdbcUrl;

    @Inject("java:db.password")
    private int password;

    @Inject("java:db.testOnBorrow")
    private boolean testOnBorrow;

//    @OK("beetl:index")
    @GET
    @Path("/index")
    @Aop({TransAop.READ_COMMITTED,"allAop"})
    public String index(HttpServletRequest request) throws SQLException {
        System.out.printf("当前访问indx方法{dbPass:%s,password：%d} \r\n", jdbcUrl, password);
        try {
            Topic tp = new Topic();
            tp.setContent("我了个艹A");
            daoA.insert(Topic.class, tp);

            Topic tp2 = new Topic();
            tp2.setId(5);
            tp2.setContent("我了个艹a+");

            daoA.delect(Topic.class,tp2);

            daoA.insert(Topic.class, tp2);
        } catch (Exception e) {
            throw e;
        }
        return "beetl:index";
    }

    @OK("beetl:index2")
    @GET
    @Path("/index2")
    public void index2(HttpServletRequest request) {
        System.out.println("index2");
    }

    @OK("jsp:testpage.jsp")
    @GET
    @Path("/testindex")
    public AjaxResult index(
            @Parameter("id") int id,
            @Parameter("name") String name,
            @Parameter("age") int age,
            @Parameter("doubleNum") double doubleNum,
            @Parameter("flag") boolean flag,
            @Parameter("ids") String[] ids,
            HttpServletRequest request) {
        System.out.println("----" + id + "----" + name + "----" + age);
        System.out.println("mian index");
        request.setAttribute("test", "测试request.setAttribute");
        return new AjaxResult(true, "呵呵呵", "json哦");
    }


    @OK("rd:testpage.jsp")
    @GET
    @Path("/testpage")
    public void testpage() {
        System.out.println("---testpage");
    }


    @OK("freemarker:TestFreeMarker")
    @GET
    @Path("/freemarker")
    public Map freemarkerTest() {
        System.out.println("---freemarkerTest");
        Map data1 = new HashMap();
        data1.put("name", "张三");
        data1.put("age", 11);
        return data1;
    }

    @OK("beetl:TestBeetl")
    @GET
    @Path("/beetl")
    public Map beetlTest() {
        System.out.println("---beetlTest");
        Map data1 = new HashMap();
        data1.put("name", "张三");
        data1.put("age", 11);
        List<Map> datalist=new ArrayList<Map>();
        for(int i=1;i<=9;i++){
            Map temp = new HashMap();
            temp.put("name", "张"+i);
            temp.put("age", (Math.random() * 100));
            datalist.add(temp);
        }
        data1.put("data", datalist);
        return data1;
    }

    @OK("beetl:TestBeetl2")
    @GET
    @Path("/beetl2")
    public User beetl2() {
        System.out.println("---beetlTest2");
        User user = new User();
        user.setAge(18);
        user.setName("李四");
        return user;
    }

    @OK("beetl:TestBeetl2")
    @GET
    @Path("/beetl3")
    public Map beetl3() {
        System.out.println("---beetlTest3");
        Map data1 = new HashMap();
        data1.put("name", "张三");
        data1.put("age", 11);
        return data1;
    }


    @OK("json")
    @POST
    @Path("/buildBeanFile")
    public AjaxResult buildBeanFile(@Parameter("data>>") User bean, @Parameter("docName") TempFile docName) {
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

    @OK("json")
    @POST
    @Path("/buildBeanFiles")
    public AjaxResult buildBeanFiles(@Parameter("data>>") User bean, @Parameter("docName") TempFile[] docName) {
        System.out.println(new Gson().toJson(bean));
        for (TempFile file : docName) {
            System.out.println(file.getName());
            System.out.println(file.getContentType());
        }
        return new AjaxResult(true, "批量文件上传", bean);
    }


    @OK("json")
    @POST
    @Path("/buildBean")
    public AjaxResult buildBean(@Parameter("data>>") User bean) throws SQLException {
        System.out.println(new Gson().toJson(bean));
        Connection connection = DBConnPool.getDataSource().getConnection();
        System.out.println(connection);
        return new AjaxResult(true, "呵呵呵", bean);
    }


    @OK("file")
    @GET
    @Path("/dowload")
    public File dowloadFile() {
        return new File("D:\\TEMP\\模版说明.docx");
    }
}
```
