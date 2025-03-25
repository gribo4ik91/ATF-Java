package com.example.ui.elements.impl.table;

//import com.example.ui.elements.ITable;
import com.example.ui.elements.impl.Group;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.cucumber.datatable.DataTable;
import java.util.*;
import static java.util.stream.Collectors.toList;

public class Table extends ElementContainer {

    private final Lazy<Group<Body>> bodyGroupComponent = new Lazy<>(() -> new Group<>(
            getWrappedElement().findElements(By.xpath("//*[@id=\"myTable\"]")) // XPath для строк
                    .stream()
                    .map(el -> new Body(el, "", browser))
                    .collect(toList())));

    private final Lazy<Group<Cell>> columnGroupComponent = new Lazy<>(() -> new Group<>(
            getWrappedElement().findElements(By.xpath("./thead/tr/th")) // XPath для заголовков
                    .stream()
                    .map(el -> new Cell(el, "", browser))
                    .collect(toList())));

    public Table(final WebElement element, final String name, final Browser browser) {
        super(element, name, browser);
    }

//    @Override
    public List<List<String>> getRowsDataByColumns(final List<String> columns) {
        return TableUtils.getRowsData(columnGroupComponent.get(), bodyGroupComponent.get(), columns);
    }

    public List<List<String>> filterRowsData(DataTable dataTable) {
        return TableUtils.filterRows(columnGroupComponent.get(), bodyGroupComponent.get(), dataTable)
                .stream()
                .map(row -> row.values().stream().map(Cell::getText).collect(toList()))
                .collect(toList());
    }


}


