package com.quick.hui.crawler.common;


import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by yihui on 2017/5/6.
 */
public class FileReadUtil {

    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     *
     * @param fileName 文件的名
     */
    public static InputStream createByteRead(String fileName) throws IOException {

//        File file = new File(fileName);
//
//        return new FileInputStream(file);
        return getStreamByFileName(fileName);
    }


    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     *
     * @param fileName 文件名
     */
    public static Reader createCharRead(String fileName) throws IOException {
//        File file = new File(fileName);
//        return new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"));

        return new InputStreamReader(getStreamByFileName(fileName), Charset.forName("UTF-8"));
    }


    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     *
     * @param fileName 文件名
     */
    public static BufferedReader createLineRead(String fileName) throws IOException {
//        File file = new File(fileName);
////        return new BufferedReader(new FileReader(file));
//        return new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));

        return new BufferedReader(new InputStreamReader(getStreamByFileName(fileName), Charset.forName("UTF-8")));
    }


    public static InputStream getStreamByFileName(String fileName) throws IOException {
        check(fileName);

        if (fileName.startsWith("http")) { // 网络地址
            URL url = new URL(fileName);
            return url.openStream();
        } else if (fileName.startsWith("/")) { // 绝对路径
            Path path = Paths.get(fileName);
            return Files.newInputStream(path);
        } else { // 相对路径
            return FileReadUtil.class.getClassLoader().getResourceAsStream(fileName);
        }
    }


    public static File getFile(String fileName) throws IOException {
        check(fileName);

        if (fileName.startsWith("http")) { // 网络地址
            URL url = new URL(fileName);
            fileName = url.getFile();
        } else if (!fileName.startsWith("/")){ // 相对路径
            URL url = FileReadUtil.class.getClassLoader().getResource(fileName);
            check(url, "System do not have this file : " + fileName);
            fileName = url.getFile();
        }

        return new File(fileName);
    }



    private static void check(Object arg) {
        check(arg, "params should not be null!");
    }

    private static void check(Object arg, String msg) {
        if (arg == null) {
            throw new IllegalArgumentException(msg);
        }
    }

}
