package com.journey.other.reference;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * 软引用
 * Created by xiaxiangnan on 16/5/16.
 */
public class SoftReferenceTest {

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("SoftReferenceTest's finalize called"); //被回收时输出
    }

    @Override
    public String toString() {
        return "I am SoftReferenceTest";
    }

    //-Xmx5M -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
    public static void main(String[] args) {
        SoftReferenceTest obj = new SoftReferenceTest(); //强引用
        //引用队列,当对象被回收时会加入队列
        ReferenceQueue<SoftReferenceTest> softQueue = new ReferenceQueue<>();
        SoftReference<SoftReferenceTest> softRef = new SoftReference<>(obj, softQueue);

        //监控引用队列
        new Thread(() -> {
            Reference<SoftReferenceTest> ref = null;
            try {
                ref = (Reference<SoftReferenceTest>) softQueue.remove(); //blocking until one becomes available.
            } catch (InterruptedException e) {
            }
            if (ref != null) {
                System.out.println("[Monitor] softQueue get SoftReference is: " + ref.get());
            }

        }).start();

        obj = null; //删除强引用
        System.gc();
        //内存充足不会被回收
        System.out.println("After GC: Soft Get: " + softRef.get());
        byte[] b = new byte[4 * 1024 * 890]; //分配大内存,强迫GC
        //内存紧张被回收
        System.out.println("After new big bytes: Soft Get: " + softRef.get());

    }

}
