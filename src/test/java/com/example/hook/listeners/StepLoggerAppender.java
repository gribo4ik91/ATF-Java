package com.example.hook.listeners;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ch.qos.logback.classic.Level;
import org.springframework.stereotype.Service;

/**
 * Кастомный appender для Logback, предназначен для сбора логов по уровням (INFO, WARN, ERROR, DEBUG)
 * в рамках текущего потока. Используется для дальнейшего прикрепления логов к шагам в Allure.
 */
@Service
public class StepLoggerAppender extends AppenderBase<ILoggingEvent> {

    // Хранение логов по уровням отдельно для каждого потока (по threadId)
    private static final ConcurrentMap<Long, StringBuilder> infoLogs = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Long, StringBuilder> warnLogs = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Long, StringBuilder> errorLogs = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Long, StringBuilder> debugLogs = new ConcurrentHashMap<>();

    /**
     * Метод вызывается при каждом лог-событии.
     * Классифицирует сообщение по уровню логирования и добавляет его в соответствующую коллекцию.
     */
    @Override
    protected void append(ILoggingEvent event) {
        long threadId = Thread.currentThread().getId(); // Идентификатор текущего потока
        String message = event.getFormattedMessage() + "\n"; // Текст сообщения

        Level level = event.getLevel();
        if (level.equals(Level.INFO)) {
            infoLogs.computeIfAbsent(threadId, k -> new StringBuilder()).append(message);
        } else if (level.equals(Level.WARN)) {
            warnLogs.computeIfAbsent(threadId, k -> new StringBuilder()).append(message);
        } else if (level.equals(Level.ERROR)) {
            errorLogs.computeIfAbsent(threadId, k -> new StringBuilder()).append(message);
        } else if (level.equals(Level.DEBUG)) {
            debugLogs.computeIfAbsent(threadId, k -> new StringBuilder()).append(message);
        }
    }

    // Методы для получения и очистки логов определенного уровня
    public static String getAndClearInfo() {
        return getAndClear(infoLogs);
    }

    public static String getAndClearWarn() {
        return getAndClear(warnLogs);
    }

    public static String getAndClearError() {
        return getAndClear(errorLogs);
    }

    public static String getAndClearDebug() {
        return getAndClear(debugLogs);
    }

    /**
     * Получает логи текущего потока из указанной карты и очищает их.
     *
     * @param map Коллекция логов, сгруппированных по потокам, для одного из уровней логирования (INFO, WARN, ERROR, DEBUG).
     *            Ключом является ID потока (threadId), значением — накопленные логи (StringBuilder) для этого потока.
     * @return Строка с логами текущего потока, либо пустая строка, если логов нет.
     */
    private static String getAndClear(ConcurrentMap<Long, StringBuilder> map) {
        long threadId = Thread.currentThread().getId();
        StringBuilder sb = map.getOrDefault(threadId, new StringBuilder());

        // Очищаем лог после получения, чтобы избежать дублирования между шагами
        map.put(threadId, new StringBuilder());

        return sb.toString();
    }
}
