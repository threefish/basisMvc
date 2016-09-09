package com.sgaop.web.frame.server.mvc.view;

import freemarker.template.TemplateException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/9/5 0005
 * To change this template use File | Settings | File Templates.
 */
public interface View {

    /**
     * @param path     @OK("jsp : path值")
     * @param request
     * @param response
     * @param object   用户自定义返回的数据值
     */
    void render(String path, HttpServletRequest request, HttpServletResponse response, Object object) throws IOException, TemplateException;

}
