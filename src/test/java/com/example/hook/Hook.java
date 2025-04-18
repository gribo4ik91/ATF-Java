package com.example.hook;

import com.example.global.GlobalMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;

import static com.example.global.GlobalMapKey.*;

/**
 * Загружает схему базы из resources;
 * Инициализирует тестовые данные и сохраняет их в JSON;
 * Генерирует SQL-скрипт и выполняет его в тестовой БД;
 * Создаёт структуру для хранения результатов запуска.
 *
 * <p>Класс `Hook` выполняет инициализацию тестового окружения после запуска Spring-контекста:
 * - Загружает SQL-схему из файла;
 * - Генерирует данные (аккаунт, контакт) и сохраняет в JSON;
 * - Преобразует JSON в SQL и применяет скрипт к базе;
 * - Создаёт структуру каталогов для сохранения артефактов запуска.
 * Используется как вспомогательный шаг перед выполнением тестов.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class Hook {

    private final DbSchemaLoaderService dbSchemaLoaderService;
    private final TestDataInitializer testDataInitializer;
    private final JsonDataService jsonDataService;
    private final SqlGeneratorService sqlGeneratorService;
    private final SqlExecutorService sqlExecutorService;
    private final RunFolderManager runFolderManager;

    /**
     * Выполняется автоматически после инициализации Spring-контекста.
     * Отвечает за полную подготовку среды:
     * 1. Загрузка схемы БД;
     * 2. Генерация и запись тестовых данных;
     * 3. Генерация SQL-скрипта и его выполнение;
     * 4. Создание каталога отчёта.
     */
    @PostConstruct
    public void init() {
        dbSchemaLoaderService.loadSchema("sql/DBSchema.sql"); // Загружаем SQL-схему
        testDataInitializer.initializeGlobalMap();            // Инициализируем тестовые данные
        createAccount();                                      // Добавляем аккаунт в JSON
        createContact();                                      // Добавляем контакт в JSON
        sqlGeneratorService.generateSqlScriptFromJson();      // Генерируем SQL из JSON
        sqlExecutorService.executeSqlFile();                  // Выполняем SQL в БД
        runFolderManager.createNextRunFolder();               // Создаём каталог запуска
    }

    /**
     * Создаёт тестовую запись аккаунта и сохраняет её в JSON.
     * Используется при подготовке тестовых данных.
     */
    private void createAccount() {
        Map<String, Object> account = new HashMap<>();
        account.put("firstName", GlobalMap.getInstance().get(FIRST_NAME));
        account.put("lastName", GlobalMap.getInstance().get(LAST_NAME));
        account.put("email", GlobalMap.getInstance().get(GENERATED_EMAIL_API));
        account.put("password", GlobalMap.getInstance().get(PASSWORD));

        jsonDataService.addEntity("accounts", account);
    }

    /**
     * Создаёт тестовую запись контакта и сохраняет её в JSON.
     * Используется для формирования связанной с аккаунтом информации.
     */
    private void createContact() {
        Map<String, Object> contact = new HashMap<>();
        contact.put("firstName", GlobalMap.getInstance().get(FIRST_NAME));
        contact.put("lastName", GlobalMap.getInstance().get(LAST_NAME));
        contact.put("birthdate", "2000-05-20");
        contact.put("email", GlobalMap.getInstance().get(GENERATED_EMAIL_API));
        contact.put("phone", GlobalMap.getInstance().get(PHONE_NUMBER));
        contact.put("street1", GlobalMap.getInstance().get(GENERATED_STREET));
        contact.put("street2", GlobalMap.getInstance().get(GENERATED_BUILDINGNUMBER));
        contact.put("city", GlobalMap.getInstance().get(GENERATED_CITY));
        contact.put("stateProvince", GlobalMap.getInstance().get(GENERATED_STATE));
        contact.put("postalCode", GlobalMap.getInstance().get(GENERATED_POSTCODE));
        contact.put("country", GlobalMap.getInstance().get(GENERATED_COUNTRY));
        contact.put("accountsEmail", GlobalMap.getInstance().get(GENERATED_EMAIL_API));

        jsonDataService.addEntity("contacts", contact);
    }
}
