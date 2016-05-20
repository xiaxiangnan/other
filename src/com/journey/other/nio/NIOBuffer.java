package com.journey.other.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Java NIO中的Buffer用于和NIO通道进行交互。数据是从通道读入缓冲区，从缓冲区写入到通道中的。
 * 缓冲区本质上是一块可以写入数据，然后可以从中读取数据的内存。这块内存被包装成NIO Buffer对象，并提供了一组方法，用来方便的访问该块内存。
 * 使用Buffer读写数据一般遵循以下四个步骤：
 * a.写入数据到Buffer
 * b.调用flip()方法
 * c.从Buffer中读取数据
 * d.调用clear()方法或者compact()方法
 * 当向buffer写入数据时，buffer会记录下写了多少数据。一旦要读取数据，需要通过flip()方法将Buffer从写模式切换到读模式。
 * 在读模式下，可以读取之前写入到buffer的所有数据。一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入。
 * 有两种方式能清空缓冲区：调用clear()或compact()方法。clear()方法会清空整个缓冲区。compact()方法只会清除已经读过的数据。
 * 任何未读的数据都被移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面。
 * <p>
 * Buffer的三个重要参数:
 * 1. 位置(position): 当前缓冲区的位置
 * 2. 容量(capacity): 缓冲区总容量上限
 * 3. 上限(limit): 实际上限,<=容量
 * <p>
 * Created by xiaxiangnan on 16/4/7.
 */
public class NIOBuffer {

    /**
     * Buffer创建,重置,清空(并不是真正的清空)
     */
    public static void initBuffer() {
        //1. 从堆中分配
        ByteBuffer buffer1 = ByteBuffer.allocate(20);
        //2. 从已有数组中创建
        byte[] arrays = new byte[20];
        ByteBuffer buffer2 = ByteBuffer.wrap(arrays);
        System.out.println("create---" + buffer1);
        buffer1.put((byte) 1);
        buffer1.put((byte) 2);
        System.out.println("put 2 byte---" + buffer1);

        //1. rewind: 将position置0,并清除标志位(mark).为提取Buffer的有效数据做准备
        buffer1.rewind();
        System.out.println("rewind---" + buffer1);

        buffer1.put((byte) 1);
        //2. clear: 将position置0,将limit置为capacity,并清除标志位(mark).为重新写buffer做准备
        buffer1.clear();
        System.out.println("clear---" + buffer1);

        buffer1.put((byte) 1);
        //3. flip: 将limit设置为position,然后将position置0,并清除标志位(mark),读写转换时使用
        buffer1.flip();
        System.out.println("flip---" + buffer1);
    }

    /**
     * 读写Buffer操作
     */
    public static void readWrite() {
        ByteBuffer buffer = ByteBuffer.allocate(20);
        /**
         * put
         */
        buffer.put((byte) 1); //当前位置写入,position后移一位
        System.out.println("put(byte)--position: " + buffer.position());
        buffer.put(10, (byte) 10); //写入buffer的index(10)的位置,不移动position
        System.out.println("put(index,byte)--position: " + buffer.position());
        buffer.put(new byte[]{(byte) 2, (byte) 3});//当前位置写入,position后移对应位置
        System.out.println("put(byte[])--position: " + buffer.position());

        buffer.rewind();

        /**
         * get
         */
        byte data1 = buffer.get(); //返回position当前数据,position后移一位
        System.out.println("get()--position: " + buffer.position() + ", data: " + data1);
        byte[] arrays = new byte[10];
        buffer.get(arrays); //buffer数据写到arrays数组中,移动到对应的position
        System.out.println("get(byte[])--position: " + buffer.position() + ", data: " + arrays[0]);
        byte data2 = buffer.get(10); //给的index(10)的数据,不移动position
        System.out.println("get(index)--position: " + buffer.position() + ", data: " + data2);

    }

    /**
     * 标记缓冲区
     */
    public static void mark() {
        ByteBuffer buffer = ByteBuffer.allocate(20);
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.mark(); //标记当天位置
        System.out.println("mart position: " + buffer.position());
        buffer.put((byte) 3);
        buffer.reset(); //恢复到mark所在位置
        System.out.println("reset position: " + buffer.position());
    }

    /**
     * 复制缓冲区:
     * 生成一个完全一样的缓冲区,共享内存数据.但他们独立维护自己的position,limit,mark
     */
    public static void duplicate() {
        ByteBuffer buffer = ByteBuffer.allocate(20);
        buffer.put((byte) 1);
        buffer.put((byte) 2);

        ByteBuffer duplicateBuffer = buffer.duplicate();
        buffer.rewind();
        System.out.println("src buffer: " + buffer);
        System.out.println("duplicate buffer" + duplicateBuffer);

        duplicateBuffer.put(3, (byte) 3);
        System.out.println("src buffer data[3]: " + buffer.get(3));
    }

