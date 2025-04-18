package com.example.hook;

import com.example.global.GlobalMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.global.GlobalMapKey.FOLDR_ID;

/**
 * Создаёт уникальную папку для текущего запуска;
 * Формирует путь в формате logs/yyyy-MM-dd/run N;
 * Сохраняет имя папки в GlobalMap под ключом FOLDR_ID.
 *
 * <p>Сервис `RunFolderManager` отвечает за создание папки для текущего запуска тестов.
 * Папка создаётся внутри директории с текущей датой, и получает имя вида `run N`, где `N` — порядковый номер запуска за день.
 * Имя новой папки сохраняется в `GlobalMap` под ключом `FOLDR_ID` для последующего использования (например, для логов, скриншотов, отчётов).</p>
 */
@Service
@Slf4j
public class RunFolderManager {

    /**
     * Создаёт директорию для нового запуска.
     * Путь строится по шаблону: logs/yyyy-MM-dd/run N
     * где `N` — порядковый номер запуска за день.
     */
    public void createNextRunFolder() {
        // Получаем текущую дату и время
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String date = timestamp.substring(0, 10);

        // Путь к папке с логами по дате
        String parentDirPath = "logs/" + date;
        new File(parentDirPath).mkdir(); // создаём, если не существует

        File parentDir = new File(parentDirPath);
        if (!parentDir.exists() || !parentDir.isDirectory()) {
            log.info("Указанный путь не существует или не является директорией.");
            return;
        }

        // Регулярное выражение для поиска папок вида "run N"
        Pattern pattern = Pattern.compile("run (\\d+)");
        int maxRunNumber = 0;

        // Ищем все уже существующие папки с форматом "run N"
        File[] subDirs = parentDir.listFiles(File::isDirectory);
        if (subDirs != null) {
            for (File dir : subDirs) {
                Matcher matcher = pattern.matcher(dir.getName());
                if (matcher.matches()) {
                    int runNumber = Integer.parseInt(matcher.group(1));
                    if (runNumber > maxRunNumber) {
                        maxRunNumber = runNumber;
                    }
                }
            }
        }

        // Вычисляем номер для новой папки
        int nextRunNumber = maxRunNumber + 1;
        String newFolderName = "run " + nextRunNumber;

        // Сохраняем имя новой папки в GlobalMap (для логирования, скриншотов и т.д.)
        GlobalMap.getInstance().put(FOLDR_ID, newFolderName);

        // Создаём папку запуска
        File newRunFolder = new File(parentDir, newFolderName);
        boolean created = newRunFolder.mkdir();

        if (created) {
            log.info("Создана новая папка запуска: " + newRunFolder.getAbsolutePath());
        } else {
            log.info("Не удалось создать папку: " + newFolderName);
        }
    }
}
