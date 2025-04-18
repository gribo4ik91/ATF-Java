package com.example.hook;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Читает SQL-файл из resources;
 * Делит содержимое по ; (разделитель SQL-команд);
 * Выполняет каждую команду в базе через JdbcTemplate.
 *
 * <p>Сервис для загрузки SQL-схемы из файла, находящегося в classpath (например, в папке resources).
 * Используется для инициализации или пересоздания структуры базы данных (создание таблиц, индексов и т.д.)
 * перед запуском тестов или при старте приложения.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DbSchemaLoaderService {

    // Объект для выполнения SQL-запросов к базе данных
    private final JdbcTemplate jdbcTemplate;

    /**
     * Загружает и выполняет SQL-скрипт, указанный по пути в classpath.
     * Файл читается, разбивается на отдельные SQL-операторы по символу `;`, и каждый оператор исполняется отдельно.
     *
     * @param classpathSqlFile путь к SQL-файлу внутри classpath (например: "schema/schema.sql")
     */
    public void loadSchema(String classpathSqlFile) {
        try {
            // Загружаем файл из classpath (ресурсов)
            var resource = new ClassPathResource(classpathSqlFile);

            // Читаем содержимое файла в виде строки
            String sql = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            // Разбиваем SQL на отдельные выражения и выполняем их по одному
            for (String statement : sql.split(";")) {
                if (!statement.trim().isEmpty()) {
                    jdbcTemplate.execute(statement);
                }
            }

            log.info("Database schema loaded from {}", classpathSqlFile);

        } catch (Exception e) {
            // Оборачиваем исключение в RuntimeException с пояснением
            throw new RuntimeException("Failed to load SQL schema from: " + classpathSqlFile, e);
        }
    }
}
