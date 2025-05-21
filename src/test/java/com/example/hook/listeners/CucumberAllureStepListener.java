package com.example.hook.listeners;

import com.example.config.DriverManager;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Cucumber listener для интеграции шагов с Allure-репортом.
 * При завершении каждого шага теста:
 * - прикладываются логи (INFO, WARN, ERROR, DEBUG);
 * - прикладывается скриншот при успехе или провале шага.
 */
@Slf4j
@SpringBootTest
public class CucumberAllureStepListener implements EventListener {

    /**
     * Метод подписывает listener на событие завершения шага.
     * После регистрации, handleStepFinished будет вызываться каждый раз,
     * когда Cucumber завершает выполнение отдельного шага.
     */
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, this::handleStepFinished);
    }

    /**
     * Метод вызывается после завершения каждого шага Cucumber.
     * При успешном или проваленном шаге добавляются логи и скриншот.
     */
    private void handleStepFinished(TestStepFinished event) {
        if (event.getResult().getStatus() == Status.PASSED || event.getResult().getStatus() == Status.FAILED) {
             //Это замена вот такой конструкции:
             //if (event.getTestStep() instanceof PickleStepTestStep)
             // PickleStepTestStep step = (PickleStepTestStep) event.getTestStep();
            if (event.getTestStep() instanceof PickleStepTestStep step) {
                String stepText = step.getStep().getText();

                // Прикрепляем логи и скриншот в Allure
                attachLogs(stepText);
//                attachScreenshot(stepText);

                // Только если шаг упал, прикладываем скриншот
                if (event.getResult().getStatus() == Status.FAILED) {
                    attachScreenshot(stepText);
                }
            }
        }
    }

    /**
     * Добавляет все доступные логи из логгера шагов в Allure как отдельные вложения.
     * Информационные, предупреждающие, ошибочные и отладочные сообщения добавляются отдельно.
     */
    private void attachLogs(String stepText) {
        String info = StepLoggerAppender.getAndClearInfo();
        String warn = StepLoggerAppender.getAndClearWarn();
        String error = StepLoggerAppender.getAndClearError();
        String debug = StepLoggerAppender.getAndClearDebug();

        if (!info.isBlank()) {
            Allure.addAttachment("INFO: " + stepText, "text/plain", info);
        }
        if (!warn.isBlank()) {
            Allure.addAttachment("WARN: " + stepText, "text/plain", warn);
        }
        if (!error.isBlank()) {
            Allure.addAttachment("ERROR: " + stepText, "text/plain", error);
        }
        if (!debug.isBlank()) {
            Allure.addAttachment("DEBUG: " + stepText, "text/plain", debug);
        }
    }

    /**
     * Делает скриншот с помощью WebDriver и добавляет его в Allure.
     * Если драйвер не инициализирован — прикладывается соответствующее сообщение.
     */
    public static void attachScreenshot(String stepText) {
        WebDriver driver = DriverManager.getDriver(); // Получаем текущий WebDriver из потока

        if (driver == null) {
            Allure.addAttachment("Screenshot Failed", "text/plain", "Driver is null");
            log.warn("Driver is null during screenshot at step: {}", stepText);
            return;
        }

        try {
            // Делаем скриншот и прикладываем к Allure
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            Allure.getLifecycle().addAttachment(
                    "Screenshot: " + stepText,
                    "image/png",
                    "png",
                    new ByteArrayInputStream(screenshot)
            );

        } catch (Exception e) {
            // В случае ошибки прикладываем сообщение об ошибке в текстовом виде
            Allure.getLifecycle().addAttachment(
                    "Screenshot Failed",
                    "text/plain",
                    null,
                    new ByteArrayInputStream(e.getMessage().getBytes(StandardCharsets.UTF_8))
            );
        }
    }
}
