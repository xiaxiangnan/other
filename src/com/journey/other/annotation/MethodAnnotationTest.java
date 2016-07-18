package com.journey.other.annotation;

import java.lang.reflect.InvocationTargetException;

/**
 *
 * Created by xiaxiangnan on 16/7/18.
 */
public class MethodAnnotationTest {

    @MethodAnnotation(InvocationTargetException.class)
    public static void m1() {
        System.out.println("m1");
    }

    @MethodAnnotation(InvocationTargetException.class)
    public void m2() {
        System.out.println("m2");
    }

    public static void m3() {
        System.out.println("m3");
    }

    @MethodAnnotation(InvocationTargetException.class)
    public static void m4() throws RuntimeException {
        throw new RuntimeException("m4");
    }

    @MethodAnnotation(RuntimeException.class)
    public static void m5() throws RuntimeException {
        throw new RuntimeException("m5");
    }

}
