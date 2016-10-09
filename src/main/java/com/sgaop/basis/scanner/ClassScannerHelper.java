package com.sgaop.basis.scanner;

import com.sgaop.basis.dao.impl.DaoImpl;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/8 0008
 * To change this template use File | Settings | File Templates.
 */
public class ClassScannerHelper {

    private static final Logger log = Logger.getRootLogger();

    private static ClassLoader classLoader = null;

    /**
     * 扫描包下面的类
     *
     * @param packageName
     */
    public static Set<Class<?>> scanPackage(String packageName) {
        classLoader = Thread.currentThread().getContextClassLoader();
        //是否循环搜索包
        boolean recursive = true;
        //存放扫描到的类
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        //将包名转换为文件路径
        String packageDirName = packageName.replace('.', '/');
        try {
//            scannerJar(DaoImpl.class.getPackage().getName(), classes, true);
            scannerJar("com.sgaop.basis", classes, true);
            Enumeration<URL> dirs = classLoader.getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                }
            }
            return classes;
        } catch (IOException e) {
            e.printStackTrace();
            log.warn(e);
        }
        return null;
    }

    public static void scannerJar(String packageName, Set<Class<?>> classes, boolean recursive) throws IOException {
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs = classLoader.getResources(packageDirName);
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            String protocol = url.getProtocol();
            if ("jar".equals(protocol)) {
                // 如果是jar包文件
                JarFile jar;
                try {
                    // 获取jar
                    jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    // 从此jar包 得到一个枚举类
                    Enumeration<JarEntry> entries = jar.entries();
                    // 同样的进行循环迭代
                    while (entries.hasMoreElements()) {
                        // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        // 如果是以/开头的
                        if (name.charAt(0) == '/') {
                            // 获取后面的字符串
                            name = name.substring(1);
                        }
                        // 如果前半部分和定义的包名相同
                        if (name.startsWith(packageDirName)) {
                            int idx = name.lastIndexOf('/');
                            // 如果以"/"结尾 是一个包
                            if (idx != -1) {
                                // 获取包名 把"/"替换成"."
                                packageName = name.substring(0, idx).replace('/', '.');
                            }
                            // 如果可以迭代下去 并且是一个包
                            if ((idx != -1) || recursive) {
                                // 如果是一个.class文件 而且不是目录
                                if (name.endsWith(".class")
                                        && !entry.isDirectory()) {
                                    // 去掉后面的".class" 获取真正的类名
                                    String className = name.substring(packageName.length() + 1, name.length() - 6);
                                    try {
                                        // 添加到classes
                                        classes.add(classLoader.loadClass(removeStartDot(packageName + '.' + className)));
                                    } catch (ClassNotFoundException e) {
                                        log.warn(e);
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    log.warn(e);
                }
            }
        }
    }


    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName 包名
     * @param packagePath 包的物理路径
     * @param recursive   是否递归扫描
     * @param classes     类集合
     */
    private static void findAndAddClassesInPackageByFile(String packageName,
                                                         String packagePath,
                                                         final boolean recursive, Set<Class<?>> classes) {

        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirFiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory())
                        || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirFiles) {
            // 如果是目录 则递归继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(removeStartDot(packageName + "." + file.getName()), file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    classes.add(classLoader.loadClass(removeStartDot(packageName + '.' + className)));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    log.warn(e);
                }
            }
        }
    }

    public static String removeStartDot(String str) {
        if (str.startsWith(".")) {
            str = str.substring(1, str.length());
        }
        return str;
    }

}
