package com.journey.other.enumtest;

import java.util.Arrays;

/**
 * enum create
 * Created by xiaxiangnan on 16/7/15.
 */
public class EnumCreate {

    public enum EasyEnum {
        A, B;

        EasyEnum() {
            System.out.println("constructor is call");
        }
    }

    public enum EnumWithData {
        A(1, 2), B(3, 4);

        private final int a;
        private final int b;

        //enum is private
        EnumWithData(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public int sum() {
            return a + b;
        }
    }

    public enum Operation {

        PLUS("+") {
            @Override
            double apply(double x, double y) {
                return x + y;
            }
        }, MINUS("-") {
            @Override
            double apply(double x, double y) {
                return x - y;
            }
        };

        private final String symbol;

        Operation(String symbol) {
            this.symbol = symbol;
        }

        abstract double apply(double x, double y);

        //Enum覆载了了toString方法，因此我们如果调用PLUS的toString()默认返回字符串”PLUS”.
        @Override
        public String toString() {
            return symbol;
        }

    }

    public interface OP {
        double apply(double x, double y);
    }

    public enum OPImpl implements OP {
        MUL {
            @Override
            public double apply(double x, double y) {
                return x * y;
            }
        }, DIV {
            @Override
            public double apply(double x, double y) {
                return x / y;
            }
        };

    }

    public static void main(String[] args) {
        System.out.println(Operation.valueOf("PLUS")); //调用valueOf(“PLUS”)将返回Operation.PLUS
        System.out.println(Operation.PLUS.ordinal()); //枚举值声明的顺序0,1,2...

        System.out.println("EasyEnum: " + Arrays.asList(EasyEnum.values()).toString());
        System.out.println("EnumWithData: " + EnumWithData.A.sum());
        System.out.println("Operation: " + Operation.PLUS.apply(1, 2) + ", " + Operation.MINUS);
        System.out.println("OPImpl: " + OPImpl.MUL.apply(2, 4));

    }

}
