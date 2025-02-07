package com.example.elements.impl.table;

import com.example.elements.impl.Group;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Body extends ElementContainer {

    private Supplier<Group<Row>> rowGroupComponent = () -> new Group<>(
            getWrappedElement().findElements(By.xpath("//*[@id=\"myTable\"]/tr"))
                    .stream()
                    .map(el -> new Row(el, "", browser))
                    .collect(Collectors.toList()));

    public Body(final WebElement element, final String name, final Browser browser) {
        super(element, name, browser);
    }


    /**
     * Get rows
     *
     * @return list of rows
     */
    public List<Map<String, Cell>> getRowsComponent(final List<String> columns) {
        List<Map<String, Cell>> results = new ArrayList<>();
        for (Row row : rowGroupComponent.get().getAll()) {
            results.add(row.getRowComponentFrom(columns));
        }
        return results;
    }
}
