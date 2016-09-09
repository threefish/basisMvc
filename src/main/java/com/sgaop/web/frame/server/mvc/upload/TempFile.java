package com.sgaop.web.frame.server.mvc.upload;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/26 0026
 * To change this template use File | Settings | File Templates.
 */
public class TempFile {

    private String name;


    private InputStream InputStream;

    private String contentType;


    public TempFile(String name, InputStream InputStream, String contentType) {
        this.name = name;
        this.InputStream = InputStream;
        this.contentType = contentType;

    }

    public String getName() {
        return name;
    }

    public int getSize() throws IOException {
        return getInputStream().available();
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getInputStream() {
        return InputStream;
    }

}
