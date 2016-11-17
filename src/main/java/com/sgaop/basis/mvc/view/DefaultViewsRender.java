package com.sgaop.basis.mvc.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sgaop.basis.constant.Constant;
import com.sgaop.basis.error.WebErrorMessage;
import com.sgaop.basis.json.JsonExclusionStrategy;
import com.sgaop.basis.json.JsonFormat;
import com.sgaop.basis.json.TimestampTypeAdapter;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/10 0010
 * To change this template use File | Settings | File Templates.
 * 默认视图
 */
public class DefaultViewsRender {

    private static final Logger logger = Logger.getRootLogger();

    private static final Gson formatJson=new Gson();

    public static void RenderJSP(String jspPath, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher(jspPath).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("转发至[" + jspPath + "]出错", e);
            throw new RuntimeException(e);
        }
    }

    public static void RenderRedirect(String jspPath, HttpServletResponse response) {
        try {
            response.sendRedirect(jspPath);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("重定向至[" + jspPath + "]出错", e);
            throw new RuntimeException(e);
        }
    }


    public static void RenderJSON(HttpServletResponse response,String regs,Object resultObj) {
        try {
            Gson gson=null;
            if(regs.equals("")){
                gson=new Gson();
            }else {
                //此处可以进行优化，在扫描类时就将返回类型确定，可以进一步提升性能
                JsonFormat jsonFormat = formatJson.fromJson(regs, JsonFormat.class);
                GsonBuilder gb = new GsonBuilder();
                if(jsonFormat.isIgnoreNull()){
                    gb.serializeNulls();
                }
                if(!jsonFormat.getLocked().equals("")){
                    gb.setExclusionStrategies(new JsonExclusionStrategy(jsonFormat.getLocked().split("\\|")));
                }
                if (jsonFormat.getDateFormat()!=null&&!jsonFormat.getDateFormat().equals("")) {
                    gb.setDateFormat(jsonFormat.getDateFormat());
                    gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).create();
                }
                gson = gb.create();
            }
            response.setContentType("application/json");
            response.setCharacterEncoding(Constant.utf8);
            PrintWriter printWriter = response.getWriter();
            printWriter.write(gson.toJson(resultObj));
            printWriter.flush();
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回JSON数据出错", e);
            throw new RuntimeException(e);
        }
    }

    public static void RenderJsonStr(HttpServletResponse response, String resultStr) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding(Constant.utf8);
            PrintWriter printWriter = response.getWriter();
            printWriter.write(resultStr);
            printWriter.flush();
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回JSON数据出错", e);
            throw new RuntimeException(e);
        }
    }

    public static void RenderFile(HttpServletResponse response, Object resultObj) {
        try {
            if (resultObj instanceof File) {
                File file = (File) resultObj;
                String fileName = URLEncoder.encode(file.getName(), Constant.utf8);
                fileName = fileName.replaceAll("\\+", "%20");
                response.setHeader("Cache-control", "private");
                response.setHeader("Cache-Control", "maxage=3600");
                response.setHeader("Pragma", "public");
                response.setHeader("Accept-Ranges", "bytes");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                byte[] data = new byte[1024];
                int count = -1;
                while ((count = inputStream.read(data, 0, 1024)) != -1) {
                    outputStream.write(data, 0, count);
                }
                outputStream.flush();
                inputStream.close();
                outputStream.close();
            } else {
                RenderErrorPage(response, new WebErrorMessage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "返回的数据不是文件"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void RenderErrorPage(HttpServletResponse response, WebErrorMessage errorMessage) {
        try {
            if (errorMessage.getException() != null) {
                response.sendError(errorMessage.getCode(), errorMessage.getException().getMessage());
            } else {
                response.sendError(errorMessage.getCode(), errorMessage.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void RenderErrorPage(HttpServletResponse response, Exception ex) {
        try {
            response.sendError(500, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void RenderHttpStatus(HttpServletResponse response, int code, String msg) {
        try {
            response.sendError(code, msg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
