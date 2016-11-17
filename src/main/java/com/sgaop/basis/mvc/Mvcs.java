package com.sgaop.basis.mvc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/8 0008
 * To change this template use File | Settings | File Templates.
 */
public class Mvcs {

    private static ThreadLocal<FrameRequest> local = new ThreadLocal();

    /**
     * 初始化
     *
     * @param servletRequest
     * @param servletResponse
     * @param reqMap
     */
    public static void initLocal(ServletRequest servletRequest, ServletResponse servletResponse, Map<String, ?> reqMap, boolean isAjax) {
        /*设置项目根路径*/
        servletRequest.setAttribute("base", servletRequest.getServletContext().getContextPath());
        servletRequest.setAttribute("ctxPath", servletRequest.getServletContext().getContextPath());
        FrameRequest frameRequest = new FrameRequest((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, reqMap, isAjax);
        local.set(frameRequest);
    }

    /**
     * 销毁
     */
    public static void destroy() {
        local.remove();
    }

    public static HttpServletRequest getReq() {
        return local.get().getRequest();
    }

    public static HttpSession getSession() {
        return local.get().getRequest().getSession();
    }

    public static HttpServletResponse getResp() {
        return local.get().getResponse();
    }

    public static Map<String, ?> getReqMap() {
        return local.get().getReqMap();
    }

    public static boolean isAjax() {
        return local.get().isAjax();
    }

    /**
     * 缓存当前http请求内容
     */
    static class FrameRequest {

        private HttpServletRequest request;
        private HttpServletResponse response;
        private Map<String, ?> reqMap;

        private boolean ajax;

        public boolean isAjax() {
            return ajax;
        }

        public FrameRequest(HttpServletRequest request, HttpServletResponse response, Map<String, ?> reqMap, boolean ajax) {
            this.request = request;
            this.response = response;
            this.reqMap = reqMap;
            this.ajax = ajax;
        }

        public HttpServletRequest getRequest() {
            return request;
        }

        public HttpServletResponse getResponse() {
            return response;
        }

        public Map<String, ?> getReqMap() {
            return reqMap;
        }


    }
}
