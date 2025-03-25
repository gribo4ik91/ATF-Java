package com.example.ui.elements.impl.table;

import com.example.ui.elements.impl.Group;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Body extends ElementContainer {


    private final Lazy<Group<Row>> rowGroupComponent = new Lazy<>(() -> new Group<>(
            getWrappedElement().findElements(By.xpath("//*[@id=\"myTable\"]/tr"))
                    .stream()
                    .map(el -> new Row(el, "", browser))
                    .collect(Collectors.toList())));

    public Body(final WebElement element, final String name, final Browser browser) {
        super(element, name, browser);
    }


    /**
     * Получает строки в виде списка `Map<String, Cell>` на основе переданных колонок.
     */
    public List<Map<String, Cell>> getRowsComponent(final List<String> columns) {
        return rowGroupComponent.get().getAll().stream()
                .map(row -> row.getRowComponentFrom(columns))
                .collect(Collectors.toList());
    }
}


