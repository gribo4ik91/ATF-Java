package com.example.elements.impl.table;


import com.example.elements.ITable;
import com.example.elements.impl.Group;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.cucumber.datatable.DataTable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

public class Table extends ElementContainer implements ITable {

    private Supplier<Group<Body>> bodyGroupComponent = () -> new Group<>(
//            getWrappedElement().findElements(By.className("contactTableBodyRow"))
            getWrappedElement().findElements(By.xpath("//*[@id=\"myTable\"]"))
                    .stream()
                    .map(el -> new Body(el, "", browser))
                    .collect(toList()));

    private Supplier<Group<Cell>> columnGroupComponent = () -> new Group<>(
            getWrappedElement().findElements(By.xpath("./thead/tr/th"))
                    .stream()
                    .map(el -> new Cell(el, "", browser))
                    .collect(toList()));

    public Table(final WebElement element, final String name, final Browser browser) {
        super(element, name, browser);
    }


    @Override
    public List<List<String>> getRowsDataByColumns(final List<String> columns) {
        return TableUtils.getRowsDataByColumns(columnGroupComponent.get(), bodyGroupComponent.get(), columns);
    }


    public List<List<String>> filterRowsData(DataTable dataTable) {
        List<List<String>> results = new ArrayList<>();
        List<Map<String, Cell>> filterRows = filterRowsComponent(dataTable);
        for (Map<String, Cell> row : filterRows) {
            results.add(row.values().stream().map(Cell::getText).collect(toList()));
        }
        return results;
    }



    private List<Map<String, Cell>> filterRowsComponent(DataTable dataTable) {
        List<Map<String, Cell>> results = new ArrayList<>();
        List<Map<String, Cell>> filterRows = TableUtils.filterRowsComponent(columnGroupComponent.get(), bodyGroupComponent.get(), dataTable);

        // Exclude unused columns
        for (Map<String, Cell> row : filterRows) {
            Map<String, Cell> newRowMap = new LinkedHashMap<>();
            for (String column : dataTable.row(0)) {
                newRowMap.put(column, row.get(column.toLowerCase()));
            }
            results.add(newRowMap);
        }
        return results;
    }

}
