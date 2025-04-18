package com.example.hook;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Читает SQL-скрипт из файла;
 * Разбивает содержимое по `;` на отдельные SQL-операторы;
 * Выполняет каждый оператор через JdbcTemplate.
 *
 * <p>Сервис `SqlExecutorService` отвечает за выполнение заранее сгенерированного SQL-скрипта,
 * обычно содержащего `INSERT`-запросы с тестовыми данными.
 * Путь к SQL-файлу задаётся через конфигурацию `output.sql`.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SqlExecutorService {
    /**
     * Объект для работы с базой данных через Spring JDBC.
     * Позволяет выполнять SQL-запросы (SELECT, INSERT, UPDATE, DELETE) без ручного управления соединениями.
     * Внедряется автоматически через конструктор благодаря аннотации @RequiredArgsConstructor.
     */
    private final JdbcTemplate jdbcTemplate;

    // Путь к SQL-файлу, указанный в application.properties (output.sql)
    @Value("${output.sql}")
    private Path outputSql;

    /**
     * Выполняет SQL-операторы из файла.
     * Ожидается, что команды в файле разделены точкой с запятой `;`.
     * Каждая непустая команда будет выполнена через JdbcTemplate.
     */
    public void executeSqlFile() {
        try {
            // Читаем весь SQL-файл как строку
            String sql = Files.readString(outputSql);

            // Разбиваем на отдельные SQL-запросы по `;`
            String[] statements = sql.split(";");

            for (String stmt : statements) {
                if (!stmt.trim().isEmpty()) {
                    jdbcTemplate.execute(stmt.trim()); // Выполняем каждую команду
                }
            }

            log.info("SQL-script executed successfully: {}", outputSql.toAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException("Error while executing SQL script", e);
        }
    }
}
