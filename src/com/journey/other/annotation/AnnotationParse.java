package com.journey.other.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 注解处理器
 * Created by xiaxiangnan on 16/7/18.
 */
public class AnnotationParse {

    public static void parseSimple(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(field.isAnnotationPresent(SimpleAnnotation.class)) {
                SimpleAnnotation simpleAnnotation = field.getAnnotation(SimpleAnnotation.class);
                System.out.println(simpleAnnotation.address() + simpleAnnotation.name());
            }
        }
    }

    public static void parseMethod(Class<?> clazz) {
        for(Method method: clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MethodAnnotation.class)) {
                try {
                    method.invoke(null);
                } catch (InvocationTargetException e) {
                    System.out.println(method + " failed: " + e.getCause());
                    Class<? extends Exception> exceptionType = method.getAnnotation(MethodAnnotation.class).value();
                    if(exceptionType.isInstance(e)) {
                        System.out.println(method.getName() + " InvocationTargetException is catch");
                    }
                } catch (Exception e) {
                    System.out.println(method + " invalid, must static method");
                }


            }

        }
    }




}
