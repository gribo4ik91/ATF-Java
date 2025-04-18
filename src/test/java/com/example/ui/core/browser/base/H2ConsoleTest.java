package com.example.ui.core.browser.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Базовый абстрактный класс для UI-тестов с H2-базой;
 * Загружает Spring Boot-контекст с использованием фиксированного порта;
 * Позволяет наследникам выполнять тесты с доступом к H2 консоли и Spring-окружению.
 *
 * <p>Класс `H2ConsoleTest` служит базой для тестов, которым требуется Spring Boot-контекст
 * и подключение к H2-базе данных. Используется аннотация `@SpringBootTest` с `DEFINED_PORT`,
 * чтобы приложение запускалось на стабильном порту, подходящем для UI и API взаимодействий.</p>
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class H2ConsoleTest {
    // Класс пустой, но предоставляет преднастроенное окружение
    // для наследуемых UI- или интеграционных тестов.
}
