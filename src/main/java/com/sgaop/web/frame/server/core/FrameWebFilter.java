package com.sgaop.web.frame.server.core;

import com.sgaop.web.frame.server.cache.StaticCacheManager;
import com.sgaop.web.frame.server.constant.Constant;
import com.sgaop.web.frame.server.error.ConstanErrorMsg;
import com.sgaop.web.frame.server.mvc.ActionHandler;
import com.sgaop.web.frame.server.mvc.ActionResult;
import com.sgaop.web.frame.server.mvc.Mvcs;
import com.sgaop.web.frame.server.mvc.view.DefaultViewsRender;
import com.sgaop.web.frame.server.mvc.view.ViewsRegister;
import com.sgaop.web.frame.server.util.ParameterConverter;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/8 0008
 * To change this template use File | Settings | File Templates.
 */
@WebFilter("/*")
public class FrameWebFilter implements Filter {
    private static final Logger logger = Logger.getRootLogger();

    private static final String STATIC_PATH = StaticCacheManager.getCache(Constant.STATIC_PATH_KEY).toString();

    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("过滤器启动");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        servletRequest.setCharacterEncoding(Constant.utf8);
        servletResponse.setCharacterEncoding(Constant.utf8);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
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
                } else if (!isStatic) {
                    logger.debug("[" + reqMethod + "] [" + servletPath + "] ");
                    /**
                     * 访问的不是静态目录，现在注解中查询符合的访问地址
                     */
                    ActionResult actionResult = ActionHandler.invokeAction(servletPath, reqMethod, request, response);
                    String resultType = actionResult.getResultType();
                    if (actionResult.getWebErrorMessage().getCode() == 200 && actionResult.getWebErrorMessage().isJsp()) {
                        DefaultViewsRender.RenderJSP(servletPath, request, response);
                        return;
                    } else if (actionResult.getWebErrorMessage().getCode() != 500 && actionResult.getWebErrorMessage().getCode() != 404) {
                        if (resultType != null) {
                            if (ViewsRegister.hasRegisterView(resultType)) {
                                String path[] = resultType.split(":");
                                Class<?> klass = ViewsRegister.getViewClass(path[0]);
                                Object view = klass.newInstance();
                                Method method = klass.getMethod("render", String.class, HttpServletRequest.class, HttpServletResponse.class, Object.class);
                                method.invoke(view, path[1], request, response, actionResult.getResultData());
                                return;
                            } else if (resultType.equals("json")) {
                                DefaultViewsRender.RenderJSON(response, actionResult.getResultData());
                            } else if (resultType.startsWith("jsp:") || resultType.startsWith("fw:")) {
                                String path[] = resultType.split(":");
                                DefaultViewsRender.RenderJSP(Constant.JSP_PATH + path[1], request, response);
                                return;
                            } else if (resultType.startsWith("rd:")) {
                                String path[] = resultType.split(":");
                                DefaultViewsRender.RenderRedirect(request.getContextPath() + "/" + path[1], response);
                                return;
                            } else if (resultType.startsWith("file")) {
                                DefaultViewsRender.RenderFile(response, actionResult.getResultData());
                                return;
                            } else {
                                actionResult.getWebErrorMessage().setMessage("没有设置返回类型 [" + servletPath + "]");
                                logger.error(actionResult.getWebErrorMessage().getMessage());
                                DefaultViewsRender.RenderErrorPage(response, actionResult.getWebErrorMessage());
                                return;
                            }
                        }
                    } else {
                        /**
                         * 在以上条件都不满足的情况下进入错误页面
                         */
                        DefaultViewsRender.RenderErrorPage(response, actionResult.getWebErrorMessage());
                        return;
                    }
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
        }
    }

    public void destroy() {
        logger.info("过滤器销毁");
    }
}
