package com.journey.other.jdk78new;

/**
 * 异常处理
 * Created by xiaxiangnan on 16/2/24.
 */

import java.io.*;

/**
 * Throwable类增加addSuppressed方法和getSuppressed方法，支持原始异常中加入被抑制的异常。
 * 异常抑制：在try和finally中同时抛出异常时，finally中抛出的异常会在异常栈中向上传递，而try中产生的原始异常会消失。
 * 在Java7之前的版本，可以将原始异常保存，在finally中产生异常时抛出原始异常;在Java7中的版本，可以使用addSuppressed方法记录被抑制的异常
 */

public class NewException {

    public static void main(String[] args) throws Exception {

        // catch子句可以同时捕获多个异常
        try {
            Integer.parseInt("Hello");
        } catch (NumberFormatException | NullPointerException e) {  //使用'|'分割，多个类型，一个对象e

        }


        /**
         * try-with-resources语句
         * Java7之前需要在finally中关闭 socket、文件、数据库连接等资源；
         * Java7中在try语句中申请资源，实现资源的自动释放（资源类必须实现java.lang.AutoCloseable接口，
         * 一般的文件、数据库连接等均已实现该接口，close方法将被自动调用）
         */
        try (BufferedReader reader = new BufferedReader(new FileReader(""))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(String.format("%n"));
            }
        }
        //try子句中可以管理多个资源
        try (InputStream input = new FileInputStream("");
             OutputStream output = new FileOutputStream("")) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
        }
    }


}
