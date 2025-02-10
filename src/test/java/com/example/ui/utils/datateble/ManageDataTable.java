package com.example.ui.utils.datateble;

import com.example.ATFAssert;
import com.example.elements.impl.table.Table;
import com.example.ui.utils.datateble.paramreplacers.PlaceholderReplacer;
import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.DataTableTypeRegistry;
import io.cucumber.datatable.DataTableTypeRegistryTableConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ManageDataTable {

    @Autowired
    PlaceholderReplacer placeholderReplacer;

    private static final DataTableTypeRegistry registry = new DataTableTypeRegistry(Locale.ENGLISH);
    private static final DataTable.TableConverter tableConverter = new DataTableTypeRegistryTableConverter(registry);

//
    public String replaceData(final String data) {
        return StringUtils.isNotEmpty(data) ? placeholderReplacer.replaceAll(data) : StringUtils.EMPTY;
    }
//
    public DataTable overrideData(final DataTable dataTable) {
        List<List<String>> results = new ArrayList<>();
        for (List<String> row : dataTable.cells()) {
            List<String> newRow = row.stream().map(this::replaceData).collect(toList());
            results.add(newRow);
        }
        return DataTable.create(results, tableConverter);
    }


    public void matchesRecordsInTable(Table table, DataTable dataTable) {
        List<List<String>> results = new ArrayList<>();

        // Add columns
        List<String> columns = dataTable.row(0);
        results.add(dataTable.row(0));

        // Add all rows
        results.addAll(table.getRowsDataByColumns(columns));

        // Diffs expected table with actual table
        DataTable actualDataTable = DataTable.create(results);
        dataTable.unorderedDiff(actualDataTable);
    }

    public void includesRecordsInTable(Table table, DataTable dataTable) {
        List<List<String>> results = new ArrayList<>();

        // Add columns
        List<String> columns = dataTable.row(0);
        results.add(columns);

        // Add filtered rows
        results.addAll(table.filterRowsData(dataTable));

        // Verify that the row(s) include in the table
        if (dataTable.asLists().size() > results.size()) {
            // Add columns
            List<List<String>> newResults = new ArrayList<>();
            newResults.add(columns);

            // Add all rows
            newResults.addAll(table.getRowsDataByColumns(columns));

            DataTable actualDataTable = DataTable.create(newResults);
            dataTable.unorderedDiff(actualDataTable);
        }
    }

    public void notIncludesRecordsInTable(Table table, DataTable dataTable) {
        // Get filter rows
        List<List<String>> filteredRows = table.filterRowsData(dataTable);

        // Verify that the row(s) not include in the table
        ATFAssert.assertThat(String.format("The rows should not be displayed in the table.\n%s", DataTable.create(filteredRows)), filteredRows.size(), is(0));
    }


}

