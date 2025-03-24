package com.example.ui.utils.datateble.paramreplacers;

import com.example.global.GlobalMap;
import com.example.global.GlobalMapKey;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class PlaceholderReplacer {


    private final static String bracketPattern = "\\[%s\\]";
    private final static String withoutBracketPattern = "\\[([^\\[\\]]*)]";

    public String replaceAll(final String text) {
        String result = text;

        Matcher matcher = Pattern.compile(withoutBracketPattern).matcher(result);
        List<String> found = new ArrayList<>();

        // Сначала находим все ключи внутри квадратных скобок
        while (matcher.find()) {
            found.add(matcher.group(1));
        }

        // Обрабатываем каждый ключ
        for (int i = 0; i < found.size(); i++) {
            String keyName = found.get(i);
            String initialPattern = String.format(bracketPattern, keyName);

            GlobalMapKey mapKey = GlobalMapKey.getByName(keyName);
            Object data = GlobalMap.getInstance().getOrDefault(mapKey, initialPattern);

            // Если данные - строка, просто заменяем
            if (data instanceof String) {
                result = result.replaceAll(initialPattern, (String) data);
            }

        }

        return result;
    }


}
