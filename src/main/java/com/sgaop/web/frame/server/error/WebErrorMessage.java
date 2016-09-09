package com.sgaop.web.frame.server.error;


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
