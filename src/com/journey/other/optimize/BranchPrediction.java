package com.journey.other.optimize;

import java.util.Arrays;
import java.util.Random;

/**
 * CPU的分支预测(Branch Prediction)模型
 * 遇到类似if的分支时,猜测分支的下一步走向:
 * -如果猜错了，处理器要flush掉pipelines(指令流水线), 回滚到之前的分支，然后重新热启动，选择另一条路径。
 * -如果猜对了，处理器不需要暂停，继续往下执行
 * 如果每次都猜错了，处理器将耗费大量时间在停止-回滚-热启动这一周期性过程里。如果侥幸每次都猜对了，
 * 那么处理器将从不暂停，一直运行至结束。
 * 那么处理器该采用怎样的策略来用最小的次数来尽量猜对指令分支的下一步走向呢？答案就是分析历史运行记录.
 * <p>
 * 参考:http://ifeve.com/why-is-it-faster-to-process-a-sorted-array-than-an-unsorted-array/
 * <p>
 * Created by xiaxiangnan on 16/10/18.
 */
public class BranchPrediction {

    private int[] data;
    private int arraySize = 32768;

    public BranchPrediction() {
        // Generate data
        data = new int[arraySize];
        Random rnd = new Random(0);
        for (int c = 0; c < arraySize; ++c) {
            data[c] = rnd.nextInt() % 256;
        }
    }


    /**
     * 使用位运算: 是否排序对performance无显著影响
     * 使用分支预测: 是否排序严重影响performance
     */
    public static void main(String[] args) {
        new BranchPrediction().common(); // 10s
        new BranchPrediction().commonSort(); //2.8s
        new BranchPrediction().optimize(); //2.3s
    }

    /**
     * 一般计算
     */
    public void common() {
        long start = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < 100000; ++i) {
            // Primary loop
            for (int c = 0; c < arraySize; ++c) {
                /**数组data有序时，前面一半元素的迭代将不会进入if-statement, 超过一半时，元素迭代将全部进入if-statement.*/
                if (data[c] >= 128)
                    sum += data[c];
            }
        }

        System.out.println((System.nanoTime() - start) / 1000000000.0);
        System.out.println("sum = " + sum);
    }

    /**
     * 对data排序后计算
     */
    public void commonSort() {
        Arrays.sort(data);
        common();
    }


    /**
     * 利用位运算取消分支跳转
     */
    public void optimize() {
        long start = System.nanoTime();
        long sum = 0;

        for (int i = 0; i < 100000; ++i) {
            // Primary loop
            for (int c = 0; c < arraySize; ++c) {
                int t = (data[c] - 128) >> 31; //或者t=-((data>=128)); # statement 1
                sum += ~t & data[c]; //# statement 2
                /**
                 * 分析：
                 * data < 128, 则statement 1值为: 0xffff = -1(负数右移31为一定为0xffff), statement 2等号右侧值为: 0 & data == 0;
                 * data >= 128, 则statement 1值为: 0(非负数右移31为一定为0), statement 2等号右侧值为: ~0 & data == -1 & data == 0xffff & data == data;
                 */
            }
        }

        System.out.println((System.nanoTime() - start) / 1000000000.0);
        System.out.println("sum = " + sum);
    }


}
