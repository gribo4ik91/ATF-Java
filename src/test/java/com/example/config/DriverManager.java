package com.example.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Класс DriverManager предназначен для управления объектом WebDriver в потоко-безопасной среде.
 * Используется ThreadLocal для хранения WebDriver отдельно для каждого потока выполнения (например, при параллельных тестах).
 */
public class DriverManager {

    // Потокобезопасное хранилище WebDriver, отдельное для каждого потока
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Сохраняет WebDriver в ThreadLocal для текущего потока.
     *
     * @param driver экземпляр WebDriver, который нужно сохранить
     */
    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }

    /**
     * Возвращает текущий WebDriver для потока, если он существует и сессия активна.
     * Если используется RemoteWebDriver и сессия мертва (null), то удаляет его.
     *
     * @return WebDriver или null, если сессия завершена или отсутствует
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();

        // Проверяем, является ли WebDriver удалённым и неактивным (нет sessionId)
        if (driver instanceof RemoteWebDriver remote && remote.getSessionId() == null) {
            // Сессия мертва — очистим driverThreadLocal
            removeDriver();
            return null;
        }

        // Возвращаем валидный WebDriver
        return driver;
    }

    /**
     * Удаляет WebDriver из ThreadLocal для текущего потока.
     * Используется для очистки ресурсов после завершения работы потока.
     */
    public static void removeDriver() {
        driverThreadLocal.remove();
    }
}
