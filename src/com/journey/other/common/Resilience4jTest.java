package com.journey.other.common;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.common.util.concurrent.Uninterruptibles;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.Predicates;
import io.vavr.control.Try;

/**
 * @author xiaxiangnan <xiaxiangnan@kuaishou.com>
 * Created on 2021-01-18
 */
public class Resilience4jTest {


    /**
     * CircuitBreaker 初始时是关闭状态，所有请求正常运行，当失败率达到设置的阈值时（包括异常率和慢请求率），
     * CircuitBreaker 进入开启状态，拒绝处理所有请求。持续一段时间后进入半开状态，尝试让一部分请求通过，
     * 当半开状态的请求失败率高于阈值时，CircuitBreaker 重新进入开启状态拒绝所有请求，否则 CircuitBreaker 进入关闭状态允许执行所有请求。
     */
    @Test
    public void circuitBreaker() {
        CircuitBreakerConfig testCircuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // 故障率阈值百分比，超过该值，断路器开始断路调用，默认值 50
                .slowCallRateThreshold(50) // 慢请求阈值, 默认值 100
                .slowCallDurationThreshold(Duration.ofSeconds(2)) // 慢请求的时间，默认值60s
                .permittedNumberOfCallsInHalfOpenState(10) // 半开状态允许的请求数，默认 10
                .maxWaitDurationInHalfOpenState(Duration.ofSeconds(10)) // 半开状态持续的最大时间
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED) // 断路器关闭状态下，记录请求的滑动窗口类型，默认 基于计数
                .slidingWindowSize(5) // 断路器关闭状态下，如果滑动窗口类型是 COUNT_BASED，表示窗口计数的个数，如果是 TIME_BASED，则表示窗口时间范围，默认值 100
                .minimumNumberOfCalls(5) // 转换状态前，需要记录的最小的请求个数, 默认值 100
                .waitDurationInOpenState(Duration.ofSeconds(5)) // 断路器从开启状态转换成半开状态的等待时间，默认 60s
                .automaticTransitionFromOpenToHalfOpenEnabled(
                        false) // 默认false，表示断路器需要新的请求才能触发从开启状态到半开状态，true 表示断路器不需要请求就可以从开启状态转化为半开状态
                .recordExceptions() // 需要判定为失败的异常，默认空
                .ignoreExceptions() // 需要忽略的异常，不会判定为失败，默认空
                // 默认所有异常都被认定为失败事件，你可以定制Predicate的test检查，实现选择性记录，只有该函数返回为true时异常才被认定为失败事件。
                .recordException(throwable -> Match(throwable).of(
                        Case($(Predicates.instanceOf(RuntimeException.class)), true),
                        Case($(), false)))
                .ignoreException(throwable -> Match(throwable).of(
                        Case($(Predicates.instanceOf(NullPointerException.class)), true),
                        Case($(), false)))
                .build();

        CircuitBreaker testCircuitBreaker = CircuitBreakerRegistry.ofDefaults()
                .circuitBreaker("testCircuitBreaker", testCircuitBreakerConfig);

        testCircuitBreaker.getEventPublisher()
                .onCallNotPermitted(event -> System.out.println(event.getEventType()))
                .onStateTransition(event -> System.out.println(event.getEventType()))
                .onFailureRateExceeded(event -> System.out.println(event.getEventType()))
                .onSlowCallRateExceeded(event -> System.out.println(event.getEventType()))
                .onSuccess(event -> System.out.println(event.getEventType()))
                .onError(event -> System.out.println(event.getEventType()))
                .onIgnoredError(event -> System.out.println(event.getEventType()))
                .onEvent(event -> System.out.println("event: " + event.getEventType()));


        CheckedFunction0<String> task = () -> {
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            int a = 1 / 0;
            return "task";
        };
        CheckedFunction0<String> decoratedTask = CircuitBreaker.decorateCheckedSupplier(testCircuitBreaker, task);

