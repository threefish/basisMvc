package com.sgaop.basis.scanner;


import com.sgaop.basis.annotation.*;
import com.sgaop.basis.cache.CacheManager;
import com.sgaop.basis.constant.Constant;
import com.sgaop.basis.dao.bean.TableFiled;
import com.sgaop.basis.dao.bean.TableInfo;
import com.sgaop.basis.ioc.IocBeanContext;
import com.sgaop.basis.mvc.ActionMethod;
import com.sgaop.basis.util.ClassTool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/8 0008
 * To change this template use File | Settings | File Templates.
 */
public class ClassScanner {

    public static Set<Class<?>> classes = new HashSet<>();

    static {
        classes = ClassScannerHelper.scanPackage("");
    }

    public static List<Class<?>> classes() {
        return new ArrayList(classes);
    }

    public static void init() {
        for (Class<?> ks : classes) {
            String classKey = ks.getName();
            Action action = ks.getAnnotation(Action.class);
            Setup setup = ks.getAnnotation(Setup.class);
            Table table = ks.getAnnotation(Table.class);
            if (action != null) {
                Method[] methods = ks.getMethods();
                for (Method method : methods) {
                    Path webAction = method.getAnnotation(Path.class);
                    OK ok = method.getAnnotation(OK.class);
                    String okVal = "";
                    if (ok != null) {
                        okVal = ok.value();
                    }
                    String relpath = "";
                    if (webAction != null) {
                        if (webAction.value().length == 0) {
                            relpath = action.value() + "/" + method.getName();
                            putUrlMapping(relpath, method, classKey, ks, okVal, webAction.note());
                        } else {
                            for (String path : webAction.value()) {
                                relpath = action.value() + path;
                                putUrlMapping(relpath, method, classKey, ks, okVal, webAction.note());
                            }
                        }
                    }
                }
            } else if (setup != null) {
                Method[] methods = ks.getMethods();
                for (Method method : methods) {
                    if ("init".equals(method.getName())) {
                        CacheManager.putSetupCache(Constant.WEB_SETUP_INIT, new ActionMethod("init", classKey, ks, method));
                    } else if ("destroy".equals(method.getName())) {
                        CacheManager.putSetupCache(Constant.WEB_SETUP_DESTROY, new ActionMethod("destroy", classKey, ks, method));
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


    private static void putUrlMapping(String relpath, Method method, String classKey, Class<?> ks, String okVal, String note) {
        boolean hasPOST = method.isAnnotationPresent(POST.class);
        boolean hasDELETE = method.isAnnotationPresent(DELETE.class);
        boolean hasPUT = method.isAnnotationPresent(PUT.class);
        boolean hasGET = method.isAnnotationPresent(GET.class);
        boolean hasHEAD = method.isAnnotationPresent(HEAD.class);
        if (hasPOST) {
            CacheManager.putUrlCache(relpath, new ActionMethod("POST", classKey, ks, method, okVal, note));
        }
        if (hasHEAD) {
            CacheManager.putUrlCache(relpath, new ActionMethod("HEAD", classKey, ks, method, okVal, note));
        }
        if (hasDELETE) {
            CacheManager.putUrlCache(relpath, new ActionMethod("DELETE", classKey, ks, method, okVal, note));
        }
        if (hasPUT) {
            CacheManager.putUrlCache(relpath, new ActionMethod("PUT", classKey, ks, method, okVal, note));
        }
        //默认支持get访问
        if (hasGET || (!hasPOST && !hasDELETE && !hasPUT && !hasHEAD)) {
            CacheManager.putUrlCache(relpath, new ActionMethod("GET", classKey, ks, method, okVal, note));
        }
    }


}
