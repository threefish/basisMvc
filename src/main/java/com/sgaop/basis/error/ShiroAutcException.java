package com.sgaop.basis.error;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/17 0017
 * To change this template use File | Settings | File Templates.
 */
public class ShiroAutcException extends Throwable implements Serializable {

    static final long serialVersionUID = -3387516993124229948L;

    private String redirectUrl = "";

    private boolean ajax = false;

    public boolean isAjax() {
        return ajax;
    }

    public void setAjax(boolean ajax) {
        this.ajax = ajax;
    }

    public ShiroAutcException(String message) {
        super(message);
    }

    public ShiroAutcException(String message, String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
    }


    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

}
