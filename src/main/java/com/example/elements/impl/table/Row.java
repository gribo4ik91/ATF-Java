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
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Row extends ElementContainer implements IButton {


    private Supplier<Group<Cell>> cellGroupComponent = () -> new Group<>(
            getWrappedElement().findElements(By.tagName("td"))
            .stream()
            .filter(el -> !"true".equals(el.getAttribute("hidden")))
            .map(el -> new Cell(el, "", browser)).collect(Collectors.toList()));

    public Row(final WebElement element, final String name, final Browser browser) {
        super(element, name, browser);
    }

    @Override
    public void click() {
        new ClickAction(this, browser).execute();
    }


    /**
     * Get row component from cells
     *
     * @param columns
     *
     * @return
     */
    public Map<String, Cell> getRowComponentFrom(final List<String> columns) {
        Map<String, Cell> columnHashMap = new LinkedHashMap<>();
        Map<String, Cell> indexHashMap = new LinkedHashMap<>();
        Map<String, Cell> results = new LinkedHashMap<>();

        int columnIndex = 0;
        final List<Cell> cells = cellGroupComponent.get().getAll();
        final int size = cells.size() - 1;

        for (String column : columns) {
            if (columnIndex > size) {
                break;
            }

            Cell cell = cells.get(columnIndex++);
            if (!column.isEmpty()) {
                columnHashMap.put(column.toLowerCase(), cell);
            }
            indexHashMap.put(String.valueOf(columnIndex), cell);
        }
        results.putAll(columnHashMap);
        results.putAll(indexHashMap);
        return results;
    }
}
