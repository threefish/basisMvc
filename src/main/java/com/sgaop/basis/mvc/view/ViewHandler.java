package com.sgaop.basis.mvc.view;

import com.sgaop.basis.constant.Constant;
import com.sgaop.basis.error.WebErrorMessage;
import com.sgaop.basis.mvc.ActionResult;
import com.sgaop.basis.mvc.Mvcs;
import com.sgaop.basis.util.Logs;
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
    private static final Logger logger = Logs.get();

    public static void invokeAction(String servletPath, ActionResult actionResult, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resultType = actionResult.getResultType();
        if ("".equals(resultType) && actionResult.getResultData() != null) {
            resultType = (String) actionResult.getResultData();
        }
        WebErrorMessage error = actionResult.getWebErrorMessage();
        if ((error.getCode() == 200 && error.isJsp())) {
            DefaultViewsRender.RenderJSP(servletPath, request, response);
        } else if ((error.getCode() == 500 && !"".equals(error.getRedirectUrl()))) {
            if (error.isAjax()) {
                DefaultViewsRender.RenderJsonStr(response, error.getMessage());
            } else {
                String basePath = Mvcs.getSession().getServletContext().getContextPath();
                basePath = basePath.equals("/") ? "" : basePath;
                DefaultViewsRender.RenderRedirect(basePath + error.getRedirectUrl(), response);
            }
        } else if (error.getCode() == 500 && error.isAjax()) {
            DefaultViewsRender.RenderJsonStr(response, error.getMessage());
        } else if (error.getCode() != 500 && error.getCode() != 404) {
            if (resultType != null) {
                String[] path = resultType.split(":");
                if (path.length == 1) {
                    String temp = path[0];
                    path = new String[]{temp, ""};
                }
                if (ViewsRegister.hasRegisterView(resultType)) {
                    Class<?> klass = ViewsRegister.getViewClass(path[0]);
                    View view = (View) klass.newInstance();
                    view.afterProcess(request, response);
                    view.render(path[1], request, response, actionResult.getResultData());
                } else if (resultType.equals("json") || resultType.startsWith("json:")) {
                    String regs = "";
                    if (resultType.startsWith("json:")) {
                        regs = resultType.substring(5, resultType.length());
                    }
                    DefaultViewsRender.RenderJSON(response, regs, actionResult.getResultData());
                } else if (resultType.startsWith("jsp:") || resultType.startsWith("fw:")) {
                    DefaultViewsRender.RenderJSP(Constant.JSP_PATH + path[1], request, response);
                } else if (resultType.startsWith("rd:")) {
                    DefaultViewsRender.RenderRedirect(request.getContextPath() + "/" + path[1], response);
                } else if (resultType.startsWith("fw:")) {
                    request.getRequestDispatcher(request.getContextPath() + "/" + path[1]).forward(request, response);
                } else if (resultType.startsWith("file")) {
                    DefaultViewsRender.RenderFile(response, actionResult.getResultData());
                } else if (resultType.startsWith("raw")) {
                    RawViewRender.RenderRaw(request, response, actionResult.getResultData(), path[1]);
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
