package com.example.ui.utils.datateble.paramreplacers;

import com.example.global.GlobalMap;
import com.example.global.GlobalMapKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Заменяет плейсхолдеры вида [KEY_NAME] в строке на значения из {@link GlobalMap};
 * Поддерживает множество подстановок за один проход;
 * Используется при работе с DataTable, запросами, UI-проверками и т.п.
 *
 * <p>Класс `PlaceholderReplacer` сканирует входной текст на наличие плейсхолдеров в квадратных скобках
 * и заменяет их значениями, взятыми из глобального кэша (`GlobalMap`), по соответствующему {@link GlobalMapKey}.</p>
 * <p>
 * Пример:
 * Вход: "User email is [GENERATED_EMAIL]"
 * Выход: "User email is test123@example.com" — если значение есть в GlobalMap
 */
@Component
@Slf4j
public class PlaceholderReplacer {

    // Шаблон подстановки — пример: \[KEY_NAME]
    private static final String bracketPattern = "\\[%s\\]";

    // Регулярка для нахождения всех плейсхолдеров в тексте: [KEY]
    private static final String withoutBracketPattern = "\\[([^\\[\\]]*)]";

    /**
     * Заменяет все плейсхолдеры в переданной строке значениями из GlobalMap.
     *
     * @param text строка, в которой нужно выполнить замену (может содержать [KEY])
     * @return строка с подставленными значениями, если ключ найден; иначе — возвращается исходный плейсхолдер
     */
    public String replaceAll(final String text) {
        String result = text;

        // Ищем все вхождения [KEY]
        Matcher matcher = Pattern.compile(withoutBracketPattern).matcher(result);
        List<String> found = new ArrayList<>();

        while (matcher.find()) {
            found.add(matcher.group(1)); // извлекаем ключ без скобок
        }

        // Заменяем каждый плейсхолдер
        for (String keyName : found) {
            String initialPattern = String.format(bracketPattern, keyName);

            // Получаем ключ в виде enum
            GlobalMapKey mapKey = GlobalMapKey.getByName(keyName);

            // Получаем значение по ключу
            Object data = GlobalMap.getInstance().getOrDefault(mapKey, initialPattern);

//            // Если значение строка — подставляем
//            if (data instanceof String) {
//                result = result.replaceAll(initialPattern, (String) data);
//            }

            if (data != null) {
                result = result.replaceAll(initialPattern, data.toString());
            }
        }

        return result;
    }
}
