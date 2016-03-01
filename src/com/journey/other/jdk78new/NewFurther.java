package com.journey.other.jdk78new;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jdk7&8 simple new further
 * Created by xiaxiangnan on 16/2/23.
 */
public class NewFurther {

    public static void main(String[] args) throws Exception {
        /**
         * 泛型实例化类型自动推断
         */
        Map<String, Integer> map = new HashMap<>();

        /**
         *  switch中增加对String类型的支持
         */
        String s = "good";
        switch (s) {
            case "good":
                System.out.println("this is good");
                break;
            case "bad":
                System.out.println("this is bad");
                break;
            default:
                System.out.println("this is default");
        }


        /**
         * 数字字面量的改进
         */
        // 支持二进制语法，用0b开头
        // 8位byte
        byte aByte = (byte) 0b00100001;
        // 16位short
        short aShort = (short) 0b1010000101000101;
        // 32位int
        int anInt1 = 0b10100001010001011010000101000101;
        int int10 = 123; //十进制
        int int8 = 0123; //八进制
        int int16 = 0X123; //十六进制
        System.out.println("int10: " + int10 + ", int8: " + int8 + ", int16: " + int16);
        // 支持单位级别的数字，提高可读性
        // 每三位加一下划线，等同于 9,223,372,036,854,775,807
        long underScores = 9_223_372_036_854_775_807L;
        int a = 1_000;


        /**
         * 变长参数方法的优化,在可变参数方法中传递非具体化参数,改进编译警告和错误
         */
        addToList(new ArrayList<>(), "");

        /**
         * 在Java8以前，内部类访问外部对象的一个本地变量,那么这个变量必须声明为final才行.
         * 在Java8中，这种限制被去掉了，代之以一个新的概念，“effectively final”,
         * 它的意思是你可以声明为final，也可以不声明final但是按照final来用，也就是一次赋值永不改变。
         * 换句话说，保证它加上final前缀后不会出编译错误。
         */
        Integer tmp = 9;
        new Runnable(){
            @Override
            public void run() {
                System.out.println(tmp);//没有声明为final，但是effectively final的本地变量
                //tmp += 1;  // 编译错！对tmp赋值导致它不是effectively final的
            }
        };


    }

    @SafeVarargs
    public static <T> void addToList (List<T> listArg, T... elements) {
        for (T x : elements) {
            listArg.add(x);
        }
    }



}