    /**
     * 缓冲区分片:
     * 在现有的缓冲区中创建子缓冲区,共享数据.只处理buffer的一个片段,有助于模块化
     */
    public static void slice() {
        ByteBuffer buffer = ByteBuffer.allocate(20);
        for (int i = 0; i < 10; i++) {
            buffer.put((byte) i);
        }
        buffer.position(2);
        buffer.limit(6);
        System.out.println("buffer: " + buffer);
        ByteBuffer subBuffer = buffer.slice();
        System.out.println("subBuffer" + subBuffer + ", data[1]: " + subBuffer.get(1));

        subBuffer.put((byte) 20);
        System.out.println("buffer data[2]: " + buffer.get(2));

    }

    /**
     * 只读缓冲区
     * 只读buffer不可修改数据,和原始缓存区共享内存快,因此原始改动对只读可见
     */
    public static void onlyRead() {
        ByteBuffer buffer = ByteBuffer.allocate(20);
        for (int i = 0; i < 10; i++) {
            buffer.put((byte) i);
        }

        ByteBuffer onlyReadBuffer = buffer.asReadOnlyBuffer(); //创建只读缓冲区
//        onlyReadBuffer.put((byte)1); //抛异常
    }

    /**
     * 文件映射到内存
     */
    public static void mappedBuffer() throws Exception {
        try (RandomAccessFile raf = new RandomAccessFile("/Users/didi/Documents/IDEA-workspace/other/src/com/journey/other/nio/structData.txt", "rw")) {
            FileChannel fc = raf.getChannel();
            //将文件映射到内存
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, raf.length());
//            while (mbb.hasRemaining()) {
//                System.out.println((char)mbb.get());
//            }
            mbb.put(0, (byte) 77); //通过修改buffer,修改文件
        }
    }

    /**
     * 处理结构化数据
     * 1. 散射(ScatteringByteChannel): 将数据读入一组buffer,填满一个就开填充下一个
     * 2. 聚合(GatheringByteChannel): 将一组buffer写入Channel
     */
    public static void structData() throws Exception {
        ByteBuffer write1 = ByteBuffer.wrap("i am Chinese".getBytes());
        ByteBuffer write2 = ByteBuffer.wrap("我是中国人".getBytes("UTF-8"));
        int length1 = write1.limit();
        int length2 = write2.limit();
        System.out.println("buffer1: " + write1);
        System.out.println("buffer2: " + write2);
        ByteBuffer[] buffers = new ByteBuffer[]{write1, write2};
        File file = new File("/Users/didi/Documents/IDEA-workspace/other/src/com/journey/other/nio/structData.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            FileChannel fc = fos.getChannel();
            fc.write(buffers); //聚合,buffers写入文件
        }

        ByteBuffer read1 = ByteBuffer.allocate(length1);
        ByteBuffer read2 = ByteBuffer.allocate(length2);
        buffers = new ByteBuffer[]{read1, read2};
        file = new File("/Users/didi/Documents/IDEA-workspace/other/src/com/journey/other/nio/structData.txt");
        try (FileInputStream fis = new FileInputStream(file)) {
            FileChannel fc = fis.getChannel();
            fc.read(buffers); //散射,读入数据到buffers
            System.out.println(new String(read1.array(), "UTF-8"));
            System.out.println(new String(read2.array(), "UTF-8"));
        }
    }

    /**
     * 直接内存访问: 更接近系统底层的方法,比普通的buffer更快,但创建销毁的代价很高
     * 普通的buffer在jvm的堆上分配,而DirectBuffer直接分配在物理内存中,不占用堆内存
     */
    public static void directBuffer() throws Exception {
        monitorDirectBuffer();
        ByteBuffer direct = ByteBuffer.allocateDirect(2000); //分配
        monitorDirectBuffer();
    }

    /**
     * 监控DirectBuffer的使用情况
     */
    private static void monitorDirectBuffer() throws Exception {
        Class c = Class.forName("java.nio.Bits");
        Field maxMemory = c.getDeclaredField("maxMemory");
        maxMemory.setAccessible(true);
        Field reservedMemory = c.getDeclaredField("reservedMemory");
        reservedMemory.setAccessible(true);

        synchronized (c) {
            Long maxMemoryValue = (Long) maxMemory.get(null); //总大小
            Long reservedMemoryValue = (Long) reservedMemory.get(null); //使用大小
            System.out.println("maxMemoryValue: " + maxMemoryValue + ", reservedMemoryValue: " + reservedMemoryValue);
        }

    }


    public static void main(String[] args) throws Exception {
        System.out.println("-------------initBuffer---------------");
        initBuffer();
        System.out.println("-------------readWrite---------------");
        readWrite();
        System.out.println("-------------mark---------------");
        mark();
        System.out.println("-------------duplicate---------------");
        duplicate();
        System.out.println("-------------slice---------------");
        slice();
        System.out.println("-------------onlyRead---------------");
        onlyRead();
        System.out.println("-------------mappedBuffer---------------");
        mappedBuffer();
        System.out.println("-------------structData---------------");
        structData();
        System.out.println("-------------directBuffer---------------");
        directBuffer();
    }

}
