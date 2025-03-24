package com.example.ui.core;

import com.example.ui.core.browser.Browser;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;
import org.springframework.stereotype.Controller;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.example.ATFAssert.fail;

@Slf4j
@Controller
public abstract class AbstractAction {

    private static final int ATTEMPTS_NUMBER = 3;
    private static final int RETRY_DELAY_MS = 500;

    protected Browser browser;
    protected ElementContainer element;

    public AbstractAction(ElementContainer element, Browser browser) {
        this.element = element;
        this.browser = browser;
    }

    /**
     * Выполняет действие с повторными попытками
     */
    protected void execute(Runnable action) {
        browser.waitCurrentPageToLoad();
        retryAction(action);
    }

    /**
     * Выполняет действие, возвращающее значение, с повторными попытками
     */
    protected <T> T execute(Supplier<T> action) {
        browser.waitCurrentPageToLoad();
        return retryAction(action);
    }

    /**
     * Универсальный метод для повторных попыток выполнения действий
     */
    private <T> T retryAction(Supplier<T> action) {
        Exception lastException = null;
        for (int i = 1; i <= ATTEMPTS_NUMBER; i++) {
            try {
                return action.get();
            } catch (WebDriverException ex) {
                lastException = ex;
                log.error("Execution failed on attempt {} for element: {}", i, element.getName());
                sleep(RETRY_DELAY_MS);
            }
        }
        logErrorAndFail(lastException);
        return null;
    }

    private void retryAction(Runnable action) {
        Exception lastException = null;
        for (int i = 1; i <= ATTEMPTS_NUMBER; i++) {
            try {
                action.run();
                return;
            } catch (WebDriverException ex) {
                lastException = ex;
                log.error("Execution failed on attempt {} for element: {}", i, element.getName());
                sleep(RETRY_DELAY_MS);
            }
        }
        logErrorAndFail(lastException);
    }

    /**
     * Безопасная пауза между попытками
     */
    private void sleep(int milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Корректно прерываем поток
            log.warn("Thread interrupted during retry delay", e);
        }
    }

    /**
     * Логирует ошибку и завершает выполнение с `fail()`
     */
    private void logErrorAndFail(Exception lastException) {
        if (lastException != null) {
            log.error("Final execution failed: {}", lastException.getMessage());
        }
        fail("Action execution failed after multiple attempts");
    }

}
