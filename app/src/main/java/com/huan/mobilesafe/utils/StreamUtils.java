package com.huan.mobilesafe.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * StreamUtils 读取流的工具类
 *
 * @author: 欢
 * @time: 2016/2/2 17:44
 */
public class StreamUtils {

    public static String readFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //初始化读取内容长度为0
        int len;
        //创建字节数组缓冲区(缓冲区大小为1KB)
        byte[] buffer = new byte[1024];

        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }

        String result = out.toString();
        in.close();
        out.close();

        return result;
    }
}
