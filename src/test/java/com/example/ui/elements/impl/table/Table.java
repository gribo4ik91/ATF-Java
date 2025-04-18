package com.example.ui.elements.impl.table;

import com.example.ui.elements.impl.Group;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import io.cucumber.datatable.DataTable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Представляет таблицу на UI и управляет доступом к колонкам, строкам и данным;
 * Инициализирует заголовки (`columnGroupComponent`) и тело (`bodyGroupComponent`) через ленивые контейнеры;
 * Предоставляет методы для получения и фильтрации данных по колонкам.
 *
 * <p>Класс `Table` — это основной объект UI-модели таблицы. Он работает с компонентами:
 * - `thead/tr/th` — заголовки колонок;
 * - `tbody/tr` или аналогичные строки — через компонент `Body`;
 * - `Cell`, `Row`, `Group`, `Lazy` — для представления структуры таблицы.
 *
 * Поддерживает чтение данных и фильтрацию через Cucumber `DataTable`.</p>
 */
public class Table extends ElementContainer {

    // Группа тел таблицы (обычно <tbody>), обёрнута в ленивый контейнер
    private final Lazy<Group<Body>> bodyGroupComponent = new Lazy<>(() -> new Group<>(
            getWrappedElement()
                    .findElements(By.xpath("//*[@id=\"myTable\"]")) // ⚠️ по необходимости сделай XPath более универсальным
                    .stream()
                    .map(el -> new Body(el, "", browser))
                    .collect(toList())
    ));

    // Группа заголовков колонок таблицы (<th> внутри thead), обёрнута в ленивый контейнер
    private final Lazy<Group<Cell>> columnGroupComponent = new Lazy<>(() -> new Group<>(
            getWrappedElement()
                    .findElements(By.xpath("./thead/tr/th"))
                    .stream()
                    .map(el -> new Cell(el, "", browser))
                    .collect(toList())
    ));

    /**
     * Конструктор таблицы.
     *
     * @param element  WebElement, представляющий саму таблицу
     * @param name     логическое имя (например, "Users Table")
     * @param browser  браузер, в котором будет работать компонент
     */
    public Table(final WebElement element, final String name, final Browser browser) {
        super(element, name, browser);
    }

    /**
     * Возвращает данные всех строк таблицы только по указанным колонкам.
     *
     * @param columns список имён колонок, по которым нужно получить данные
     * @return таблица строк с данными (List<List<String>>)
     */
    public List<List<String>> getRowsDataByColumns(final List<String> columns) {
        return TableUtils.getRowsData(columnGroupComponent.get(), bodyGroupComponent.get(), columns);
    }

    /**
     * Фильтрует строки таблицы по данным, указанным в Cucumber DataTable.
     * Возвращает только строки, совпадающие с фильтром.
     *
     * @param dataTable таблица с фильтрами (обычно приходит из .feature-файла)
     * @return отфильтрованные строки таблицы
     */
    public List<List<String>> filterRowsData(DataTable dataTable) {
        return TableUtils.filterRows(columnGroupComponent.get(), bodyGroupComponent.get(), dataTable)
                .stream()
                .map(row -> row.values().stream()
                        .map(Cell::getText)
                        .collect(toList()))
                .collect(toList());
    }
}
