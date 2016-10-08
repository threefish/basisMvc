package com.sgaop.bean;

import com.sgaop.basis.annotation.Colum;
import com.sgaop.basis.annotation.Pk;
import com.sgaop.basis.annotation.Table;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/27 0027
 * To change this template use File | Settings | File Templates.
 */
@Table("tb_user")
public class tbUser {

    @Pk
    @Colum
    private int id;

    private boolean lock;

    @Colum("tk")
    private String tkx;

    @Colum
    private String os;

    @Colum
    private String ip;

    @Colum
    private Timestamp ct;

    @Colum
    private String ua;


    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTkx() {
        return tkx;
    }

    public void setTkx(String tkx) {
        this.tkx = tkx;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Timestamp getCt() {
        return ct;
    }

    public void setCt(Timestamp ct) {
        this.ct = ct;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }
}
