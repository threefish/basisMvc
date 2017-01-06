package com.sgaop.basis.mvc.view;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2017/1/5 0005
 * To change this template use File | Settings | File Templates.
 */
public class DownFile {

    private String name;

    private File file;


    public DownFile(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
