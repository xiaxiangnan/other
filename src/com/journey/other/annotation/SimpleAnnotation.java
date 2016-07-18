package com.journey.other.annotation;

import java.lang.annotation.*;

/**
 *
 * Created by xiaxiangnan on 16/7/18.
 */
@Target(ElementType.FIELD) //用于描述域
@Retention(RetentionPolicy.RUNTIME) //在运行时有效（即运行时保留）
@Documented //描述其它类型的annotation应该被作为被标注的程序成员的公共API
public @interface SimpleAnnotation {

    enum Color{BLUE, RED, GREEN}

    String name() default "fieldName";
    String address() default "";
    Color color() default Color.BLUE;
    boolean isGood() default false;

}
