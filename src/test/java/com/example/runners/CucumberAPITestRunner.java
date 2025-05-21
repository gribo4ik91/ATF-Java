package com.example.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Запускает Cucumber-тесты, помеченные тегом @API;
 * Использует движок Cucumber через JUnit 5;
 * Генерирует отчёты (HTML, JSON, Allure) в папке `target/`.
 *
 * <p>Класс `CucumberAPITestRunner` используется как точка входа для выполнения API-тестов,
 * реализованных на Cucumber + JUnit 5 (Jupiter). Выполняет все сценарии из `features`,
 * у которых указан тег `@API`, и настраивает генерацию отчётов в нескольких форматах.</p>
 */
@Suite // Обозначает, что это JUnit 5 Suite (используется для запуска набора тестов)
@IncludeEngines("cucumber") // Используем Cucumber как движок для выполнения тестов
@SelectClasspathResource("features") // Папка, в которой хранятся .feature-файлы
@ConfigurationParameter(key = "cucumber.glue", value = "com.example.steps") // Пакет, где находятся step definition'ы и хуки
@ConfigurationParameter(key = "cucumber.filter.tags", value = "@ui2") // Выполнять только сценарии, помеченные тегом @API
@ConfigurationParameter(
        key = "cucumber.plugin",
        value = "pretty, summary, html:target/cucumber-reports.html, json:target/cucumber.json,  com.example.hook.listeners.CucumberAllureStepListener,io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
) // Подключение репортов: HTML, JSON и Allure
@ConfigurationParameter(key = "cucumber.execution.monochrome", value = "true") // Отключает цветной вывод (удобно в CI)
@ConfigurationParameter(key = "cucumber.execution.step-notifications", value = "true") // Показывает шаги в выводе при запуске
public class CucumberAPITestRunner {
    // Этот класс не содержит кода — он просто конфигурация для запуска API-тестов через Cucumber + JUnit 5
}
