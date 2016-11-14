//package com.sgaop.web.action.view;
//
//import com.sgaop.basis.mvc.view.View;
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.OutputStreamWriter;
//import java.io.Writer;
//
///**
// * Created by IntelliJ IDEA.
// * User: 306955302@qq.com
// * Date: 2016/9/5 0005
// * To change this template use File | Settings | File Templates.
// */
//public class FreeMarkerView implements View {
//
//    private static Configuration cfg = new Configuration(Configuration.getVersion());
//
//    static {
//        cfg.setClassForTemplateLoading(FreeMarkerView.class, "/ftl");
//    }
//
//    public void render(String path, HttpServletRequest request, HttpServletResponse response, Object object) {
//        try {
//            Template template = cfg.getTemplate(path);
//            Writer writer = new OutputStreamWriter(response.getOutputStream());
//            template.process(object, writer);
//            writer.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
