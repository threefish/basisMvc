package com.sgaop.web.frame.server.scanner;


import com.sgaop.web.frame.server.cache.CacheManager;
import com.sgaop.web.frame.server.constant.Constant;
import com.sgaop.web.frame.server.dao.TableFiled;
import com.sgaop.web.frame.server.dao.TableInfo;
import com.sgaop.web.frame.server.dao.annotation.Colum;
import com.sgaop.web.frame.server.dao.annotation.Pk;
import com.sgaop.web.frame.server.dao.annotation.Table;
import com.sgaop.web.frame.server.ioc.IocBeanContext;
import com.sgaop.web.frame.server.mvc.ActionMethod;
import com.sgaop.web.frame.server.mvc.annotation.*;
import com.sgaop.web.frame.server.util.ClassTool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/8 0008
 * To change this template use File | Settings | File Templates.
 */
public class ClassScanner {

    public static void ScannerAllClass() {
        Set<Class<?>> classes = ClassScannerHelper.scanPackage("");
        for (Class<?> ks : classes) {
            String classKey = ks.getName();
            WebController webController = ks.getAnnotation(WebController.class);
            Setup setup = ks.getAnnotation(Setup.class);
            Table table = ks.getAnnotation(Table.class);
            if (webController != null) {
                Method[] methods = ks.getMethods();
                for (Method method : methods) {
                    Path webAction = method.getAnnotation(Path.class);
                    OK ok = method.getAnnotation(OK.class);
                    String relpath = "";
                    if (webAction != null) {
                        if (webAction.value().length == 0) {
                            relpath = webController.value() + "/" + method.getName();
                        } else {
                            for (String path : webAction.value()) {
                                relpath = webController.value() + path;
                            }
                        }
                        String okVal = "";
                        if (ok != null) {
                            okVal = ok.value();
                        }
                        boolean hasPOST = method.isAnnotationPresent(POST.class);
                        boolean hasDELETE = method.isAnnotationPresent(DELETE.class);
                        boolean hasPUT = method.isAnnotationPresent(PUT.class);
                        boolean hasGET = method.isAnnotationPresent(GET.class);
                        boolean hasHEAD = method.isAnnotationPresent(HEAD.class);
                        if (hasPOST) {
                            CacheManager.putUrlCache(relpath, new ActionMethod("POST", classKey, ks, method, okVal));
                        }
                        if (hasHEAD) {
                            CacheManager.putUrlCache(relpath, new ActionMethod("HEAD", classKey, ks, method, okVal));
                        }
                        if (hasDELETE) {
                            CacheManager.putUrlCache(relpath, new ActionMethod("DELETE", classKey, ks, method, okVal));
                        }
                        if (hasPUT) {
                            CacheManager.putUrlCache(relpath, new ActionMethod("PUT", classKey, ks, method, okVal));
                        }
                        //默认支持get访问
                        if (hasGET || (!hasPOST && !hasDELETE && !hasPUT && !hasHEAD)) {
                            CacheManager.putUrlCache(relpath, new ActionMethod("GET", classKey, ks, method, okVal));
                        }
                    }
                }
            } else if (setup != null) {
                Method[] methods = ks.getMethods();
                for (Method method : methods) {
                    if ("init".equals(method.getName())) {
                        CacheManager.putUrlCache(Constant.WEB_SETUP_INIT, new ActionMethod("init", classKey, ks, method, ""));
                    } else if ("destroy".equals(method.getName())) {
                        CacheManager.putUrlCache(Constant.WEB_SETUP_DESTROY, new ActionMethod("destroy", classKey, ks, method, ""));
                    }
                }
            } else if (table != null) {
                Field[] fields = ks.getDeclaredFields();
                TableInfo daoMethod = new TableInfo();
                if ("".equals(table.value())) {
                    daoMethod.setTableName(ks.getSimpleName().toLowerCase());
                } else {
                    daoMethod.setTableName(table.value());
                }
                for (Field field : fields) {
                    Colum colum = field.getAnnotation(Colum.class);
                    Pk pk = field.getAnnotation(Pk.class);
                    TableFiled tableFiled = new TableFiled();
                    if (colum != null) {
                        if ("".equals(colum.value())) {
                            tableFiled.setColumName(field.getName());
                            tableFiled.setFiledName(field.getName());
                        } else {
                            tableFiled.setColumName(colum.value());
                            tableFiled.setFiledName(field.getName());
                        }
                        daoMethod.addColums(tableFiled.getColumName());
                        tableFiled.set_setMethodName(ClassTool.setMethodName(field.getName()));
                        tableFiled.set_getMethodName(ClassTool.getMethodName(field.getName(), field.getType()));
                        daoMethod.addDaoFiled(tableFiled.getColumName(), tableFiled);
                    }
                    if (pk != null) {
                        if (!"".equals(pk.value())) {
                            daoMethod.setPkName(pk.value());
                        } else {
                            daoMethod.setPkName(field.getName());
                        }
                    }
                }
                CacheManager.putTableCache(classKey, daoMethod);
            }
        }
        IocBeanContext.me().init(classes);
    }
}
