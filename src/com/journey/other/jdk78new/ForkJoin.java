package com.journey.other.jdk78new;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Fork/Join框架是Java7提供了的一个用于并行执行任务的框架，
 * 是一个把大任务分割成若干个小任务，最终汇总每个小任务结果后得到大任务结果的框架
 *
 *
 * ForkJoinTask：我们要使用ForkJoin框架，必须首先创建一个ForkJoin任务。它提供在任务中执行fork()和join()操作的机制，
 *   通常情况下我们不需要直接继承ForkJoinTask类，而只需要继承它的子类，Fork/Join框架提供了以下两个子类：
 *      RecursiveAction：用于没有返回结果的任务。
 *      RecursiveTask ：用于有返回结果的任务。
 * ForkJoinPool ：ForkJoinTask需要通过ForkJoinPool来执行，任务分割出的子任务会添加到当前工作线程所维护的双端队列中，
 *   进入队列的头部。当一个工作线程的队列里暂时没有任务时，它会随机从其他工作线程的队列的尾部获取一个任务。
 *
 *
 *  Created by xiaxiangnan on 16/2/24.
 */
public class ForkJoin {

    /**
     * 计算1+2+3+4+..+n的结果
     */
    static class CountTask extends RecursiveTask<Integer> {

        //每个子任务最多执行两个数的相加，设置分割的阈值是2
        private static final int THRESHOLD = 2;
        private int start;
        private int end;

        public CountTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            int sum = 0;
            //如果任务足够小,就计算任务
            boolean canCompute = (end - start) <= THRESHOLD;
            if (canCompute) {
                System.out.println("任务<=阀值2,计算任务: " + (end - start));
                for (int i = start; i <= end; i++) {
                    sum += i;
                }
            } else {
                //如果任务大于阀值,就分裂成两个子任务计算
                System.out.println("任务>阀值2,分裂成两个子任务计算: " + (end - start));
                int middle = (start + end) / 2;
                CountTask leftTask = new CountTask(start, middle);
                CountTask rightTask = new CountTask(middle + 1, end);
                //执行子任务
                leftTask.fork();
                rightTask.fork();
                //等待子任务执行完,并得到结果

                int leftResult = leftTask.join();
                int rightResult = rightTask.join();
                //合并子任务
                sum = leftResult + rightResult;
            }
            return sum;
        }
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //生成一个计算任务,负责计算1+2+3+4+..+10
        CountTask task = new CountTask(1, 10);
        //执行一个任务
        Future<Integer> result = forkJoinPool.submit(task);

        try {
            System.out.println("result is: " + result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


}
