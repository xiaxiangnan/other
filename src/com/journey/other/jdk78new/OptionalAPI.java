package com.journey.other.jdk78new;

import java.util.Optional;

/**
 * Optional实际上是个容器：它可以保存类型T的值，或者仅仅保存null。
 * Optional提供很多有用的方法，这样我们就不用显式进行空值检测
 * Created by xiaxiangnan on 16/2/25.
 */
public class OptionalAPI {

    public static void main(String[] args) {
        //允许为空值
        Optional<String> fullName = Optional.ofNullable(null);
        //如果Optional类的实例为非空值的话，isPresent()返回true，否从返回false
        System.out.println("Full Name is set? " + fullName.isPresent());
        //orElseGet()方法通过回调函数来产生一个默认值
        System.out.println("Full Name: " + fullName.orElseGet(() -> "[none]"));
        //orElse()方法和orElseGet()方法类似，但是orElse接受一个默认值而不是一个回调函数
        System.out.println(fullName.map(s -> "Hey " + s + "!").orElse("Hey Stranger!"));

        //不允许为空值
        Optional<String> firstName = Optional.of("Tom");
        System.out.println("First Name is set? " + firstName.isPresent());
        System.out.println("First Name: " + firstName.orElseGet(() -> "[none]"));
        System.out.println(firstName.map(s -> "Hey " + s + "!").orElse("Hey Stranger!"));
    }
}
