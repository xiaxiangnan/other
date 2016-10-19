package com.journey.other.jdk78new;

/**
 * 在jdk7中，interned strings被分配在堆中.
 * 当调用intern方法时，先去查看Symbol table,如果有，直接返回引用。
 * 如果没有，再查看String table,如果有，则返回引用，如果在String table没有发现，
 * 则把该字符串的引用存在String table中。
 * Created by xiaxiangnan on 16/8/1.
 */
public class StringPool {

    public static void main(String[] args) throws Exception {

        //字符串常量内容"abc123"在类初始化的时候是以符号链接Symbol存放.
        //调用赋值,通过调用内部的StringTable::intern产生了String的引用，并且存放在常量池中。
        String s = "abc123";

        System.out.println(s.intern() == s); //true

        String a = new StringBuilder("abc").append("123").toString();
        //变量a.intern方法调用发现Symbol有直接返回引用,a的地址为新new的地址
        System.out.println(a.intern() == a); //false

        String b = new StringBuilder("zxc").append("456").toString();

        String c = new StringBuilder("zxc").append("456").toString();
        //c.intern发现Symbol和常量池都不存在,则加入池中.c的引用就是常量池的引用.
        System.out.println(c.intern() == b);//false
        System.out.println("zxc456".intern() == c); //true
        System.out.println("zxc456".intern() == b); //false


    }
}
