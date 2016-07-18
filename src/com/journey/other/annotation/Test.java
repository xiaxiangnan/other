package com.journey.other.annotation;

/**
 *
 * Created by xiaxiangnan on 16/7/18.
 */
public class Test {

    public static void main(String[] args) {
        AnnotationParse.parseSimple(SimpleAnnotationExample.class);
        AnnotationParse.parseMethod(MethodAnnotationTest.class);
    }
}
