package com.sgaop.basis.web;

import com.sgaop.basis.cache.PropertiesManager;
import com.sgaop.basis.constant.ConstanErrorMsg;
import com.sgaop.basis.constant.Constant;
import com.sgaop.basis.mvc.ActionHandler;
import com.sgaop.basis.mvc.ActionResult;
import com.sgaop.basis.mvc.Mvcs;
import com.sgaop.basis.mvc.view.DefaultViewsRender;
import com.sgaop.basis.mvc.view.ViewHandler;
import com.sgaop.basis.util.ParameterConverter;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/8 0008
 * To change this template use File | Settings | File Templates.
 */
//@WebFilter("/*")
public class BasisMvcWebFilter implements Filter {
    private static final Logger logger = Logger.getRootLogger();

    private static final String STATIC_PATH = PropertiesManager.getCache(Constant.STATIC_PATH_KEY).toString();

    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("过滤器启动");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        servletRequest.setCharacterEncoding(Constant.utf8);
        servletResponse.setCharacterEncoding(Constant.utf8);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        DispatcherType dispatcherType = request.getDispatcherType();
        try {
            Map<String, ?> requestParameterMap = request.getParameterMap();
            if (ServletFileUpload.isMultipartContent(request)) {
                requestParameterMap = ParameterConverter.bulidMultipartMap(request);
            }
            Mvcs.initLocal(servletRequest, servletResponse, requestParameterMap);
            String reqMethod = request.getMethod();
            String servletPath = request.getServletPath();
            if (STATIC_PATH != null) {
                boolean isStatic = false;
                String staticPaths[] = STATIC_PATH.split(",");
                out:
                for (String staticPath : staticPaths) {
                    if (servletPath.startsWith(staticPath)) {
                        isStatic = true;
                        break out;
                    }
                }
                /**
                 * 访问静态目录下的jsp文件
                 */
                if (isStatic && servletPath.endsWith(Constant.PAGE_SUFFIX)) {
                    logger.warn("安全警告：不允许访问静态目录中的" + Constant.PAGE_SUFFIX + "文件");
                    DefaultViewsRender.RenderHttpStatus(response, 403, ConstanErrorMsg.ILLEGAL_OPERATION);
                    return;
                } else if (dispatcherType.name().equals("FORWARD") && servletPath.endsWith(Constant.PAGE_SUFFIX)) {
                    filterChain.doFilter(request, response);
                } else if (!isStatic) {
                    logger.debug("[" + reqMethod + "] [" + servletPath + "] ");
                    /**
                     * 访问的不是静态目录，现在注解中查询符合的访问地址
                     */
                    ActionResult actionResult = ActionHandler.invokeAction(servletPath, reqMethod, request, response);
                    ViewHandler.invokeAction(servletPath, actionResult, request, response);
                } else {
                    /**
                     * 访问静态资源文件
                     */
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            } else {
                logger.warn("没有设置静态WEB资源文件文件目录，可能会导致无法访问WEB资源文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            DefaultViewsRender.RenderErrorPage(response, e);
        } finally {
            Mvcs.destroy();
        }
    }

    public void destroy() {
        logger.info("过滤器销毁");
    }
}
