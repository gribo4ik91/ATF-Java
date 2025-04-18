package com.example.hook;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Читает тестовые данные из JSON;
 * Генерирует SQL-скрипт с INSERT-запросами;
 * Сохраняет скрипт в файл, путь задан в `output.sql`.
 *
 * <p>Сервис `SqlGeneratorService` преобразует подготовленные тестовые данные из JSON-файла
 * (например, аккаунты и контакты) в SQL-команды для вставки в базу.
 * Результат сохраняется как файл, который позже исполняется для инициализации тестовой среды.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SqlGeneratorService {

    /**
     * Сервис для доступа к данным, хранящимся в JSON-файле.
     * Используется для получения сущностей по ключам ("accounts", "contacts" и т.д.).
     */
    private final JsonDataService jsonDataService;

    /**
     * Путь к SQL-файлу, в который будет сохранён результат генерации.
     * Указывается в application.properties через ключ `output.sql`.
     */
    @Value("${output.sql}")
    private Path outputSql;

    /**
     * Генерирует SQL-скрипт из сущностей, загруженных из JSON,
     * и сохраняет его в файл `output.sql`.
     * Поддерживает два типа сущностей: accounts и contacts.
     */
    public void generateSqlScriptFromJson() {
        List<String> sqlLines = new ArrayList<>();

        // Получаем список аккаунтов и контактов из JSON
        List<Map<String, Object>> accounts = jsonDataService.getEntities("accounts");
        List<Map<String, Object>> contacts = jsonDataService.getEntities("contacts");

        // Генерация SQL-запросов для вставки аккаунтов
        sqlLines.add("-- Accounts");
        for (Map<String, Object> acc : accounts) {
            sqlLines.add(String.format(
                    "INSERT INTO Accounts (firstName, lastName, email, password, timestamp) VALUES ('%s', '%s', '%s', '%s', '%s');",
                    acc.get("firstName"), acc.get("lastName"), acc.get("email"), acc.get("password"), acc.get("timestamp")
            ));
        }

        // Генерация SQL-запросов для вставки контактов
        sqlLines.add("\n-- Contacts");
        for (Map<String, Object> c : contacts) {
            sqlLines.add(String.format(
                    "INSERT INTO Contact (firstName, lastName, birthdate, email, phone, street1, street2, city, stateProvince, postalCode, country, accountsEmail, timestamp) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                    c.get("firstName"), c.get("lastName"), c.get("birthdate"), c.get("email"),
                    c.get("phone"), c.get("street1"), c.get("street2"), c.get("city"),
                    c.get("stateProvince"), c.get("postalCode"), c.get("country"), c.get("accountsEmail"), c.get("timestamp")
            ));
        }

        try {
            // Убеждаемся, что директория существует, иначе создаём
            Path parent = outputSql.getParent();
            if (!Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            // Записываем SQL-строки в файл
            Files.write(outputSql, sqlLines);
            log.info("SQL script generated at: {}", outputSql.toAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException("Failed to write SQL file", e);
        }
    }
}
