package com.example.ui.core;

import com.example.ui.core.browser.Browser;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;
import org.springframework.stereotype.Controller;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.example.ATFAssert.fail;

/**
 * Базовый абстрактный класс для всех UI-действий;
 * Содержит механизм повторных попыток выполнения действий при WebDriver-исключениях;
 * Гарантирует ожидание загрузки страницы перед выполнением действия.
 *
 * <p>Классы-наследники (например, {@code ClickAction}, {@code GetTextAction}) используют методы {@code execute()},
 * чтобы выполнять действия над элементами безопасно, с повторными попытками и логированием.</p>
 */
@Slf4j
@Controller
public abstract class AbstractAction {

    private static final int ATTEMPTS_NUMBER = 3;       // Количество повторных попыток
    private static final int RETRY_DELAY_MS = 500;      // Задержка между попытками

    protected Browser browser;
    protected ElementContainer element;

    /**
     * Конструктор действия.
     *
     * @param element элемент, над которым будет производиться действие
     * @param browser браузер, через который осуществляется управление
     */
    public AbstractAction(ElementContainer element, Browser browser) {
        this.element = element;
        this.browser = browser;
    }

    /**
     * Выполняет действие без возвращаемого значения, с контролем загрузки страницы и повторными попытками.
     *
     * @param action действие, реализованное как {@link Runnable}
     */
    protected void execute(Runnable action) {
        browser.waitCurrentPageToLoad();
        retryAction(action);
    }

    /**
     * Выполняет действие, возвращающее результат, с контролем загрузки страницы и повторными попытками.
     *
     * @param action действие, реализованное как {@link Supplier}
     * @param <T>    тип возвращаемого значения
     * @return результат выполнения действия, либо null при неудаче
     */
    protected <T> T execute(Supplier<T> action) {
        browser.waitCurrentPageToLoad();
        return retryAction(action);
    }

    /**
     * Выполняет {@link Supplier}-действие с повторными попытками.
     *
     * @param action действие, возвращающее результат
     * @param <T>    тип результата
     * @return результат выполнения или null, если все попытки не удались
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

    /**
     * Выполняет {@link Runnable}-действие с повторными попытками.
     *
     * @param action действие без возвращаемого значения
     */
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
     * Безопасная задержка между попытками.
     *
     * @param milliseconds длительность паузы
     */
    private void sleep(int milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Thread interrupted during retry delay", e);
        }
    }

    /**
     * Логирует последнюю ошибку и завершает тест с фейлом.
     *
     * @param lastException последняя пойманная ошибка
     */
    private void logErrorAndFail(Exception lastException) {
        if (lastException != null) {
            log.error("Final execution failed: {}", lastException.getMessage());
        }
        fail("Action execution failed after multiple attempts");
    }
}
