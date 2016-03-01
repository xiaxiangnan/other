package com.journey.other.jdk78new;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 并行（parallel）数组
 * Java 8增加了大量的新方法来对数组进行并行处理
 * Created by xiaxiangnan on 16/2/26.
 */
public class ParallelArrays {
    public static void main(String[] args) {
        long[] arrayOfLong = new long[20000];

        Arrays.parallelSetAll(arrayOfLong,
                index -> ThreadLocalRandom.current().nextInt(1000000));
        Arrays.stream(arrayOfLong).limit(10).forEach(
                i -> System.out.print(i + " "));
        System.out.println();
        //parallelSort可以在多核机器上极大提高数组排序的速度
        Arrays.parallelSort(arrayOfLong);
        Arrays.stream(arrayOfLong).limit(10).forEach(
                i -> System.out.print(i + " "));
        System.out.println();
    }
}
