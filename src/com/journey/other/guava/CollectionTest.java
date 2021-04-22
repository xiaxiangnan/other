package com.journey.other.guava;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Throwables;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Table;

/**
 * @author xiaxiangnan <xiaxiangnan@kuaishou.com>
 * Created on 2019-12-06
 */
public class CollectionTest {

    public static void multiset() {
        System.out.println("--------------multiset-------------------");
        Multiset<String> multiset = HashMultiset.create();
        multiset.add("a", 2);
        multiset.add("f");
        multiset.add("d");
        multiset.add("d");
        System.out.println(multiset.size());
        System.out.println(multiset.count("d"));
        System.out.println(Arrays.toString(multiset.toArray()));
    }

    public static void immutable() {
        System.out.println("---------------immutable------------------");
        List<String> list = ImmutableList.of("a", "b", "c");
        try {
            list.add("d");
        } catch (Exception e) {
            System.out.println("immutable add error: " + e.getClass());
        }
    }

    public static void multimap() {
        System.out.println("----------------multimap-----------------");
        Multimap<String, String> map = HashMultimap.create();
        map.put("k1", "v1");
        map.put("k1", "v2");
        map.put("k2", "v2");
        map.put("k3", "v2");
        System.out.println(map.get("k1"));
        System.out.println(map.toString());
    }

    public static void biMap() {
        System.out.println("---------------biMap------------------");
        BiMap<String, Integer> map = HashBiMap.create();
        map.put("a", 1);
        try {
            map.put("b", 1);
        } catch (RuntimeException e) {
            System.out.println("BiMap value duplicate.error: " + Throwables.getStackTraceAsString(e));
        }
        System.out.println(map.toString());
        System.out.println("inverse: " + map.inverse().toString());
        map.forcePut("c", 1);
        System.out.println("inverse: " + map.inverse().toString());
        map.inverse().put(1, "d");
        System.out.println(map.toString());
    }

    public static void table() {
        System.out.println("---------------table------------------");
        Table<String, String, String> table = HashBasedTable.create();
        table.put("IBM", "101", "Mahesh");
        table.put("IBM", "102", "Ramesh");
        table.put("IBM", "111", "Suresh");

        table.put("Microsoft", "111", "Sohan");
        table.put("Microsoft", "112", "Mohan");
        table.put("Microsoft", "113", "Rohan");

        table.put("TCS", "111", "Ram");
        table.put("TCS", "111", "Shyam");
        table.put("TCS", "123", "Sunil");

        System.out.println(table.toString());
        System.out.println(table.row("IBM"));
        System.out.println(table.column("111"));
        System.out.println(table.get("IBM", "101"));
        System.out.println(table.get("TCS", "111"));
    }


    public static void main(String[] args) {
//        multiset();
//        immutable();
//        multimap();
//        biMap();
        table();
    }

}
