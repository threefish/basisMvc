package com.sgaop.basis.constant;

import com.sgaop.basis.cache.PropertiesManager;

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


    public static final Pattern ATTACH_PATTERN = Pattern.compile(PropertiesManager.getCacheStr("attach.suffixReg"));

    public static final int ATTACH_MAX_FILE_SIEZ = PropertiesManager.getIntCache("attach.maxFileSize");

    public static final int BASE_BYTE = 4096;

    public static final String WEB_TEMP_PATH = System.getProperty("java.io.tmpdir");

    public static final String WEB_SETUP = "$_WEB_INIT_$";

    public static final String IOC_SEPARATOR = "_$$_";
    public static final String IOC_SEPARATOR_REG = "\\_\\$\\$\\_";

    public static final String BASIS_AUTHOR_ISSUES = "https://github.com/threefish/basisMvc/issues";

}
