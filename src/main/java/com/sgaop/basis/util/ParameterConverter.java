package com.sgaop.basis.util;

import com.sgaop.basis.mvc.upload.FileUploadAdapter;
import com.sgaop.basis.mvc.upload.TempFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/13 0013
 * To change this template use File | Settings | File Templates.
 */
public class ParameterConverter {
    /**
     * 参数转对象
     *
     * @param cls
     * @param prefix
     * @param requestParameterMap
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws ParseException
     */
    public static <T> T bulid(Class<T> cls, String prefix, Map<String, ?> requestParameterMap) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, ParseException {
        boolean b = false;
        sw:
        for (String key : requestParameterMap.keySet()) {
            if (key.startsWith(prefix + ".")) {
                b = true;
                break sw;
            }
        }
        if (b) {
            Object obj = ClassTool.getInstance(cls);
            Field[] fieldArray = cls.getDeclaredFields();
            for (int i = 0; i < fieldArray.length; i++) {
                Field field = fieldArray[i];
                Class fieldType = field.getType();
                String fieldName = field.getName();
                String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                String fromName = prefix + "." + fieldName;
                Object paramObj = requestParameterMap.get(fromName);
                if (paramObj == null) {
                    continue;
                }
                Method method = null;
                Class classDef = obj.getClass();
                if (fieldType.equals(String.class)) {
                    method = classDef.getMethod(methodName, String.class);
                } else if (fieldType.equals(String[].class)) {
                    method = classDef.getMethod(methodName, String[].class);
                } else if (fieldType.equals(int[].class)) {
                    method = classDef.getMethod(methodName, int[].class);
                } else if (fieldType.equals(int.class)) {
                    method = classDef.getMethod(methodName, int.class);
                } else if (fieldType.equals(double.class)) {
                    method = classDef.getMethod(methodName, double.class);
                } else if (fieldType.equals(long.class)) {
                    method = classDef.getMethod(methodName, long.class);
                } else if (fieldType.equals(float.class)) {
                    method = classDef.getMethod(methodName, float.class);
                } else if (fieldType.equals(boolean.class)) {
                    method = classDef.getMethod(methodName, boolean.class);
                } else if (fieldType.equals(Date.class)) {
                    method = classDef.getMethod(methodName, Date.class);
                } else if (fieldType.equals(java.sql.Date.class)) {
                    method = classDef.getMethod(methodName, java.sql.Date.class);
                } else if (fieldType.equals(Timestamp.class)) {
                    method = classDef.getMethod(methodName, Timestamp.class);
                }
                try {
                    Object objevalue = ClassTool.ParamCast(fieldType, paramObj);
                    if (objevalue != null) {
                        method.invoke(obj, new Object[]{objevalue});
                    }
                } catch (Exception e) {
                    throw new RuntimeException("参数" + fromName + "  (" + fieldType + ")转换错误： " + e.getMessage());
                }
            }
            return (T) obj;
        } else {
            return null;
        }

    }

    /**
     * 将url get参数转MAP
     *
     * @param url
     * @return
     */
    public static Map<String, String> urlToMap(String url) {
        url = url.substring(url.indexOf("?") + 1);
        String[] temp = url.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String s : temp) {
            String[] stemp = s.split("=");
            if (stemp.length == 2) {
                map.put("" + stemp[0], stemp[1]);
            } else {
                map.put("" + stemp[0], "");
            }
        }
        return map;
    }


    /**
     * 转成带文件的正常map
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object[]> bulidMultipartMap(HttpServletRequest request, Map<String, ?> requestParameterMap) throws Exception {
        Map<String, Object[]> dataMap = new HashMap();
        for (Map.Entry entry : requestParameterMap.entrySet()) {
            if (entry.getValue() instanceof String[]) {
                dataMap.put(String.valueOf(entry.getKey()), (String[]) entry.getValue());
            } else {
                dataMap.put(String.valueOf(entry.getKey()), new Object[]{entry.getValue()});
            }
        }
        if (ServletFileUpload.isMultipartContent(request)) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List items = upload.parseRequest(request);
            Map<String, Integer> isFiles = new HashMap<String, Integer>();
            for (Iterator i = items.iterator(); i.hasNext(); ) {
                FileItem fileItem = (FileItem) i.next();
                // 如果该FileItem不是表单域
                if (!fileItem.isFormField()) {
                    String name = fileItem.getFieldName();
                    String fileName = fileItem.getName();
                    int lax = fileName.lastIndexOf("\\");
                    if (lax > 0) {
                        fileName = fileName.substring(lax + 1, fileName.length());
                    }
                    boolean isNull = false;
                    if ("".equals(fileName) && fileItem.getInputStream().available() == 0) {
                        isNull = true;
                    }
                    if (isFiles.get(name) != null) {
                        isFiles.put(name, (isFiles.get(name) + 1));
                    } else {
                        isFiles.put(name, 1);
                    }
                    TempFile tempFile = new TempFile(fileName, fileItem.getInputStream(), fileItem.getContentType());
                    if (isNull) {
                        if (dataMap.get(name) == null || dataMap.get(name).length > 0) {
                            if (isFiles.get(name) > 1) {
                                dataMap.put(name, new TempFile[]{});
                            } else {
                                dataMap.put(name, new Object[]{null});
                            }
                        }
                    } else {
                        FileUploadAdapter.checkFile(fileName, fileItem.getInputStream().available());
                        //map中当前名称的文件个数不为空并且第一个文件不为null
                        if (dataMap.get(name) != null) {
                            Object[] fileObjs = dataMap.get(name);
                            int len = 0;
                            //如果第一个文件为空
                            if (dataMap.get(name)[0] != null) {
                                len = fileObjs.length;
                            }
                            TempFile[] files = new TempFile[len + 1];
                            for (int fi = 0; fi < len; fi++) {
                                TempFile file = (TempFile) fileObjs[fi];
                                files[fi] = file;
                            }
                            files[len] = tempFile;
                            dataMap.put(name, files);
                        } else {
                            dataMap.put(name, new Object[]{tempFile});
                        }
                    }
                } else {
                    String name = fileItem.getFieldName();
                    String value = IoTool.InputStreamTOString(fileItem.getInputStream());
                    dataMap.put(name, new Object[]{value});
                }
            }
        }
        return dataMap;
    }


    /**
     * 转成正常map
     *
     * @param request
     * @return
     */
    public static Map<String, String> bulid(HttpServletRequest request) {
        Map<String, String[]> req = request.getParameterMap();
        Map<String, String> map = new HashMap<String, String>();
        for (Iterator iter = req.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry element = (Map.Entry) iter.next();
            String key = (String) element.getKey();
            Object value = element.getValue();
            if (value instanceof String[]) {
                String[] values = (String[]) value;
                if (values.length == 1) {
                    map.put(key, values[0]);
                } else {
                    String temp = "";
                    for (String str : values) {
                        if (temp.equals(""))
                            temp = str;
                        temp += "," + str;
                    }
                }
            } else {
                map.put(key, value.toString());
            }
        }
        return map;
    }

}
