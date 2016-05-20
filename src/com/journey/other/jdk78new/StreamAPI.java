package com.journey.other.jdk78new;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stream API极大简化了集合框架的处理（但它的处理的范围不仅仅限于集合框架的处理）
 * 一串支持连续、并行聚集操作的元素。设计使用了管道（pipelines）模式。对流的一次操作会返回另一个流
 * Created by xiaxiangnan on 16/2/25.
 */
public class StreamAPI {

    /**
     * 给出一个String类型的数组，找出其中所有不重复的奇数
     */
    public void distinctPrimary(String... numbers) {
        List<String> l = Arrays.asList(numbers);
        List<Integer> r = l.stream()
                .map(e -> new Integer(e))
                .filter(e -> e % 2 == 1)
                .distinct()
                //除collect外其它的eager操作还有forEach，toArray，reduce等。
                .collect(Collectors.toList());
        System.out.println("distinctPrimary result is: " + r);
    }

    /**
     * 给出一个String类型的数组，找出其中各个奇数，并统计其出现次数
     */
    public void primaryOccurrence(String... numbers) {
        List<String> l = Arrays.asList(numbers);
        Map<Integer, Integer> r = l.stream()
                .map(e -> new Integer(e))
                .filter(e -> e % 2 == 1)
                //把结果收集到一个Map中，用统计到的各个素数自身作为键，其出现次数作为值
                .collect(Collectors.groupingBy(p -> p, Collectors.summingInt(p -> 1)));
        System.out.println("primaryOccurrence result is: " + r);
    }

    /**
     * 给出一个String类型的数组，求其中所有不重复奇数的和
     */
    public void distinctPrimarySum(String... numbers) {
        List<String> l = Arrays.asList(numbers);
        int sum = l.stream()
                .map(e -> new Integer(e))
                .filter(e -> e % 2 == 1)
                .distinct()
                //reduce方法用来产生单一的一个最终结果.流有很多预定义的reduce操作，如sum()，max()，min()等
                .reduce(0, (x,y) -> x+y); // equivalent to .sum()
        System.out.println("distinctPrimarySum result is: " + sum);
    }



    public static void main(String[] args) throws IOException {
        Collection<Task> tasks = Arrays.asList(new Task(1), new Task(2), new Task(3));
        int sum = tasks.stream().filter(task -> task.isGood()).mapToInt(Task::getFlag).sum();
        System.out.println("stream sum: " + sum);

        // Calculate total points of all tasks
        final double totalPoints = tasks
                .stream()
                .parallel() //并行运行
                .map(task -> task.getFlag()) // or map( Task::getFlag )
                .reduce(0, Integer::sum);
        System.out.println("Total points (all tasks): " + totalPoints);

        // Group tasks 对集合中的元素进行分组
        final Map<Boolean, List<Task>> map = tasks
                .stream()
                .collect(Collectors.groupingBy(Task::isGood));
        System.out.println(map);


        /**
         * 这些返回另一个Stream的方法都是“懒（lazy）”的，而最后返回最终结果的collect方法则是“急（eager）”的。
         * 在遇到eager方法之前，lazy的方法不会执行,其实只经过了一次循环.
         */
        // Calculate the weight of each tasks (as percent of total points)
        Collection<String> result = tasks
                .stream()                                        // Stream< String >
                .mapToInt(Task::getFlag)                     // IntStream
                .asLongStream()                                  // LongStream
                .mapToDouble(points -> points / totalPoints)   // DoubleStream
                .boxed()                                         // Stream< Double >
                .mapToLong(weight -> (long) (weight * 100)) // LongStream
                .mapToObj(percentage -> percentage + "%")      // Stream< String>
                .collect(Collectors.toList());                 // List< String >
        System.out.println(result);

        //从文本文件中逐行读取数据这样典型的I/O操作也很适合用Stream API来处理
        final Path path = new File(".gitignore").toPath();
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            lines.onClose(() -> System.out.println("Done!")).forEach(System.out::println);
        }

    }

    static class Task {

        int flag;
        boolean good;

        public int getFlag() {
            return flag;
        }

        Task(int flag) {
            this.flag = flag;
            good = isGood();
        }

        public boolean isGood() {
            return flag % 2 == 1;
        }

        @Override
        public String toString() {
            return String.format("[%s, %d]", good, flag);
        }

    }
}
