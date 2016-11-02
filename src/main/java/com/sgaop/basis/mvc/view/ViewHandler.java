package com.sgaop.basis.mvc.view;

import com.sgaop.basis.constant.Constant;
import com.sgaop.basis.mvc.ActionResult;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/2 0002
 * To change this template use File | Settings | File Templates.
 */
public class ViewHandler {
    private static final Logger logger = Logger.getRootLogger();

    public static void invokeAction(String servletPath, ActionResult actionResult, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resultType = actionResult.getResultType();
        if ("".equals(resultType) && actionResult.getResultData() != null) {
            resultType = (String) actionResult.getResultData();
        }
        if ((actionResult.getWebErrorMessage().getCode() == 200 && actionResult.getWebErrorMessage().isJsp())) {
            DefaultViewsRender.RenderJSP(servletPath, request, response);
        } else if (actionResult.getWebErrorMessage().getCode() != 500 && actionResult.getWebErrorMessage().getCode() != 404) {
            if (resultType != null) {
                if (ViewsRegister.hasRegisterView(resultType)) {
                    String path[] = resultType.split(":");
                    Class<?> klass = ViewsRegister.getViewClass(path[0]);
                    View view = (View) klass.newInstance();
                    view.render(path[1], request, response, actionResult.getResultData());
                } else if (resultType.equals("json")) {
                    DefaultViewsRender.RenderJSON(response, actionResult.getResultData());
                } else if (resultType.startsWith("jsp:") || resultType.startsWith("fw:")) {
                    String path[] = resultType.split(":");
                    DefaultViewsRender.RenderJSP(Constant.JSP_PATH + path[1], request, response);
                } else if (resultType.startsWith("rd:")) {
                    String path[] = resultType.split(":");
                    DefaultViewsRender.RenderRedirect(request.getContextPath() + "/" + path[1], response);
                } else if (resultType.startsWith("fw::")) {
                    String path[] = resultType.split(":");
                    request.getRequestDispatcher(request.getContextPath() + "/" + path[1]) .forward(request,response);
                } else if (resultType.startsWith("file")) {
                    DefaultViewsRender.RenderFile(response, actionResult.getResultData());
                } else {
                    actionResult.getWebErrorMessage().setMessage("没有设置返回类型 [" + servletPath + "]");
                    logger.error(actionResult.getWebErrorMessage().getMessage());
                    DefaultViewsRender.RenderErrorPage(response, actionResult.getWebErrorMessage());
                }
            }
        } else {
            /**
             * 在以上条件都不满足的情况下进入错误页面
             */
            DefaultViewsRender.RenderErrorPage(response, actionResult.getWebErrorMessage());
        }

    }
}
