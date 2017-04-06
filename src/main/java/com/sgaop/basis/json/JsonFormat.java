package com.sgaop.basis.json;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/17 0017
 * To change this template use File | Settings | File Templates.
 */
public class JsonFormat {

    private boolean ignoreNull = false;

    private boolean nullAsEmtry = false;

    private String locked;

    private String DateFormat;


    public String getLocked() {
        return locked == null ? "" : locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getDateFormat() {
        return DateFormat;
    }

    public void setDateFormat(String dateFormat) {
        DateFormat = dateFormat;
    }

    public boolean isIgnoreNull() {

        return ignoreNull;
    }

    public boolean isNullAsEmtry() {
        return nullAsEmtry;
    }

    public void setNullAsEmtry(boolean nullAsEmtry) {
        this.nullAsEmtry = nullAsEmtry;
    }

    public void setIgnoreNull(boolean ignoreNull) {
        this.ignoreNull = ignoreNull;
    }
}
