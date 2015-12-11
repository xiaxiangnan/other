package com.journey.other.sensitivewordfilter;

import java.util.Set;

/**
 * 敏感词过滤
 * from: http://cmsblogs.com/?p=1031
 * PROJECT: other
 * Created by xiaxiangnan on 15-12-2.
 */
public class Main {

    public static void main(String[] args) {
        SensitivewordFilter filter = new SensitivewordFilter();
        System.out.println("敏感词树的数量：" + filter.sensitiveWordMap.size());
        String string = "这个是测试的字符串，中国人，是好人。五星的中国女人。中国人民啊";
        System.out.println("待检测语句字数：" + string.length());
        long beginTime = System.currentTimeMillis();
        Set<String> set = filter.getSensitiveWord(string, 2);
        long endTime = System.currentTimeMillis();
        System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
        System.out.println("总共消耗时间为：" + (endTime - beginTime));
    }
}
