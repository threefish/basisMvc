package com.sgaop.basis.mvc.view;

import com.sgaop.basis.constant.Constant;
import com.sgaop.basis.util.IoTool;
import com.sgaop.basis.util.Logs;
import com.sgaop.basis.util.StringsTool;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/10 0010
 * To change this template use File | Settings | File Templates.
 * 默认视图
 */
public class RawViewRender {

    private static final Logger logger = Logs.get();

    public static final boolean DISABLE_RANGE_DOWNLOAD = false; //禁用断点续传

    private static final int big4G = Integer.MAX_VALUE;

    protected static final Map<String, String> contentTypeMap = new HashMap<String, String>();

    static {
        contentTypeMap.put("xml", "application/xml");
        contentTypeMap.put("html", "text/html");
        contentTypeMap.put("htm", "text/html");
        contentTypeMap.put("stream", "application/octet-stream");
        contentTypeMap.put("js", "application/javascript");
        contentTypeMap.put("json", "application/json");
        contentTypeMap.put("jpg", "image/jpeg");
        contentTypeMap.put("jpeg", "image/jpeg");
        contentTypeMap.put("png", "image/png");
        contentTypeMap.put("webp", "image/webp");
    }


    public static void RenderRaw(HttpServletRequest req, HttpServletResponse resp, Object obj, String contentType) {
        try {

            if (obj instanceof BufferedImage) {
                OutputStream out = resp.getOutputStream();
                if (contentType.contains("png"))
                    ImageIO.write((BufferedImage) obj, "png", out);
                else if (contentType.contains("webp"))
                    ImageIO.write((BufferedImage) obj, "webp", out);
                else
                    ImageIO.write((BufferedImage) obj, "jpg", out);
                return;
            }
            // 文件
            else if (obj instanceof File || obj instanceof DownFile) {
                File file;
                String fileName ;
                if (obj instanceof DownFile) {
                    DownFile downFile = (DownFile) obj;
                    file = downFile.getFile();
                    if (StringsTool.isNullorEmpty(downFile.getName())) {
                        fileName = file.getName();
                    } else {
                        fileName = downFile.getName();
                    }
                } else {
                    file = (File) obj;
                    fileName = file.getName();
                }

                long fileSz = file.length();
                if (!file.exists() || file.isDirectory()) {
                    logger.debug("File downloading ... Not Exist : " + file.getAbsolutePath());
                    resp.sendError(404);
                    return;
                }
                if (!resp.containsHeader("Content-Disposition")) {
                    String filename = URLEncoder.encode(fileName, Constant.utf8);
                    resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                }

                String rangeStr = req.getHeader("Range");
                OutputStream out = resp.getOutputStream();
                if (DISABLE_RANGE_DOWNLOAD
                        || fileSz == 0
                        || (rangeStr == null || !rangeStr.startsWith("bytes=") || rangeStr.length() < "bytes=1".length())) {
                    resp.setHeader("Content-Length", "" + fileSz);
                    IoTool.writeAndClose(out, new FileInputStream(file));
                } else {
                    List<RangeRange> rs = new ArrayList<RangeRange>();
                    if (!parseRange(rangeStr, rs, fileSz)) {
                        resp.setStatus(416);
                        return;
                    }
                    // 暂时只实现了单range
                    if (rs.size() != 1) {
                        // TODO 完成多range的下载
                        logger.info("multipart/byteranges is NOT support yet");
                        resp.setStatus(416);
                        return;
                    }
                    long totolSize = 0;
                    for (RangeRange rangeRange : rs) {
                        totolSize += (rangeRange.end - rangeRange.start);
                    }
                    resp.setStatus(206);
                    resp.setHeader("Content-Length", "" + totolSize);
                    resp.setHeader("Accept-Ranges", "bytes");

                    // 暂时只有单range,so,简单起见吧
                    RangeRange rangeRange = rs.get(0);
                    resp.setHeader("Content-Range", String.format("bytes %d-%d/%d",
                            rangeRange.start,
                            rangeRange.end - 1,
                            fileSz));
                    writeFileRange(file, out, rangeRange);
                }
            }
            // 字节数组
            else if (obj instanceof byte[]) {
                resp.setHeader("Content-Length", "" + ((byte[]) obj).length);
                OutputStream out = resp.getOutputStream();
                IoTool.writeAndClose(out, (byte[]) obj);
            }
            // 字符数组
            else if (obj instanceof char[]) {
                Writer writer = resp.getWriter();
                writer.write((char[]) obj);
                writer.flush();
            }
            // 文本流
            else if (obj instanceof Reader) {
                IoTool.writeAndClose(resp.getWriter(), (Reader) obj);
            }
            // 二进制流
            else if (obj instanceof InputStream) {
                OutputStream out = resp.getOutputStream();
                IoTool.writeAndClose(out, (InputStream) obj);
            }
            //普通对象
            else if (obj != null) {
                byte[] data = String.valueOf(obj).getBytes(Constant.utf8);
                resp.setHeader("Content-Length", "" + data.length);
                OutputStream out = resp.getOutputStream();
                IoTool.writeAndClose(out, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("raw视图出错", e);
            throw new RuntimeException(e);
        }
    }


    public static class RangeRange {
        public RangeRange(long start, long end) {
            this.start = start;
            this.end = end;
        }

        long start;
        long end = -1;

    }

    public static final boolean parseRange(String rangeStr, List<RangeRange> rs, long maxSize) {
        rangeStr = rangeStr.substring("bytes=".length());
        String[] ranges = rangeStr.split(",");
        for (String range : ranges) {
            if (StringsTool.isNullorEmpty(range)) {
                logger.debug("Bad Range -->    " + rangeStr);
                return false;
            }
            range = range.trim();
            try {
                // 首先是从后往前算的 bytes=-100 取最后100个字节
                if (range.startsWith("-")) {

                    // 注意,这里是负数
                    long end = Long.parseLong(range);
                    long start = maxSize + end;
                    if (start < 0) {
                        logger.debug("Bad Range -->    " + rangeStr);
                        return false;
                    }
                    rs.add(new RangeRange(start, maxSize));
                    continue;
                }

                // 然后就是从开头到最后 bytes=1024-
                if (range.endsWith("-")) {
                    // 注意,这里是负数
                    long start = Long.parseLong(range.substring(0, range.length() - 1));
                    if (start < 0) {
                        logger.debug("Bad Range -->    " + rangeStr);
                        return false;
                    }
                    rs.add(new RangeRange(start, maxSize));
                    continue;
                }

                // 哦也,是最标准的有头有尾?
                if (range.contains("-")) {
                    String[] tmp = range.split("-");
                    long start = Long.parseLong(tmp[0]);
                    long end = Long.parseLong(tmp[1]);
                    if (start > end) {
                        logger.debug("Bad Range -->    " + rangeStr);
                        return false;
                    }
                    rs.add(new RangeRange(start, end + 1)); // 这里需要调查一下
                } else {
                    // 操!! 单个字节?!!
                    long start = Long.parseLong(range);
                    rs.add(new RangeRange(start, start + 1));
                }
            } catch (Throwable e) {
                logger.debug("Bad Range -->    " + rangeStr, e);
                return false;
            }
        }
        return !rs.isEmpty();
    }

    public static void writeDownloadRange(DataInputStream in,
                                          OutputStream out,
                                          RangeRange rangeRange) {
        try {
            if (rangeRange.start > 0) {
                long start = rangeRange.start;
                while (start > 0) {
                    if (start > big4G) {
                        start -= big4G;
                        in.skipBytes(big4G);
                    } else {
                        in.skipBytes((int) start);
                        break;
                    }
                }
            }
            byte[] buf = new byte[8192];
            BufferedInputStream bin = new BufferedInputStream(in);
            long pos = rangeRange.start;
            int len = 0;
            while (pos < rangeRange.end) {
                if (rangeRange.end - pos > 8192) {
                    len = bin.read(buf);
                } else {
                    len = bin.read(buf, 0, (int) (rangeRange.end - pos));
                }
                if (len == -1) {// 有时候,非常巧合的,文件已经读取完,就悲剧开始了...
                    break;
                }
                if (len > 0) {
                    out.write(buf, 0, len);
                    pos += len;
                }
            }
            out.flush();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeFileRange(File file, OutputStream out, RangeRange rangeRange) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fin);
            writeDownloadRange(in, out, rangeRange);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            IoTool.safeClose(fin);
        }
    }

}
