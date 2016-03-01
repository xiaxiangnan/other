package com.journey.other.jdk78new;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Jdk8 lambda表达式
 * λ表达式有三部分组成：参数列表，箭头（->），以及一个表达式或语句块。
 * 一个接口，如果只有一个显式声明的抽象方法，那么它就是一个函数接口。
 * 一般用@FunctionalInterface标注出来（也可以不标）例如:Runnable,Comparator等
 * 一个λ表达式其实就是定义了一个匿名方法，只不过这个方法必须符合至少一个函数接口.
 * λ表达式主要用于替换以前广泛使用的内部匿名类，各种回调，比如事件响应器、传入Thread类的Runnable等
 * Created by xiaxiangnan on 16/2/25.
 */
public class Lambda {

    public static void main(String[] args) throws Exception {
        Arrays.asList("a", "b", "c").forEach((String e) -> System.out.println(e));
        // e不写类型由编译器推测出来
        Arrays.asList("a", "b", "c").forEach(e -> System.out.println(e));

        // 函数体放到在一对花括号中
        Arrays.asList("a", "b", "d").forEach(e -> {
            System.out.println(e);
            System.out.println(e);
        });

        // 变量不是final的话，它们会被隐含的转为final，这样效率更高
        String separator = ",";
        Arrays.asList("a", "b", "d").forEach(
                (String e) -> System.out.print(e + separator));

        /**
         * Lambda可能会返回一个值。返回值的类型也是由编译器推测出来的。
         * 如果lambda的函数体只有一行的话，那么没有必要显式使用return语句
         */
        Arrays.asList(1, 3, 2).sort((e1, e2) -> e1.compareTo(e2));
        Arrays.asList(1, 3, 2).sort((Integer e1, Integer e2) -> {
            int result = e1.compareTo(e2);
            return result;
        });

        //可以用一个λ表达式为一个函数接口赋值
        Runnable r1 = () -> System.out.println("Hello Lambda!");
        //必须显式的转型成一个函数接口
        Object o = (Runnable) () -> System.out.println("hi");

        MyFunction myFunction = () -> System.out.println("my Lambda Function");

        // 嵌套的λ表达式
        Callable<Runnable> c1 = () -> () -> System.out.println("Nested lambda");
        c1.call().run();

        // 用在条件表达式中
        Callable<Integer> c2 = true ? (() -> 42) : (() -> 24);
        System.out.println(c2.call());

        System.out.println(new Lambda().factorial.apply(3));

        //一个生成器函数会产生一系列元素，供给一个流,Stream.generate(Supplier<T> s)就是一个生成器函数.
        // 生成并打印5个随机数
        Stream.generate(Math::random).limit(5).forEach(System.out::println);

    }

    /**
     * 定义一个递归函数，注意须用this限定
     */
    UnaryOperator<Integer> factorial = i -> i == 0 ? 1 : i * this.factorial.apply(i - 1);

    /**
     * 自定义函数接口
     */
    @FunctionalInterface
    interface MyFunction {
        void run();
    }
    /**
     * JDK预定义了很多函数接口以避免用户重复定义
     * 1.Function:这个接口代表一个函数，接受一个T类型的参数，并返回一个R类型的返回值
     *   Function<T, R>::R apply(T t)
     * 2.Consumer:跟Function的唯一不同是它没有返回值
     *   Consumer<T>::accept(T t)
     * 3.Predicate:用来判断某项条件是否满足。经常用来进行筛滤操作
     *   Predicate<T>::test(T t)
     */


}
