package com.sgaop.basis.util;

import com.sgaop.basis.constant.Constant;

import java.io.*;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/13 0013
 * To change this template use File | Settings | File Templates.
 */
public class IoTool {


    public static File writeFile(InputStream in, String pathName) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[Constant.BASE_BYTE];
        int count = -1;
        while ((count = in.read(data, 0, Constant.BASE_BYTE)) != -1)
            outStream.write(data, 0, count);
        data = null;
        File file = new File(pathName);
        FileOutputStream fout = new FileOutputStream(file);
        fout.write(outStream.toByteArray());
        fout.flush();
        fout.close();
        return file;
    }

    public static File writeTempFile(InputStream in, String fileName, String sessionid) throws IOException {
        String uuidtemp = UUID.randomUUID().toString().replaceAll("-", "");
        String pathName = Constant.WEB_TEMP_PATH + File.separator + sessionid + File.separator + uuidtemp;
        File tempfiledir = new File(pathName);
        if (!tempfiledir.exists()) {
            tempfiledir.mkdirs();
        }
        pathName += File.separator + fileName;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[Constant.BASE_BYTE];
        int count = -1;
        while ((count = in.read(data, 0, Constant.BASE_BYTE)) != -1)
            outStream.write(data, 0, count);
        data = null;
        File file = new File(pathName);
        FileOutputStream fout = new FileOutputStream(file);
        fout.write(outStream.toByteArray());
        fout.flush();
        fout.close();
        return file;
    }


    /**
     * 将InputStream转换成String
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static String InputStreamTOString(InputStream in) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[Constant.BASE_BYTE];
        int count = -1;
        while ((count = in.read(data, 0, Constant.BASE_BYTE)) != -1)
            outStream.write(data, 0, count);
        data = null;
        return new String(outStream.toByteArray(), "UTF-8");

    }
}
