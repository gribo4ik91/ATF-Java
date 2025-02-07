package com.example.ui.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class WaitUtils {

    private static long DEFAULT_RETRY_INTERVAL_MS = 500;



    public static <T> T waitUntilNotNull(Supplier<T> s, Integer timeout) {
        long timeoutInMillis = TimeUnit.SECONDS.toMillis(timeout);
        long start = System.currentTimeMillis();

        T t = null;

        while ((System.currentTimeMillis() - start) < timeoutInMillis && t == null) {
            t = s.get();
            waitForRetry(DEFAULT_RETRY_INTERVAL_MS);
        }
        return t;
    }

    public static Boolean waitUntilCondition(Supplier<Boolean> function, boolean condition, int timeout) {
        return waitUntilCondition(function, condition, timeout, DEFAULT_RETRY_INTERVAL_MS);
    }

    public static Boolean waitUntilCondition(Supplier<Boolean> function, boolean condition, int timeout, long retryTimoutinMs) {
        boolean result = !condition;

        long timeOutInMillis = TimeUnit.SECONDS.toMillis(timeout);
        long start = System.currentTimeMillis();

        while ((System.currentTimeMillis() - start) < timeOutInMillis && result != condition) {
            result = function.get();
            waitForRetry(retryTimoutinMs);
        }
        return result;
    }

    public static void waitForRetry(long retryDelayMs) {
        waitFor(retryDelayMs, TimeUnit.MILLISECONDS);
    }

    public static void waitFor(long value, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(value);
        } catch (InterruptedException e) {
            log.error("Main thread has been interrupted. Trying to close thread.");
            Thread.currentThread().interrupt();
        }
    }

}
