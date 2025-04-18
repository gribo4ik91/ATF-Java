package com.example.hook;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Загружает и сохраняет JSON-файл с тестовыми данными;
 * Добавляет записи с таймстемпом и ограничением по количеству;
 * Предоставляет доступ к данным по ключу (например, "accounts", "contacts").
 *
 * <p>Сервис `JsonDataService` управляет хранением тестовых данных в формате JSON.
 * Поддерживает операции чтения, записи, добавления сущностей с автоматической обрезкой
 * старых записей по таймстемпу и максимальному количеству (`max.rows`).</p>
 */
@Service
@Slf4j
public class JsonDataService {

    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${json.path}")
    private Path jsonPath;

    @Value("${max.rows}")
    private int maxRows;

    /**
     * Загружает данные из JSON-файла, указанный в настройке `json.path`.
     * Если файл отсутствует, возвращается пустая структура.
     *
     * @return Map, содержащая все данные (accounts, contacts и т.д.)
     */
    public Map<String, Object> loadJson() {
        try {
            File file = jsonPath.toFile();
            if (file.exists()) {
                return mapper.readValue(file, new TypeReference<>() {});
            } else {
                return new HashMap<>();
            }
        } catch (Exception e) {
            log.error("Failed to load JSON file: {}", jsonPath, e);
            throw new RuntimeException("Failed to load JSON file", e);
        }
    }

    /**
     * Сохраняет переданные данные в JSON-файл (`json.path`) с форматированием.
     *
     * @param data данные, которые нужно сохранить
     */
    public void saveJson(Map<String, Object> data) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonPath.toFile(), data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save JSON file", e);
        }
    }

    /**
     * Добавляет новую сущность в список по заданному ключу (например: "accounts", "contacts").
     * Каждая сущность получает timestamp. Если превышен лимит `max.rows`, старые записи обрезаются.
     *
     * @param key    ключ (категория), к которой добавляется сущность
     * @param entity новая сущность в виде Map<String, Object>
     */
    public void addEntity(String key, Map<String, Object> entity) {
        Map<String, Object> data = loadJson();

        List<Map<String, Object>> list = (List<Map<String, Object>>) data.getOrDefault(key, new ArrayList<>());

        entity.put("timestamp", LocalDateTime.now().toString());
        list.add(entity);

        // Сортировка по timestamp и обрезка до maxRows
        list = list.stream()
                .sorted(Comparator.comparing(e -> e.get("timestamp").toString()))
                .skip(Math.max(0, list.size() - maxRows))
                .collect(Collectors.toList());

        data.put(key, list);
        saveJson(data);
    }

    /**
     * Получает список сущностей по ключу (например: "accounts", "contacts").
     *
     * @param key ключ (категория)
     * @return список сущностей, либо пустой список, если таких нет
     */
    public List<Map<String, Object>> getEntities(String key) {
        Map<String, Object> data = loadJson();
        return (List<Map<String, Object>>) data.getOrDefault(key, new ArrayList<>());
    }
}
