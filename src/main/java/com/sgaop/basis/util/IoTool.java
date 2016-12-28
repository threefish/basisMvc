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
    private static final int BUF_SIZE = 8192;

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


    /**
     * 将输入流写入一个输出流。
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输入/出流
     *
     * @param ops        输出流
     * @param ins        输入流
     * @param bufferSize 缓冲块大小
     * @return 写入的字节数
     * @throws IOException
     */
    public static long write(OutputStream ops, InputStream ins, int bufferSize) throws IOException {
        if (null == ops || null == ins)
            return 0;

        byte[] buf = new byte[bufferSize];
        int len;
        long bytesCount = 0;
        while (-1 != (len = ins.read(buf))) {
            bytesCount += len;
            ops.write(buf, 0, len);
        }
        // 啥都没写，强制触发一下写
        // 这是考虑到 walnut 的输出流实现，比如你写一个空文件
        // 那么输入流就是空的，但是 walnut 的包裹输出流并不知道你写过了
        // 它人你就是打开一个输出流，然后再关上，所以自然不会对内容做改动
        // 所以这里触发一个写，它就知道，喔你要写个空喔。
        if (0 == bytesCount) {
            ops.write(buf, 0, 0);
        }
        ops.flush();
        return bytesCount;
    }

    public static long writeAndClose(OutputStream ops, InputStream ins, int buf) {
        try {
            return write(ops, ins, buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            safeClose(ops);
            safeClose(ins);
        }
    }


    /**
     * 关闭一个可关闭对象，可以接受 null。如果成功关闭，返回 true，发生异常 返回 false
     *
     * @param cb 可关闭对象
     * @return 是否成功关闭
     */
    public static boolean safeClose(Closeable cb) {
        if (null != cb)
            try {
                cb.close();
            } catch (IOException e) {
                return false;
            }
        return true;
    }

    /**
     * 将一段文本全部写入一个writer。
     * <p>
     * <b style=color:red>注意</b>，它会关闭输出流
     *
     * @param writer 输出流
     * @param cs     文本
     */
    public static void writeAndClose(Writer writer, CharSequence cs) {
        try {
            write(writer, cs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            safeClose(writer);
        }
    }

    /**
     * 将一段文本全部写入一个writer。
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输出流
     *
     * @param writer
     * @param cs     文本
     * @throws IOException
     */
    public static void write(Writer writer, CharSequence cs) throws IOException {
        if (null != cs && null != writer) {
            writer.write(cs.toString());
            writer.flush();
        }
    }

    /**
     * 将输入流写入一个输出流。块大小为 8192
     * <p>
     * <b style=color:red>注意</b>，它会关闭输入/出流
     *
     * @param ops 输出流
     * @param ins 输入流
     * @return 写入的字节数
     */
    public static long writeAndClose(OutputStream ops, InputStream ins) {
        try {
            return write(ops, ins);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            safeClose(ops);
            safeClose(ins);
        }
    }

    /**
     * 将输入流写入一个输出流。块大小为 8192
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输入/出流
     *
     * @param ops 输出流
     * @param ins 输入流
     * @return 写入的字节数
     * @throws IOException
     */
    public static long write(OutputStream ops, InputStream ins) throws IOException {
        return write(ops, ins, BUF_SIZE);
    }

    /**
     * 将文本输入流写入一个文本输出流。块大小为 8192
     * <p>
     * <b style=color:red>注意</b>，它会关闭输入/出流
     *
     * @param writer 输出流
     * @param reader 输入流
     */
    public static long writeAndClose(Writer writer, Reader reader) {
        try {
            return write(writer, reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            safeClose(writer);
            safeClose(reader);
        }
    }

    /**
     * 将文本输入流写入一个文本输出流。块大小为 8192
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输入/出流
     *
     * @param writer 输出流
     * @param reader 输入流
     * @throws IOException
     */
    public static long write(Writer writer, Reader reader) throws IOException {
        if (null == writer || null == reader)
            return 0;

        char[] cbuf = new char[BUF_SIZE];
        int len, count = 0;
        while (true) {
            len = reader.read(cbuf);
            if (len == -1)
                break;
            writer.write(cbuf, 0, len);
            count += len;
        }
        return count;
    }




    /**
     * 将一个字节数组写入一个输出流。
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输出流
     *
     * @param ops   输出流
     * @param bytes 字节数组
     * @throws IOException
     */
    public static void write(OutputStream ops, byte[] bytes) throws IOException {
        if (null == ops || null == bytes || bytes.length == 0)
            return;
        ops.write(bytes);
    }


    /**
     * 将一个字节数组写入一个输出流。
     * <p>
     * <b style=color:red>注意</b>，它会关闭输出流
     *
     * @param ops   输出流
     * @param bytes 字节数组
     */
    public static void writeAndClose(OutputStream ops, byte[] bytes) {
        try {
            write(ops, bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            safeClose(ops);
        }
    }

    /**
     * 从一个文本流中读取全部内容并返回
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输出流
     *
     * @param reader 文本输出流
     * @return 文本内容
     * @throws IOException
     */
    public static StringBuilder read(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        read(reader, sb);
        return sb;
    }

    /**
     * 从一个文本流中读取全部内容并返回
     * <p>
     * <b style=color:red>注意</b>，它会关闭输入流
     *
     * @param reader 文本输入流
     * @return 文本内容
     * @throws IOException
     */
    public static String readAndClose(Reader reader) {
        try {
            return read(reader).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            safeClose(reader);
        }
    }

    /**
     * 从一个文本流中读取全部内容并写入缓冲
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输出流
     *
     * @param reader 文本输出流
     * @param sb     输出的文本缓冲
     * @return 读取的字符数量
     * @throws IOException
     */
    public static int read(Reader reader, StringBuilder sb) throws IOException {
        char[] cbuf = new char[BUF_SIZE];
        int count = 0;
        int len;
        while (-1 != (len = reader.read(cbuf))) {
            sb.append(cbuf, 0, len);
            count += len;
        }
        return count;
    }

    /**
     * 从一个文本流中读取全部内容并写入缓冲
     * <p>
     * <b style=color:red>注意</b>，它会关闭输出流
     *
     * @param reader 文本输出流
     * @param sb     输出的文本缓冲
     * @return 读取的字符数量
     */
    public static int readAndClose(InputStreamReader reader, StringBuilder sb) {
        try {
            return read(reader, sb);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            safeClose(reader);
        }
    }

    /**
     * 读取一个输入流中所有的字节
     *
     * @param ins 输入流，必须支持 available()
     * @return 一个字节数组
     * @throws IOException
     */
    public static byte[] readBytes(InputStream ins) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        write(out, ins);
        return out.toByteArray();
    }

    /**
     * 读取一个输入流中所有的字节，并关闭输入流
     *
     * @param ins 输入流，必须支持 available()
     * @return 一个字节数组
     * @throws IOException
     */
    public static byte[] readBytesAndClose(InputStream ins) {
        byte[] bytes = null;
        try {
            bytes = readBytes(ins);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            safeClose(ins);
        }
        return bytes;
    }


}
