package com.journey.other.jdk78new;

import java.util.function.Supplier;

/**
 * 接口的默认方法与静态方法
 * Java 8用默认方法与静态方法这两个新概念来扩展接口的声明
 * Created by xiaxiangnan on 16/2/25.
 */
public class Interface {

    /**
     * 接口用关键字default声明了一个默认方法notRequired()
     */
    private interface Defaulable {
        // Interfaces now allow default methods, the implementer may or
        // may not implement (override) them.
        default String notRequired() {
            return "Default implementation";
        }
    }

    /**
     * Defaulable接口的实现者之一DefaultableImpl实现了这个接口，并且让默认方法保持原样
     */
    private static class DefaultableImpl implements Defaulable {
    }

    /**
     * Defaulable接口的另一个实现者OverridableImpl用自己的方法覆盖了默认方法。
     */
    private static class OverridableImpl implements Defaulable {
        @Override
        public String notRequired() {
            return "Overridden implementation";
        }
    }


    /**
     * 接口可以声明（并且可以提供实现）静态方法
     */
    private interface DefaulableFactory {
        // Interfaces now allow static methods
        static Defaulable create(Supplier<Defaulable> supplier) {
            return supplier.get();
        }
    }

    public static void main(String[] args) {
        Defaulable defaulable = DefaulableFactory.create(DefaultableImpl::new);
        System.out.println(defaulable.notRequired());

        defaulable = DefaulableFactory.create(OverridableImpl::new);
        System.out.println(defaulable.notRequired());
    }

}
