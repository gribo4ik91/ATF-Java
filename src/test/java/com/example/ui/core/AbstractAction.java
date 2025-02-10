package com.example.ui.core;


//import com.example.listener.TestLogHelper;
import com.example.ui.core.browser.Browser;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;
import org.springframework.stereotype.Controller;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.example.ATFAssert.fail;


@Slf4j
@Controller
public abstract class AbstractAction {

//    private static String currentLogName = "";

    private final static int ATTEMPTS_NUMBER = 3;

    protected Browser browser;

    protected ElementContainer element;

    public AbstractAction(ElementContainer element, Browser browser) {
        this.element = element;
        this.browser = browser;
    }


    protected void execute(Runnable action) {
        browser.waitCurrentPageToLoad();
        Exception lastException = null;
//        updateIndexIfNewTest();
        if (!isTypeAction(action)) {

        }

        for (int i = 1; i <= ATTEMPTS_NUMBER; i++) {
            try {
                action.run();
                if (!isQuickAction(action)) {

                }
                return;
            } catch (WebDriverException ex) {
                lastException = ex;
                log.error("Runnable action execution failed on attempt " + i + " on element: " + element.getName());
            }
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }
        }
        log.error(lastException.getMessage());
        fail("Runnable action execution failed");
    }

    protected <T> T execute(Supplier<T> action) {
        browser.waitCurrentPageToLoad();
        Exception lastException = null;
//        updateIndexIfNewTest();

        T result = null;
        for (int i = 1; i <= ATTEMPTS_NUMBER; i++) {
            try {
                result = action.get();
                break;
            } catch (WebDriverException ex) {
                lastException = ex;
                log.error("Runnable action execution failed on attempt " + i + " on element: " + element.getName());
            }
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }
        }
        if (!isGetText(action)) {

        }
        if (Objects.nonNull(result)) {
            return result;
        }
        if (Objects.nonNull(lastException)) {
            log.error(lastException.getMessage());
        }
        fail("Runnable action execution failed");
        return null;
    }


    private Boolean isClickAction(Runnable action) {
        return action.getClass().getSimpleName().contains("Click");
    }

    private Boolean isSendKeysAction(Runnable action) {
        return action.getClass().getSimpleName().contains("SendKeys");
    }

    private Boolean isTypeAction(Runnable action) {
        return action.getClass().getSimpleName().contains("Type");
    }

    private Boolean isQuickAction(Runnable action) {
        return isClickAction(action) || isSendKeysAction(action);
    }

    private Boolean isGetText(Supplier action) {
        return action.getClass().getSimpleName().contains("GetText");
    }

}
