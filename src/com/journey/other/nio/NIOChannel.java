package com.journey.other.nio;

import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Java NIO的通道类似流，但又有些不同：
 * 既可以从通道中读取数据，又可以写数据到通道。但流的读写通常是单向的。
 * 通道可以异步地读写。
 * 通道中的数据总是要先读到一个Buffer，或者总是要从一个Buffer中写入。
 * Java NIO中最重要的通道的实现：
 * FileChannel 从文件中读写数据。
 * DatagramChannel 能通过UDP读写网络中的数据。
 * SocketChannel 能通过TCP读写网络中的数据。
 * ServerSocketChannel 可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel。
 * <p>
 * Created by xiaxiangnan on 16/5/3.
 */
public class NIOChannel {

    /**
     * FileChannel: 是一个连接到文件的通道
     * FileChannel无法设置为非阻塞模式，它总是运行在阻塞模式下。
     */
    public static void fileChannel() throws Exception {
        RandomAccessFile aFile = new RandomAccessFile("/Users/didi/Documents/IDEA-workspace/other/src/com/journey/other/nio/structData.txt", "rw");
        FileChannel fileChannel = aFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(48);
        /**
         * 从FileChannel读取数据:
         * 将数据从FileChannel读取到Buffer中,read()方法返回的int值表示了有多少字节被读到了Buffer中。
         * 如果返回-1，表示到了文件末尾。
         */
        int bytesRead = fileChannel.read(buf);
        /**
         * 向FileChannel写数据
         */
        fileChannel.write(buf);

        //FileChannel的当前位置
        long pos = fileChannel.position();
        //设置FileChannel的当前位置。
        fileChannel.position(pos + 123);
        //所关联文件的大小
        long fileSize = fileChannel.size();
        //截取一个文件,将中指定长度后面的部分将被删除
        fileChannel.truncate(1024);
        //将通道里尚未写入磁盘的数据强制写到磁盘上,boolean类型的参数，指明是否同时将文件元数据（权限信息等）写到磁盘上
        fileChannel.force(true);

        fileChannel.close();
        aFile.close();
    }

    /**
     * SocketChannel: 连接到TCP网络套接字的通道
     */
    public static void socketChannel() throws Exception {
        //打开SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("http://google.com", 9999));

        //设置SocketChannel为非阻塞模式(non-blocking mode),就可以在异步模式下调用connect,read和write
        socketChannel.configureBlocking(false);
        while (!socketChannel.finishConnect()) {
            //wait, or do something else...
        }

        socketChannel.close();
    }

    /**
     * ServerSocketChannel: 可以监听新进来的TCP连接的通道, 就像标准IO中的ServerSocket一样
     */
    public static void serverSocketChannel() throws Exception {
        //打开ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));

        //监听新进来的连接,accept()方法会一直阻塞到有新连接到达
        SocketChannel socketChannel = serverSocketChannel.accept();
        //do something with socketChannel...

        //设置ServerSocketChannel为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //在非阻塞模式下，accept() 方法会立刻返回，如果还没有新进来的连接,返回的将是null
        SocketChannel socketChannel2 = serverSocketChannel.accept();
        if (socketChannel2 != null) {
            //do something with socketChannel...
        }

    }


}
