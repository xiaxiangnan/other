package com.journey.other;


import java.util.Arrays;

/**
 * for test
 * Created by xiaxiangnan on 16/5/16.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        String[] arr = new String[]{"a", "b", "c"};
        String[] rs = Arrays.stream(arr).map(String::toUpperCase).toArray(String[]::new);
    }


}
