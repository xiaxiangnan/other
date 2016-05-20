package com.journey.other.optimize;

import java.util.StringTokenizer;

/**
 * 字符串分割
 * Created by xiaxiangnan on 16/4/5.
 */
public class StringSplit {

    static String orgStr = null;

    static {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append(i).append(";");
        }
        orgStr = sb.toString();
    }

    /**
     * split(正则)功能强大,但效率低
     */
    public static void split() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            orgStr.split(";");
        }
        System.out.println("split cost: " + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * StringTokenizer效率高于split(jdk1.6)
     * StringTokenizer也适用到substring
     */
    public static void stringTokenizer() {
        StringTokenizer st = new StringTokenizer(orgStr, ";");
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            while (st.hasMoreTokens()) {
                st.nextToken();
            }
            st = new StringTokenizer(orgStr, ";");
        }
        System.out.println("stringTokenizer cost: " + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * indexOf+substring效率最高(jdk1.6),
     * jdk7开始string的substring采用Arrays.copy的形式替换了jdk6的复用数组的方式,
     * 空间换时间的方式使得substring的速度慢
     */
    public static void indexSubStr() {
        String temp = orgStr;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            while (true) {
                int j = temp.indexOf(";");
                if (j < 0) {
                    break;
                }
                temp.substring(0, j);
                temp = temp.substring(j + 1);
            }
            temp = orgStr;
        }
        System.out.println("indexSubStr cost: " + (System.currentTimeMillis() - start) + "ms");
    }

    public static void main(String[] args) {
        split();
        stringTokenizer();
        indexSubStr();
    }
}
