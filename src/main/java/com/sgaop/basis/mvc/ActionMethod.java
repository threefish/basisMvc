package com.sgaop.basis.mvc;

import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/8 0008
 * To change this template use File | Settings | File Templates.
 */
public class ActionMethod {
    private String method;
    private String klass;
    private Class<?> actionClass;
    private Method actionMethod;
    private String OK;
    private String note;

    public ActionMethod(String method, String klass, Class<?> actionClass, Method actionMethod, String OK, String note) {
        this.method = method;
        this.klass = klass;
        this.actionClass = actionClass;
        this.actionMethod = actionMethod;
        this.OK = OK;
        this.note = note;
    }

    public ActionMethod(String method, String klass, Class<?> actionClass, Method actionMethod) {
        this.method = method;
        this.klass = klass;
        this.actionClass = actionClass;
        this.actionMethod = actionMethod;
    }

    public String getOK() {
        return OK;
    }

    public void setOK(String OK) {
        this.OK = OK;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getKlass() {
        return klass;
    }

    public void setKlass(String klass) {
        this.klass = klass;
    }

    public Class<?> getActionClass() {
        return actionClass;
    }

    public void setActionClass(Class<?> actionClass) {
        this.actionClass = actionClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(Method actionMethod) {
        this.actionMethod = actionMethod;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
