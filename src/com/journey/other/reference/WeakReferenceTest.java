package com.journey.other.reference;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * 弱引用
 * Created by xiaxiangnan on 16/5/16.
 */
public class WeakReferenceTest {
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("WeakReferenceTest's finalize called"); //被回收时输出
    }

    @Override
    public String toString() {
        return "I am WeakReferenceTest";
    }

    //-XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
    public static void main(String[] args) {
        WeakReferenceTest obj = new WeakReferenceTest(); //强引用
        //引用队列,当对象被回收时会加入队列
        ReferenceQueue<WeakReferenceTest> weakQueue = new ReferenceQueue<>();
        WeakReference<WeakReferenceTest> weakRef = new WeakReference<>(obj, weakQueue);

        //监控引用队列
        new Thread(() -> {
            Reference<WeakReferenceTest> ref = null;
            try {
                ref = (Reference<WeakReferenceTest>) weakQueue.remove(); //blocking until one becomes available.
            } catch (InterruptedException e) {
            }
            if (ref != null) {
                System.out.println("[Monitor] weakQueue get WeakReference is: " + ref.get());
            }

        }).start();

        obj = null; //删除强引用
        System.out.println("Before GC: Weak Get: " + weakRef.get());
        System.gc();
        //垃圾回收一旦发现就被回收
        System.out.println("After GC: Weak Get: " + weakRef.get());

    }
}
