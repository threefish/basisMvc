package com.sgaop.basis.scanner;

import com.sgaop.basis.cache.PropertiesManager;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/18 0018
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesScans {

    private static final Logger log = Logger.getRootLogger();

    public static void init() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            //将包名转换为文件路径
            URI base = classLoader.getResource("").toURI();
            Path path = Paths.get(Paths.get(base).toString());
            Files.walkFileTree(path, new FindFile());
        } catch (Exception e) {
            log.error("加载配置文件出错", e);
        }
    }


    private static class FindFile extends SimpleFileVisitor<Path> {
        public FindFile() {

        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            if (file.toString().endsWith(".properties")) {
                try (FileInputStream in = new FileInputStream(file.toFile())) {
                    Properties props = new Properties();
                    props.load(in);
                    PropertiesManager.putCache(props);
                } catch (Exception e) {
                    log.error("加载配置文件出错", e);
                }
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
