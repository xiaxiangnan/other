package com.journey.other.jdk78new;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * 方法引用:
 * 任何一个λ表达式都可以代表某个函数接口的唯一方法的匿名描述符。
 * 我们也可以使用某个类的某个具体方法来代表这个描述符，叫做方法引用
 * 可以直接引用已有Java类或对象（实例）的方法或构造器。
 * 与lambda联合使用，方法引用可以使语言的构造更紧凑简洁，减少冗余代码。
 * Created by xiaxiangnan on 16/2/25.
 */
public class MethodReferrance {

    static class Car {

        /**
         * 构造器引用，它的语法是Class::new，或者更一般的Class< T >::new
         */
        public static Car create(final Supplier<Car> supplier) {
            return supplier.get();
        }

        /**
         * 静态方法引用，它的语法是Class::static_method
         */
        public static void collide(final Car car) {
            System.out.println("Collided " + car.toString());
        }

        /**
         * 特定类的任意对象的方法引用，它的语法是Class::method
         */
        public void repair() {
            System.out.println("Repaired " + this.toString());
        }

        /**
         * 特定对象的方法引用，它的语法是instance::method
         */
        public void follow(final Car another) {
            System.out.println("Following the " + another.toString());
        }
    }

    public static void main(String[] args) {
        final Car car = Car.create(Car::new);
        final List<Car> cars = Arrays.asList(car);
        cars.forEach(Car::collide);
        cars.forEach(Car::repair);
        final Car police = Car.create(Car::new);
        cars.forEach(police::follow);
    }

}
