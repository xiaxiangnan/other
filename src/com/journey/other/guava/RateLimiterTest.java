package com.journey.other.guava;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @author xiaxiangnan <xiaxiangnan@kuaishou.com>
 * Created on 2020-01-03
 */
public class RateLimiterTest {

    @Test
    public void testAcquire() throws InterruptedException {
        RateLimiter limiter = RateLimiter.create(1);
        for (int i = 1; i < 10; i = i + 2) {
            double waitTime = limiter.acquire(i);
            System.out.println("cutTime=" + LocalDateTime.now() + " acq:" + i + " waitTime:" + waitTime);
        }
    }

    @Test
    public void testTryAcquire() {
        RateLimiter limiter = RateLimiter.create(1);
        boolean result = limiter.tryAcquire(2);
        System.out.println("cutTime=" + LocalDateTime.now() + ", result: " + result);
        result = limiter.tryAcquire(1, 2000, TimeUnit.MILLISECONDS);
        System.out.println("cutTime=" + LocalDateTime.now() + ", result: " + result);
        result = limiter.tryAcquire(1);
        System.out.println("cutTime=" + LocalDateTime.now() + ", result: " + result);
    }

    @Test
    public void testWarmingUp() {
        RateLimiter rateLimiter = RateLimiter.create(2, 4, TimeUnit.SECONDS);
        while (true) {
            System.out.println(rateLimiter.acquire(1));
            System.out.println(rateLimiter.acquire(1));
            System.out.println(rateLimiter.acquire(1));
            System.out.println(rateLimiter.acquire(1));
        }
    }


}
