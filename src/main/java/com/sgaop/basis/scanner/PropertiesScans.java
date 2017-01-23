package com.sgaop.basis.scanner;

import com.sgaop.basis.cache.PropertiesManager;
import com.sgaop.basis.constant.Constant;
import com.sgaop.basis.i18n.LanguageManager;
import com.sgaop.basis.log.Logs;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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

    private static final Logger log = Logs.get();

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

        private final String _suffix = ".properties";

        private final String i18n = "i18n_";

        public FindFile() {

        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
            String fileName = path.toString();
            if (fileName.endsWith(_suffix)) {
                File file = path.toFile();
                try (InputStreamReader in = new InputStreamReader(new FileInputStream(file), Constant.utf8)) {
                    Properties props = new Properties();
                    props.load(in);
                    fileName = file.getName();
                    //缓存语言包
                    if (fileName.startsWith(i18n)) {
                        fileName = fileName.replaceAll(i18n, "").replaceAll(_suffix, "");
                        LanguageManager.add(path, fileName, props);
                    } else {
                        //缓存配置文件kv值
                        PropertiesManager.putCache(props);
                    }
                } catch (Exception e) {
                    log.error("加载配置文件出错", e);
                }
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
