package com.sgaop.basis.error;


/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/10 0010
 * To change this template use File | Settings | File Templates.
 */
public class WebErrorMessage {

    private int code;
    private String message;
    /**
     * 默认false
     */
    private boolean isJsp = false;

    private Exception exception;

    private String redirectUrl = "";

    private boolean ajax = false;


    public WebErrorMessage() {

    }

    public WebErrorMessage(int code, String message) {
        this.message = message;
        this.code = code;
    }

    public WebErrorMessage(int code, String message, Exception exception) {
        this.code = code;
        this.message = message;
        this.exception = exception;
    }


    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public boolean isAjax() {
        return ajax;
    }

    public void setAjax(boolean ajax) {
        this.ajax = ajax;
    }

    public boolean isJsp() {
        return isJsp;
    }

    public void setJsp(boolean jsp) {
        isJsp = jsp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
