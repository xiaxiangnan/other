package com.journey.other.optimize;

/**
 * StringBuilder初始化容量设置.(arraylist,hashmap等类同)
 * Created by xiaxiangnan on 16/4/6.
 */
public class StringCapacity {

    /**
     * 默认char16,之后会2倍扩容,并拷贝内容,效率低
     */
    public static void noCapacity() {
        StringBuilder sb = new StringBuilder();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            sb.append(i);
        }
        System.out.println("noCapacity cost: " + (System.currentTimeMillis() - start) + "ms");
    }

    public static void setCapacity() {
        StringBuilder sb = new StringBuilder(110000);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            sb.append(i);
        }
        System.out.println("setCapacity cost: " + (System.currentTimeMillis() - start) + "ms");
    }

    public static void main(String[] args) {
        noCapacity();
        setCapacity();
    }

}
