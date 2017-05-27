package com.sgaop.basis.mvc;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/8 0008
 * To change this template use File | Settings | File Templates.
 */
public class Mvcs {

    private static ThreadLocal<FrameRequest> local = new ThreadLocal();

    private static ThreadLocal<String> i18nLang = new ThreadLocal();

    private static ThreadLocal<Throwable> error = new ThreadLocal();

    public static void setError(Throwable e) {
        error.set(e);
    }

    public static Throwable getError() {
        return error.get();
    }


    public static void setI18nLang(String lang) {
        if (lang != null) {
            if (lang.length() > 0) {
                i18nLang.set(lang);
            }
        }
    }

    public static String getI18nLang() {
        //默认中文
        return i18nLang.get() == null ? "zh_CN" : i18nLang.get();
    }

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
        Map<String, String> cookiesMap = new HashMap<>();
        Cookie[] cookies = ((HttpServletRequest) servletRequest).getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookiesMap.put(cookie.getName(), cookie.getValue());
            }
        }
        String lang = cookiesMap.get("lang");
        setI18nLang(lang);
        FrameRequest frameRequest = new FrameRequest((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, reqMap, isAjax, cookiesMap);
        local.set(frameRequest);
    }


    /**
     * 获取 Servlet 执行的上下文
     *
     * @return Servlet 执行的上下文
     */
    public static ServletContext getServletContext() {
        return getReq().getServletContext();
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

    public static String getCookie(String key) {
        return local.get().getCookies().get(key);
    }

    public static Map<String, String> getCookies() {
        return local.get().getCookies();
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

        private Map<String, String> cookies;

        private boolean ajax;

        public boolean isAjax() {
            return ajax;
        }

        public FrameRequest(HttpServletRequest request, HttpServletResponse response, Map<String, ?> reqMap, boolean ajax, Map<String, String> cookies) {
            this.request = request;
            this.response = response;
            this.reqMap = reqMap;
            this.ajax = ajax;
            this.cookies = cookies;
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

        public Map<String, String> getCookies() {
            return cookies;
        }


    }
}
