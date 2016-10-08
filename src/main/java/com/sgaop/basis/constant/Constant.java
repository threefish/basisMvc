package com.sgaop.basis.constant;

import com.sgaop.basis.cache.StaticCacheManager;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/8 0008
 * To change this template use File | Settings | File Templates.
 */
public class Constant {

    public static final String utf8 = "UTF-8";

    public static final String JSP_PATH = "/WEB-INF/";

    public static final String STATIC_PATH_KEY = "staticPath";

    public static final String PAGE_SUFFIX = ".jsp";


    public static final Pattern ATTACH_PATTERN = Pattern.compile(StaticCacheManager.getCacheStr("attach.suffixReg"));

    public static final int ATTACH_MAX_FILE_SIEZ = StaticCacheManager.getIntCache("attach.maxFileSize");

    public static final int BASE_BYTE = 4096;

    public static final String WEB_TEMP_PATH = System.getProperty("java.io.tmpdir");

    public static final String WEB_SETUP_INIT = "$_WEB_INIT_$";

    public static final String WEB_SETUP_DESTROY = "$_WEB_DESTROY_$";
}
