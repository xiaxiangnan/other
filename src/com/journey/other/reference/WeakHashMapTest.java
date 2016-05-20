package com.journey.other.reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * WeakHashMap构造key的弱引用,内存紧张时清理持有弱引用key的项
 * Created by xiaxiangnan on 16/5/17.
 */
public class WeakHashMapTest {

    //-Xmx5M
    public static void main(String[] args) {
        Map<Integer, byte[]> map = new WeakHashMap<>();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            Integer ii = new Integer(i);
            list.add(ii); //key有强引用,WeakHashMap就退化为普通的HashMap无法自动清理而OOM
            map.put(ii, new byte[i]);
        }
        System.out.println(map.size());
    }

}
