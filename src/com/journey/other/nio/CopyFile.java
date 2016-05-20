package com.journey.other.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * nio进行简单的文件复制
 * Created by xiaxiangnan on 16/4/7.
 */
public class CopyFile {

    public static void nioCopyFile(String res, String des) throws Exception {
        FileInputStream fis = new FileInputStream(res);
        FileOutputStream fos = new FileOutputStream(des);
        //读文件通道,写文件通道
        try (FileChannel readChannel = fis.getChannel();
             FileChannel writeChannel = fos.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(1024); //读入数据缓存区,1024字节

            while (true) {
                buffer.clear();
                int len = readChannel.read(buffer); //读入数据
                if (len == -1) {
                    break;
                }
                buffer.flip();
                writeChannel.write(buffer); //写入数据
            }
        }
    }

    public static void main(String[] args) {

    }

}
