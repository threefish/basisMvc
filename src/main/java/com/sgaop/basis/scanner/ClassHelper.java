package com.sgaop.basis.scanner;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/18 0018
 * To change this template use File | Settings | File Templates.
 */
public class ClassHelper {

    private static ClassLoader classLoader = null;

    private static String basePath = "";

    public static Set<Class<?>> scanPackage(String packageName) {
        //存放扫描到的类
        Set<Class<?>> classes = new LinkedHashSet<>();
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
            //将包名转换为文件路径
            URI base = classLoader.getResource("").toURI();
            basePath = Paths.get(base).toString();
            Path path = Paths.get(basePath, packageName.replaceAll("\\.", Matcher.quoteReplacement(File.separator)));
            Files.walkFileTree(path, new FindFile(classes));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static class FindFile extends SimpleFileVisitor<Path> {

        private Set<Class<?>> classes;

        public FindFile(Set<Class<?>> classes) {
            this.classes = classes;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            try {
                if (file.toString().endsWith(".class")) {
                    String packgeClassName = file.toString().replace(basePath, "").replaceAll(Matcher.quoteReplacement(File.separator), ".");
                    if (packgeClassName.startsWith(".")) {
                        packgeClassName = packgeClassName.substring(1, packgeClassName.length());
                    }
                    packgeClassName = packgeClassName.replace(".class", "");
                    classes.add(classLoader.loadClass(packgeClassName));
                }
//                else if (file.toString().endsWith(".jar")) {
//                    Path path = file.getFileSystem().getPath("com","sgaop","basis","");
//                    classes.add(classLoader.loadClass(""));
//                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return FileVisitResult.CONTINUE;
        }
    }


    public static void main(String[] args) {
        scanPackage("");
    }
}
