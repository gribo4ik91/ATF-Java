package com.example.ui.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Утилитный класс для реализации явных ожиданий;
 * Поддерживает ожидание до получения не-null значения, или пока условие не станет true;
 * Используется в UI-тестах, где требуется polling-поведение (без Thread.sleep).
 *
 * <p>Методы в `WaitUtils` позволяют реализовать надёжные ожидания:
 * - до появления элемента или значения;
 * - до выполнения условия (например, изменение URL, появление текста);
 * - с контролем интервалов между проверками и таймаутом.</p>
 */
@Slf4j
public class WaitUtils {

    // Интервал между повторными попытками, по умолчанию: 500 мс
    private static final long DEFAULT_RETRY_INTERVAL_MS = 500;

    /**
     * Ожидает, пока supplier вернёт не-null значение или не истечёт таймаут.
     *
     * @param supplier  метод, возвращающий значение
     * @param timeout   таймаут в секундах
     * @param <T>       тип возвращаемого значения
     * @return значение, если оно было получено в пределах таймаута; иначе — null
     */
    public static <T> T waitUntilNotNull(Supplier<T> supplier, Integer timeout) {
        long timeoutInMillis = TimeUnit.SECONDS.toMillis(timeout);
        long start = System.currentTimeMillis();

        T result = null;

        while ((System.currentTimeMillis() - start) < timeoutInMillis && result == null) {
            result = supplier.get();
            waitForRetry(DEFAULT_RETRY_INTERVAL_MS);
        }
        return result;
    }

    /**
     * Ожидает, пока результат function станет равным ожидаемому значению condition.
     *
     * @param function условие, которое нужно проверить
     * @param condition ожидаемое логическое значение
     * @param timeout таймаут в секундах
     * @return true, если условие выполнилось; иначе — false
     */
    public static boolean waitUntilCondition(Supplier<Boolean> function, boolean condition, int timeout) {
        return waitUntilCondition(function, condition, timeout, DEFAULT_RETRY_INTERVAL_MS);
    }

    /**
     * Ожидает выполнения условия с возможностью указания интервала повторных проверок.
     *
     * @param function         условие
     * @param expectedValue    ожидаемое логическое значение
     * @param timeoutSeconds   таймаут в секундах
     * @param retryTimeoutMs   интервал между проверками в миллисекундах
     * @return true, если условие выполнилось в течение таймаута
     */
    public static boolean waitUntilCondition(Supplier<Boolean> function, boolean expectedValue, int timeoutSeconds, long retryTimeoutMs) {
        boolean result = !expectedValue;

        long timeoutInMillis = TimeUnit.SECONDS.toMillis(timeoutSeconds);
        long start = System.currentTimeMillis();

        while ((System.currentTimeMillis() - start) < timeoutInMillis && result != expectedValue) {
            result = function.get();
            waitForRetry(retryTimeoutMs);
        }
        return result;
    }

    /**
     * Ждёт указанный интервал в миллисекундах.
     *
     * @param retryDelayMs задержка между повторными попытками
     */
    public static void waitForRetry(long retryDelayMs) {
        waitFor(retryDelayMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Ожидание указанное время в заданных единицах времени.
     *
     * @param value    значение
     * @param timeUnit единица измерения времени (секунды, миллисекунды и т.д.)
     */
    public static void waitFor(long value, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(value);
        } catch (InterruptedException e) {
            log.error("Main thread has been interrupted. Trying to close thread.");
            Thread.currentThread().interrupt(); // корректно завершаем поток
        }
    }
}
