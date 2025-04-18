package com.example.ui.elements.impl.table;

import com.example.ui.elements.impl.Group;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Представляет тело таблицы (например, <tbody>) и управляет доступом к строкам;
 * Использует ленивую инициализацию строк (`Row`) внутри;
 * Предоставляет метод получения строк с привязкой к именам колонок.
 *
 * <p>Класс `Body` инкапсулирует логику работы с таблицей:
 * - Получает все строки таблицы;
 * - Преобразует их в списки ячеек (Cell);
 * - Привязывает содержимое к заголовкам колонок (List<String> columns).</p>
 */
public class Body extends ElementContainer {

    // Ленивое получение списка строк таблицы (Row)
    private final Lazy<Group<Row>> rowGroupComponent = new Lazy<>(() -> new Group<>(
            getWrappedElement()
                    .findElements(By.xpath("//*[@id=\"myTable\"]/tr")) // ⚠️ уточни, если нужен относительный путь
                    .stream()
                    .map(el -> new Row(el, "", browser))
                    .collect(Collectors.toList())
    ));

    /**
     * Конструктор для инициализации тела таблицы.
     *
     * @param element  элемент, представляющий <tbody> или другой контейнер строк
     * @param name     логическое имя для логирования
     * @param browser  экземпляр браузера, через который выполняются действия
     */
    public Body(final WebElement element, final String name, final Browser browser) {
        super(element, name, browser);
    }

    /**
     * Возвращает строки таблицы как список Map, где ключ — имя колонки, значение — соответствующая ячейка (Cell).
     *
     * @param columns список названий колонок (в том порядке, в котором они ожидаются)
     * @return список строк, представленных как Map<"column name", Cell>
     */
    public List<Map<String, Cell>> getRowsComponent(final List<String> columns) {
        return rowGroupComponent.get().getAll().stream()
                .map(row -> row.getRowComponentFrom(columns))
                .collect(Collectors.toList());
    }
}
