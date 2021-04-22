package com.journey.other.reactor;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author xiaxiangnan <xiaxiangnan@kuaishou.com>
 * Created on 2019-12-23
 */
public class FluxTest {

    /**
     * just()：可以指定序列中包含的全部元素。创建出来的 Flux 序列在发布这些元素之后会自动结束。
     * fromArray()，fromIterable()和 fromStream()：可以从一个数组、Iterable 对象或 Stream 对象中创建 Flux 对象。
     * empty()：创建一个不包含任何元素，只发布结束消息的序列。
     * error(Throwable error)：创建一个只包含错误消息的序列。
     * never()：创建一个不包含任何消息通知的序列。
     * range(int start, int count)：创建包含从 start 起始的 count 个数量的 Integer 对象的序列。
     * interval(Duration period)和 interval(Duration delay, Duration period)：创建一个包含了从 0 开始递增的 Long
     * 对象的序列。其中包含的元素按照指定的间隔来发布。除了间隔时间之外，还可以指定起始元素发布之前的延迟时间。
     * intervalMillis(long period)和 intervalMillis(long delay, long period)：与 interval()方法的作用相同，只不过该方法通过毫秒数来指定时间间隔和延迟时间。
     */
    public static void simpleCreate() {
        Flux.just("Hello", "World").subscribe(System.out::println);
        Flux.fromArray(new Integer[] {1, 2, 3}).subscribe(System.out::println);
        Flux.empty().subscribe(System.out::println);
        Flux.range(1, 10).subscribe(System.out::println);
        Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).subscribe(System.out::println);
        Flux.interval(Duration.ofMillis(1000)).subscribe(System.out::println);
    }

    /**
     * generate, create
     */
    public static void generateCreate() {
        //generate通过同步和逐一的方式来产生
        Flux.generate(sink -> {
            //只能调用一次，调多次抛异常：More than one call to onNext
            sink.next("Hello");
            //complete结束该序列
            sink.complete();
        }).subscribe(System.out::println);

        final Random random = new Random();
        Flux.generate(ArrayList::new, (list, sink) -> {
            int value = random.nextInt(100);
            list.add(value + 100);
            sink.next(value);
            if (list.size() == 10) {
                sink.complete();
            }
            return list;
        }).subscribe(System.out::println);

        //create支持同步和异步的消息产生，并且可以在一次调用中产生多个元素。
        Flux.create(sink -> {
            for (int i = 0; i < 10; i++) {
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::println);
    }

    public static void buffer() {
        Flux.range(1, 10).buffer(3).subscribe(System.out::println);
    }

    private static void other() {
        //filter
        Flux.range(1, 10).filter(i -> i % 2 == 0).subscribe(System.out::println);
        //reduce,take,merge,flatMap,concatMap
    }

    public void testFlux() {
    }


    public static void main(String[] args) {
        //        simpleCreate();
        //        generateCreate();
        //        buffer();
        System.out.println("----------------------------");

        Flux.just("tom")
                .map(s -> {
                    System.out.println("(concat @qq.com) at [" + Thread.currentThread() + "]");
                    return s.concat("@qq.com");
                })
                .publishOn(Schedulers.newSingle("thread-a"))
                .map(s -> {
                    System.out.println("(concat foo) at [" + Thread.currentThread() + "]");
                    return s.concat("foo");
                })
                .filter(s -> {
                    System.out.println("(startsWith f) at [" + Thread.currentThread() + "]");
                    return s.startsWith("t");
                })
                .publishOn(Schedulers.newSingle("thread-b"))
                .map(s -> {
                    System.out.println("(to length) at [" + Thread.currentThread() + "]");
                    return s.length();
                })
                .subscribeOn(Schedulers.newSingle("source"))
                .subscribe(System.out::println);


        Flux.create(sink -> {
            sink.next(Thread.currentThread().getName());
            sink.complete();
        }).publishOn(Schedulers.single())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .publishOn(Schedulers.elastic())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .subscribeOn(Schedulers.parallel())
                .toStream()
                .forEach(System.out::println);

    }
}
