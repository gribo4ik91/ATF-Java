package com.example.ui.utils.datateble.paramreplacers;

import com.example.global.GlobalMap;
import com.example.global.GlobalMapKey;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * The type HashParamReplacer contains the logic of replacing data from string with values from GlobalMap instance by GlobalMapKey (if are present)
 */
@Slf4j
public class PlaceholderReplacer {

    private final static String bracketPattern = "\\[%s\\]";

    private final static String withoutBracketPattern = "\\[([^\\[\\]]*)]";

    /**
     * Replace all items in a string identifying matchers by "[","]" brackets.
     *
     * @return the string result with values from GlobalMap instance
     *
     * Example: @param text "[EMAIL] " -> return "example@getnada.com " (saved value in GlobalMap)
     */

    public String replaceAll(final String text) {
        var result = text;

        Matcher matcher = Pattern.compile(withoutBracketPattern).matcher(result);
        List<String> found = new ArrayList<>();
        while (matcher.find()) {
            found.add(matcher.group(1));
        }

        for (int i = 0; i < found.size(); i++) {
            var keyName = found.get(i);
            var initialPattern = String.format(bracketPattern, keyName);

            GlobalMapKey mapKey = GlobalMapKey.getByName(keyName);
            var data = GlobalMap.getInstance().getOrDefault(mapKey, initialPattern);

            if (String.class.isAssignableFrom(data.getClass())) {
                result = result.replaceAll(initialPattern, (String) data);
            } else if (Map.class.isAssignableFrom(data.getClass())) {
                var key = found.get(i + 1);
                var value = (((Map) data).get(key));

                if (value != null) {
                    String pattern = String.format(bracketPattern.repeat(2), keyName, key);
                    result = result.replaceFirst(pattern, (String) value);
                    i++;
                }
            }
        }
        return result;
    }

}