        for (int i = 0; i < 10; i++) {
            String result = Try.of(decoratedTask)
                    .onFailure(Throwable::printStackTrace)
                    .recover(RuntimeException.class, e -> "recover: " + e.getClass())
                    .getOrElse("fallback");
            System.out.println(result);
        }
        Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
        System.out.println("after 10s");
        for (int i = 0; i < 10; i++) {
            String result = Try.of(decoratedTask)
                    .onFailure(Throwable::printStackTrace)
                    .recover(RuntimeException.class, e -> "recover: " + e.getClass())
                    .getOrElse("fallback");
            System.out.println(result);
        }
    }


    /**
     * 限制对下游请求的最大并发量
     */
    @Test
    public void bulkhead() throws Exception {
        semaphoreBulkhead();
        fixedThreadPoolBulkhead();
    }

    /**
     * 固定线程池舱壁
     */
    private void fixedThreadPoolBulkhead() throws ExecutionException, InterruptedException {
        ThreadPoolBulkheadConfig testThreadPoolBulkheadConfig = ThreadPoolBulkheadConfig.custom()
                // 最大线程数，默认可用 cpu 核数
                .maxThreadPoolSize(2)
                .coreThreadPoolSize(2)
                // ArrayBlockingQueue 阻塞队列长度，默认 100
                .queueCapacity(2)
                // 线程存活时间，默认 20ms
                .keepAliveDuration(Duration.ofMillis(10))
                .build();

        ThreadPoolBulkhead threadPoolBulkhead = ThreadPoolBulkheadRegistry.ofDefaults()
                .bulkhead("threadPoolBulkhead", testThreadPoolBulkheadConfig);

        threadPoolBulkhead.getEventPublisher()
                .onCallPermitted(event -> System.out.println(event.getEventType()))
                .onCallRejected(event -> System.out.println(event.getEventType()))
                .onCallFinished(event -> System.out.println(event.getEventType()))
                .onEvent(event -> System.out.println("event: " + event.getEventType()));

        Supplier<String> task = () -> {
            System.out.println(Thread.currentThread().getName() + " call task");
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            return "task";
        };
        Supplier<CompletionStage<String>> decoratedThreadPoolTask =
                ThreadPoolBulkhead.decorateSupplier(threadPoolBulkhead, task);

        // 向线程池提交任务
        CompletionStage<String> completionStage = decoratedThreadPoolTask.get();
        System.out.println("submit task");
        String result = completionStage.toCompletableFuture().exceptionally(t -> {
            t.printStackTrace();
            return "fallback";
        }).get();
        System.out.println(result);
    }

    /**
     * 信号量舱壁
     */
    private void semaphoreBulkhead() {
        // 容错器配置
        BulkheadConfig testBulkheadConfig = BulkheadConfig.custom()
                // 舱壁允许的最大并行执行量， 默认个 25
                .maxConcurrentCalls(3)
                // 等待进入舱壁的最大超时时间，默认 0s
                .maxWaitDuration(Duration.ofMillis(2000))
                .build();

        Bulkhead testBulkhead = BulkheadRegistry.ofDefaults().bulkhead("testBulkhead", testBulkheadConfig);

        testBulkhead.getEventPublisher()
                .onCallPermitted(event -> System.out.println(event.getEventType()))
                .onCallRejected(event -> System.out.println(event.getEventType()))
                .onCallFinished(event -> System.out.println(event.getEventType()))
                .onEvent(event -> System.out.println("event: " + event.getEventType()));

        CheckedFunction0<String> task = () -> {
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            return "task";
        };
        CheckedFunction0<String> decoratedTask = Bulkhead.decorateCheckedSupplier(testBulkhead, task);

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (AtomicInteger i = new AtomicInteger(1); i.getAndIncrement() < 11; ) {
            executorService.submit(() -> {
                        String result = Try.of(decoratedTask)
                                .onFailure(Throwable::printStackTrace)
                                .recover(Exception.class, e -> "recover")
                                .getOrElse("fallback");
                        System.out.println(Thread.currentThread().getName() + "." + result);
                    }
            );
        }
        Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
    }

    @Test
    public void rateLimiter() {
        // 容错配置
        RateLimiterConfig testRateLimiterConfig = RateLimiterConfig.custom()
                // 时间段内限速次数，默认 50
                .limitForPeriod(5)
                // 限速流量刷新周期，每个周期限速器将其权限计数设置为limitForPeriod值，默认 500纳秒
                .limitRefreshPeriod(Duration.ofSeconds(1))
                // 限速阻塞超时时间，默认 5s
                .timeoutDuration(Duration.ofMillis(500))
                .build();

        RateLimiter testRateLimiter = RateLimiterRegistry.ofDefaults()
                .rateLimiter("testRateLimiter", testRateLimiterConfig);

        // 注册事件处理器，可根据不同的事件自定义不同的操作，比如状态数据上报
        testRateLimiter.getEventPublisher()
                .onSuccess(event -> System.out.println(event.getEventType()))
                .onFailure(event -> System.out.println(event.getEventType()))
                .onEvent(event -> System.out.println("event: " + event.getEventType()));

        Function<Integer, String> task = (i) -> i + ".task";
        Function<Integer, String> decoratedTask = RateLimiter.decorateFunction(testRateLimiter, task);
        for (int i = 1; i < 11; i++) {
            int temp = i;
            String result = Try.of(() -> decoratedTask.apply(temp))
                    .onFailure(Throwable::printStackTrace)
                    .recover(RuntimeException.class, e -> "recover")
                    .getOrElse("fallback");
            System.out.println(result);
        }

    }

    @Test
    public void retry() throws ExecutionException, InterruptedException {
        // 容错配置， String任务的返回结果类型
        RetryConfig testRetryConfig = RetryConfig.<String> custom()
                .maxAttempts(5)
                .intervalFunction(IntervalFunction.of(1000, i -> i + 1000))
                .retryOnResult(StringUtils::isBlank)
                .retryOnException(throwable -> Match(throwable).of(
                        Case($(Predicates.instanceOf(RuntimeException.class)), true),
                        Case($(), false)))
                .ignoreExceptions(NullPointerException.class)
                .build();

        Retry testRetry = RetryRegistry.ofDefaults().retry("test", testRetryConfig);
        // 注册事件处理器，可根据不同的事件自定义不同的操作，比如状态数据上报
        testRetry.getEventPublisher()
                .onRetry(event -> System.out.println(event.getEventType()))
                .onError(event -> System.out.println(event.getEventType()))
                .onIgnoredError(event -> System.out.println(event.getEventType()))
                .onSuccess(event -> System.out.println(event.getEventType()))
                .onEvent(event -> System.out.println("event: " + event.getEventType()));

        AtomicInteger atomicInteger = new AtomicInteger(0);
        Supplier<String> task = () -> {
            System.out.println(LocalDateTime.now() + " call task " + atomicInteger.get());
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            if (atomicInteger.addAndGet(1) < 3) {
                throw new RuntimeException("fake");
            }
            return "good";
        };
        //同步
        Supplier<String> decoratedTask = Retry.decorateSupplier(testRetry, task);
        //异步
        Supplier<CompletionStage<String>> decoratedFutureTask =
                Retry.decorateCompletionStage(testRetry, Executors.newScheduledThreadPool(10),
                        () -> CompletableFuture.supplyAsync(task));

        String result = Try.ofSupplier(decoratedTask)
                .onFailure(Throwable::printStackTrace)
                .recover(Exception.class, e -> "recover")
                .getOrElse("fallback");
        System.out.println("get result: " + result);
        System.out.println("-------------------------");

        CompletionStage<String> completionStage = decoratedFutureTask.get();
        System.out.println(completionStage.toCompletableFuture().get());
    }

    @Test
    public void timeLimiter() {
        TimeLimiterConfig testTimeLimiterConfig = TimeLimiterConfig.custom()
                // 超时时间阈值，默认 1s
                .timeoutDuration(Duration.ofSeconds(1))
                // 超时后是否取消线程，默认为 true
                .cancelRunningFuture(true)
                .build();
        //创建注册器，注册器会根据 name 缓存容错器;从缓存获取或者创建新的容错器器放入内存
        TimeLimiter testTimeLimiter = TimeLimiterRegistry.ofDefaults()
                .timeLimiter("testTimeLimiter", testTimeLimiterConfig);
        // 注册事件处理器，可根据不同的事件自定义不同的操作，比如状态数据上报
        testTimeLimiter.getEventPublisher()
                .onSuccess(event -> System.out.println(event.getEventType()))
                .onError(event -> System.out.println(event.getEventType()))
                .onTimeout(event -> System.out.println(event.getEventType()))
                .onEvent(event -> System.out.println("event: " + event.getEventType()));
        // Future 装饰
        Callable<String> decoratedFutureSupplier =
                testTimeLimiter.decorateFutureSupplier(() -> Executors.newCachedThreadPool().submit(() -> {
                    Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
                    System.out.println("call method");
                    return "good job";
                }));
        String result = Try.of(decoratedFutureSupplier::call)
                .onFailure(Throwable::printStackTrace)
                .recover(RuntimeException.class, e -> "recover")
                .getOrElse("fallback");
        System.out.println(result);
    }

}
