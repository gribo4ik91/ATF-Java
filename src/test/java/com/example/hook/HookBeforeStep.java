package com.example.hook;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;
import com.example.global.GlobalMap;
import com.example.ui.core.browser.Browser;
import io.cucumber.java.*;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestStepFinished;
import lombok.extern.slf4j.Slf4j;
import org.apiguardian.api.API;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import ch.qos.logback.classic.Level;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.example.global.GlobalMapKey.FOLDR_ID;
import static com.example.hook.listeners.CucumberAllureStepListener.attachScreenshot;

/**
 * Создаёт отдельный лог-файл для каждого сценария;
 * Запускает WebDriver перед сценарием и останавливает его после;
 * Использует MDC и Logback для изолированного логирования.
 *
 * <p>Класс HookBeforeStep реализует Cucumber-хуки:
 * - `@Before` для инициализации логгера и WebDriver;
 * - `@After` для остановки драйвера и очистки MDC;
 * - Каждому сценарию создаётся собственный лог-файл;
 * - Логи настраиваются через Logback API вручную и сохраняются в папке logs/[дата]/[folderId]/</p>
 */
@Slf4j
public class HookBeforeStep {

    private static FileAppender fileAppender = new FileAppender();

    @Autowired
    private Browser browser;

    /**
     * Запускается перед каждым сценарием.
     * Инициализирует структуру логирования — создаёт уникальный файл для текущего сценария.
     */
    @Before
    public void setUpScenarioLogging(Scenario scenario) {
        // Генерируем безопасное имя сценария
        String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9_\\-]", "_");

        // Дата и время
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String timeStampDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH"));
        String date = timestamp.substring(0, 10);
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        // Получаем folderId из GlobalMap
        String folderId = (String) GlobalMap.getInstance().get(FOLDR_ID);
        log.info("folderId: {}", folderId);

        // Формируем путь к лог-файлу
        String logFileName = scenarioName + "_" + uuid;
        String logPath = "logs/" + date + "/" + folderId + "/" + logFileName + ".log";

        // Сохраняем имя сценария в MDC (для использования в шаблоне логов)
        MDC.put("scenario", logFileName);

        // Получаем logback context
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        // Настройка encoder с шаблоном для записи логов
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        encoder.start();

        // Настраиваем appender (куда писать логи)
        fileAppender.setContext(context);
        fileAppender.setName("SCENARIO_APPENDER_" + logFileName);
        fileAppender.setFile(logPath);
        fileAppender.setEncoder(encoder);
        fileAppender.start();

        // Подключаем appender к ROOT логгеру
        Logger rootLogger = context.getLogger("ROOT");
        rootLogger.setLevel(Level.INFO);
        rootLogger.setAdditive(true); // Добавлять к существующим appender'ам
        rootLogger.addAppender(fileAppender);

        log.info("Logging for scenario: " + scenarioName + " → " + logPath);
    }

    /**
     * Завершение сценария — закрываем лог-файл и очищаем MDC.
     */
    @After
    public void clearScenarioLogging() {
        fileAppender.stop(); // Закрываем appender
        MDC.clear();         // Очищаем контекст логирования
    }

    /**
     * Перед каждым сценарием запускаем WebDriver через Browser.
     */

    @Before
    public void setUp(Scenario scenario) {
        if (!scenario.getSourceTagNames().contains("@API") && !scenario.getSourceTagNames().contains("@SQL")) {
            browser.getDriver();// Инициализация драйвера
        } else {
            log.info("Не UI тест — браузер не запускается");
        }
    }

    /**
     * После каждого сценария — останавливаем драйвер.
     */
    @After
    public void tearDown() {
        browser.stop(); // Внутри вызывается DriverManager.removeDriver()
    }
}
