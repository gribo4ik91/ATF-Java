package com.example.config;

import dev.failsafe.Failsafe;
import dev.failsafe.RetryPolicy;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * Конфигурация WebDriver с поддержкой различных браузеров, headless-режимов,
 * автоматической загрузкой драйверов и повторными попытками запуска при ошибках.
 *
 * <p>Класс `WebDriverConfig` создает и настраивает экземпляр WebDriver в зависимости от указанного типа браузера.
 * Поддерживается: Chrome, Chrome Headless, Firefox, Firefox Headless, Edge.
 * Также добавлен shutdown hook для корректного завершения сессии при завершении JVM.</p>
 */
@Slf4j
public class WebDriverConfig {



    /**
     * Тип браузера, передается из `application.properties`.
     * Возможные значения: chrome, chrome-headless, firefox, firefox-headless, edge.
     * По умолчанию используется chrome.
     */
    @Value("${browser.type:chrome}")
    private String browserType;

    /**
     * Создает и возвращает настроенный WebDriver.
     * Использует Failsafe для повторных попыток и автоматическую загрузку драйвера с помощью WebDriverManager.
     *
     * @return WebDriver, готовый к использованию
     */
    public WebDriver driver() {
        // Политика повторных попыток запуска драйвера
        RetryPolicy<Object> retryPolicy = RetryPolicy.builder()
                .withDelay(Duration.ofSeconds(3))
                .withMaxRetries(3)
                .onRetry(e -> log.warn("Попытка перезапуска WebDriver: {}", e.getLastException().getMessage()))
                .onFailure(e -> log.error("Не удалось инициализировать WebDriver после {} попыток", e.getAttemptCount()))
                .build();

        // Поставщик WebDriver в зависимости от типа браузера
        Supplier<WebDriver> driverSupplier;

        switch (browserType.toLowerCase()) {
            case "chrome-headless":
                driverSupplier = () -> {
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeHeadless = new ChromeOptions();
                    // --headless=new           : включает новый headless-режим, более стабильный и совместимый с UI-рендерингом
                    // --no-sandbox             : отключает песочницу Chrome, необходимую для запуска в CI/CD или Docker (может вызывать ошибки без этого флага)
                    // --disable-dev-shm-usage : отключает использование /dev/shm, переключая Chrome на использование диска — важно для стабильности в контейнерах
                    chromeHeadless.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");

                    log.info("Launching Chrome in headless-mode");
                    return new ChromeDriver(chromeHeadless);
                };
                break;

            case "firefox":
                driverSupplier = () -> {
                    WebDriverManager.firefoxdriver().setup();
                    log.info("Launching Firefox with GUI");
                    return new FirefoxDriver();
                };
                break;

            case "firefox-headless":
                driverSupplier = () -> {
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxHeadless = new FirefoxOptions();
                    firefoxHeadless.addArguments("--headless");
                    log.info("Launching Firefox in headless-mode");
                    return new FirefoxDriver(firefoxHeadless);
                };
                break;

            case "edge":
                driverSupplier = () -> {
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    log.info("Launching Microsoft Edge with GUI");
                    return new EdgeDriver(edgeOptions);
                };
                break;

            case "chrome":
            default:
                driverSupplier = () -> {
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--start-maximized");
                    log.info("Launching Chrome with GUI");
                    return new ChromeDriver(options);
                };
                break;
        }

        // Запуск драйвера с политикой повторных попыток
        WebDriver driver = Failsafe.with(retryPolicy).get(driverSupplier::get);

        // Автоматическое закрытие драйвера при завершении JVM
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Закрытие WebDriver...");
            try {
                driver.quit();
            } catch (Exception e) {
                log.warn("Ошибка при закрытии WebDriver: {}", e.getMessage());
            }
        }));

        return driver;
    }
}
