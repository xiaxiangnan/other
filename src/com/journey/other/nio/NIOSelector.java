package com.journey.other.nio;

import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Selector（选择器）是Java NIO中能够检测一到多个NIO通道，并能够知晓通道是否为诸如读写事件做好准备的组件。
 * 这样，一个单独的线程可以管理多个channel，从而管理多个网络连接。
 * 仅用单个线程来处理多个Channels的好处是，只需要更少的线程来处理通道。事实上，可以只用一个线程处理所有的通道。
 * 对于操作系统来说，线程之间上下文切换的开销很大，而且每个线程都要占用系统的一些资源（如内存）。因此，使用的线程越少越好。
 * 但是，需要记住，现代的操作系统和CPU在多任务方面表现的越来越好，所以多线程的开销随着时间的推移，变得越来越小了。
 * 实际上，如果一个CPU有多个内核，不使用多任务可能是在浪费CPU能力。
 * <p>
 * Created by xiaxiangnan on 16/5/3.
 */
public class NIOSelector {

    /**
     * 创建一个Selector
     */
    public static Selector create() throws Exception {
        return Selector.open();
    }

    /**
     * 向Selector注册通道
     */
    public static SelectionKey register(ServerSocketChannel channel, Selector selector) throws ClosedChannelException {
        //register()方法的第二个参数,是一个“interest集合”,意思是在通过Selector监听Channel时对什么事件感兴趣
        SelectionKey key = channel.register(selector, SelectionKey.OP_ACCEPT);
        // 对不止一种事件感兴趣,可以用“位或”操作符将常量连接起来
        // channel.register(selector, SelectionKey.OP_ACCEPT | SelectionKey.OP_CONNECT)
        return key;
    }

    /**
     * SelectionKey,包含了一些属性:
     * interest集合:  是你所选择的感兴趣的事件集合
     * ready集合: 通道已经准备就绪的操作的集合
     * Channel
     * Selector
     * 附加的对象（可选）:可以将一个对象或者更多信息附着到SelectionKey上，这样就能方便的识别某个给定的通道.
     */
    public static void selectionKey(SelectionKey selectionKey) {
        int interestSet = selectionKey.interestOps();
        boolean isInterestedInAccept = (interestSet & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT;
        boolean isInterestedInConnect = (interestSet & SelectionKey.OP_CONNECT) == SelectionKey.OP_CONNECT;
        boolean isInterestedInRead = (interestSet & SelectionKey.OP_READ) == SelectionKey.OP_READ;
        boolean isInterestedInWrite = (interestSet & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE;
        System.out.println("-----------interest集合--------------");
        System.out.println("isInterestedInAccept: " + isInterestedInAccept + ", isInterestedInConnect: "
                + isInterestedInConnect + ", isInterestedInRead: " + isInterestedInRead + ", isInterestedInWrite: " + isInterestedInWrite);

        int readySet = selectionKey.readyOps();
        selectionKey.isAcceptable();

    }

    /**
     * 打开一个Selector，注册一个通道注册到这个Selector上(通道的初始化过程略去),
     * 然后持续监控这个Selector的四种事件（接受，连接，读，写）是否就绪。
     */
    public static void demo(ServerSocketChannel channel) throws Exception {
        Selector selector = Selector.open();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
        while(true) {
            int readyChannels = selector.select();
            if(readyChannels == 0) continue;
            Set selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
            while(keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if(key.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel.
                } else if (key.isConnectable()) {
                    // a connection was established with a remote server.
                } else if (key.isReadable()) {
                    // a channel is ready for reading
                } else if (key.isWritable()) {
                    // a channel is ready for writing
                }
                keyIterator.remove();
            }
        }
    }


    public static void main(String[] args) throws Exception {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress(9999));
        // 与Selector一起使用时，Channel必须处于非阻塞模式下。这意味着不能将FileChannel与Selector一起使用，
        // 因为FileChannel不能切换到非阻塞模式。而套接字通道都可以。
        channel.configureBlocking(false);

        Selector selector = create();

        SelectionKey key = register(channel, selector);

        selectionKey(key);

        demo(channel);
    }

}
