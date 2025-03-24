package com.example.elements.impl.table;

import com.example.action.ClickAction;
import com.example.elements.IButton;
import com.example.elements.impl.Group;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Row extends ElementContainer implements IButton {


    private final Lazy<Group<Cell>> cellGroupComponent = new Lazy<>(() -> new Group<>(
            getWrappedElement().findElements(By.tagName("td"))
            .stream()
            .filter(el -> !"true".equals(el.getAttribute("hidden")))
            .map(el -> new Cell(el, "", browser)).collect(Collectors.toList())));

    public Row(final WebElement element, final String name, final Browser browser) {
        super(element, name, browser);
    }

    @Override
    public void click() {
        new ClickAction(this, browser).execute();
    }


    /**
     * Создаёт `Map<String, Cell>`, связывая ячейки с колонками (по имени и индексу).
     */
    public Map<String, Cell> getRowComponentFrom(final List<String> columns) {
        List<Cell> cells = cellGroupComponent.get().getAll();
        Map<String, Cell> rowMap = new LinkedHashMap<>();

        IntStream.range(0, Math.min(columns.size(), cells.size()))
                .forEach(i -> {
                    String columnName = columns.get(i).toLowerCase();
                    rowMap.put(columnName, cells.get(i)); // По имени колонки
                    rowMap.put(String.valueOf(i + 1), cells.get(i)); // По индексу (1-based)
                });

        return rowMap;
    }
}
