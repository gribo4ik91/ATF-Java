package com.example.ui.elements.impl.table;

import com.example.ui.action.ClickAction;
import com.example.ui.elements.impl.Group;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Представляет собой строку таблицы;
 * Содержит ячейки (`Cell`), инициализируемые лениво через `Lazy<Group<Cell>>`;
 * Позволяет кликать по строке и получать данные в виде Map<"column", Cell>.
 *
 * <p>Класс `Row` предназначен для управления одной строкой таблицы и получения ячеек в контексте колонок.
 * Он используется в связке с `Body` и `Cell` для построения объектной модели таблицы.
 * Поддерживает клик по строке и удобное сопоставление колонок с данными.</p>
 */
public class Row extends ElementContainer {

    // Ленивое получение ячеек строки, исключая скрытые элементы (с атрибутом hidden="true")
    private final Lazy<Group<Cell>> cellGroupComponent = new Lazy<>(() -> new Group<>(
            getWrappedElement()
                    .findElements(By.tagName("td"))
                    .stream()
                    .filter(el -> !"true".equals(el.getAttribute("hidden")))
                    .map(el -> new Cell(el, "", browser))
                    .collect(Collectors.toList())
    ));

    /**
     * Конструктор строки таблицы.
     *
     * @param element  WebElement строки (<tr>)
     * @param name     логическое имя (можно оставить пустым)
     * @param browser  браузер, в котором будет выполняться логика
     */
    public Row(final WebElement element, final String name, final Browser browser) {
        super(element, name, browser);
    }

    /**
     * Выполняет клик по всей строке таблицы.
     */
    public void click() {
        new ClickAction(this, browser).execute();
    }

    /**
     * Возвращает ячейки строки в виде Map, где:
     * - ключ — это имя колонки (в нижнем регистре);
     * - дополнительный ключ — индекс (строка, начиная с 1);
     *
     * @param columns список имён колонок (в порядке, соответствующем таблице)
     * @return Map с данными строки: "columnName" → Cell, "1" → Cell и т.д.
     */
    public Map<String, Cell> getRowComponentFrom(final List<String> columns) {
        List<Cell> cells = cellGroupComponent.get().getAll();
        Map<String, Cell> rowMap = new LinkedHashMap<>();

        IntStream.range(0, Math.min(columns.size(), cells.size()))
                .forEach(i -> {
                    String columnName = columns.get(i).toLowerCase(); // нормализуем ключ
                    rowMap.put(columnName, cells.get(i));             // доступ по названию колонки
                    rowMap.put(String.valueOf(i + 1), cells.get(i));  // доступ по индексу (1-based)
                });

        return rowMap;
    }
}
