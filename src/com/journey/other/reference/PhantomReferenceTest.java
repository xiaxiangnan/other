package com.journey.other.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * 虚引用
 * Created by xiaxiangnan on 16/5/16.
 */
public class PhantomReferenceTest {

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("PhantomReferenceTest's finalize called"); //被回收时输出
    }

    @Override
    public String toString() {
        return "I am PhantomReferenceTest";
    }

    //-XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
    public static void main(String[] args) throws InterruptedException {
        PhantomReferenceTest obj = new PhantomReferenceTest(); //强引用
        //引用队列,当对象被回收时会加入队列
        ReferenceQueue<PhantomReferenceTest> phantomQueue = new ReferenceQueue<>();
        //创建虚引用
        PhantomReference<PhantomReferenceTest> phantomRef = new PhantomReference<>(obj, phantomQueue);
        //虚引用的get总返回null,即使强引用对象存在
        System.out.println("Phantom get: " + phantomRef.get());

        //监控引用队列
        new Thread(() -> {
            Reference<PhantomReferenceTest> ref = null;
            try {
                ref = (Reference<PhantomReferenceTest>) phantomQueue.remove(); //blocking until one becomes available.
            } catch (InterruptedException e) {
            }
            System.out.println("[Monitor] phantomQueue get PhantomReference is: " + ref.get());
            System.exit(0);
        }).start();

        obj = null; //去除强引用
        int i = 1;
        //第一次gc找到垃圾对象并调用finalize方法,第二次gc被真正清除并加入引用队列
        while (true) {
            System.out.println("the " + i++ + " times gc");
            System.gc();
            Thread.sleep(1000L);
        }


    }
}
