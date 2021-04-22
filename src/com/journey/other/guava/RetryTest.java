package com.journey.other.guava;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;

/**
 * @author xiaxiangnan <xiaxiangnan@kuaishou.com>
 * Created on 2019-12-31
 */
public class RetryTest {

    @Test
    public void test() {
        Callable<Boolean> callable = () -> {
            return true; // do something useful here
        };

        Retryer<Boolean> retryer = RetryerBuilder.<Boolean> newBuilder()
                .retryIfResult(Objects::isNull)
                .retryIfExceptionOfType(IOException.class)
                .retryIfRuntimeException()
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .build();
        try {
            retryer.call(callable);
        } catch (RetryException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
