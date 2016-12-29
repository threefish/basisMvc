package com.sgaop.basis.scanner;


import com.sgaop.basis.annotation.*;
import com.sgaop.basis.cache.MvcsManager;
import com.sgaop.basis.constant.Constant;
import com.sgaop.basis.dao.bean.TableFiled;
import com.sgaop.basis.dao.bean.TableInfo;
import com.sgaop.basis.mvc.ActionMethod;
import com.sgaop.basis.util.ClassTool;
import com.sgaop.basis.util.Logs;
import org.apache.log4j.Logger;

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
public class ClassHelper {
    protected static final Logger log = Logs.get();
    public static Set<Class<?>> classes = new HashSet<>();

    static {
        classes = ClassScans.scanPackage("");
    }

    public static List<Class<?>> classes() {
        return new ArrayList(classes);
    }

    public static void init() {
        try {
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
                    try {
                        MvcsManager.putSetupCache(Constant.WEB_SETUP, ks.newInstance());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else if (table != null) {
                    Field[] fields = ks.getDeclaredFields();
                    TableInfo tbinfo = new TableInfo();
                    Pk pk = ks.getAnnotation(Pk.class);
                    if (pk != null) {
                        tbinfo.setPkName(pk.value());
                    }
                    if ("".equals(table.value())) {
                        tbinfo.setTableName(ks.getSimpleName().toLowerCase());
                    } else {
                        tbinfo.setTableName(table.value());
                    }
                    for (Field field : fields) {
                        Colum colum = field.getAnnotation(Colum.class);
                        ID id = field.getAnnotation(ID.class);
                        TableFiled tf = new TableFiled();
                        if (colum != null) {
                            if ("".equals(colum.value())) {
                                tf.setColumName(field.getName().toLowerCase());
                                tf.setFiledName(field.getName());
                            } else {
                                tf.setColumName(colum.value().toLowerCase());
                                tf.setFiledName(field.getName());
                            }
                            tbinfo.addColums(tf.getColumName());
                            tf.set_setMethodName(ClassTool.setMethodName(field.getName(), field.getType()));
                            tf.set_getMethodName(ClassTool.getMethodName(field.getName(), field.getType()));
                            tbinfo.addDaoFiled(tf.getColumName(), tf);
                        }
                        if (id != null) {
                            if (tbinfo.getPkName() == null) {
                                if (id.value().equals("")) {
                                    tbinfo.setPkName(new String[]{field.getName()});
                                } else {
                                    tbinfo.setPkName(new String[]{id.value()});
                                }
                            }
                        }
                    }
                    MvcsManager.putTableCache(classKey, tbinfo);
                }
            }

        } catch (Exception e) {
            log.error("classes Helper init error! " + e);
        }
//        IocBeanContext.me().init(classes);
    }


    private static void putUrlMapping(String relpath, Method method, String classKey, Class<?> ks, String okVal, String note) {
        boolean hasPOST = method.isAnnotationPresent(POST.class);
        boolean hasDELETE = method.isAnnotationPresent(DELETE.class);
        boolean hasPUT = method.isAnnotationPresent(PUT.class);
        boolean hasGET = method.isAnnotationPresent(GET.class);
        boolean hasHEAD = method.isAnnotationPresent(HEAD.class);
        if (hasPOST) {
            MvcsManager.putUrlCache(relpath, new ActionMethod("POST", classKey, ks, method, okVal, note));
        }
        if (hasHEAD) {
            MvcsManager.putUrlCache(relpath, new ActionMethod("HEAD", classKey, ks, method, okVal, note));
        }
        if (hasDELETE) {
            MvcsManager.putUrlCache(relpath, new ActionMethod("DELETE", classKey, ks, method, okVal, note));
        }
        if (hasPUT) {
            MvcsManager.putUrlCache(relpath, new ActionMethod("PUT", classKey, ks, method, okVal, note));
        }
        //默认支持get访问
        if (hasGET || (!hasPOST && !hasDELETE && !hasPUT && !hasHEAD)) {
            MvcsManager.putUrlCache(relpath, new ActionMethod("GET", classKey, ks, method, okVal, note));
        }
    }


}
