package com.example.elements.impl.table;

import com.example.elements.impl.Group;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import io.cucumber.datatable.DataTable;

import static java.util.Objects.nonNull;

public class TableUtils {


    /**
     * Get all rows component
     *
     * @return list of all rows component
     */
    public static List<Map<String, Cell>> getAllRowsComponent(Group<Cell> columnGroupComponent, Group<Body> bodyGroupComponent) {
        List<Map<String, Cell>> results = new ArrayList<>();
        List<String> columnsComponent = columnGroupComponent.getTextFromGroupComponent();

        for (Body body : bodyGroupComponent.getAll()) {
            List<Map<String, Cell>> rowsComponent = body.getRowsComponent(columnsComponent);
            results.addAll(rowsComponent);
        }
        return results;
    }

    /**
     * Get all rows component by columns
     *
     * @return list of all rows component by columns
     */
    public static List<Map<String, Cell>> getAllRowsComponentByColumns(Group<Cell> columnGroupComponent,
                                                                       Group<Body> bodyGroupComponent,
                                                                       final List<String> columns) {
        List<Map<String, Cell>> results = new ArrayList<>();
        List<Map<String, Cell>> allRowsComponent = getAllRowsComponent(columnGroupComponent, bodyGroupComponent);

        for (Map<String, Cell> rowComponent : allRowsComponent) {
            Map<String, Cell> newRowComponent = new LinkedHashMap<>();
            for (String column : columns) {
                newRowComponent.put(column.toLowerCase(), rowComponent.get(column.toLowerCase()));
            }
            results.add(newRowComponent);
        }
        return results;
    }

    /**
     * Get rows data by columns
     *
     * @return list of rows data by columns
     */
    public static List<List<String>> getRowsDataByColumns(Group<Cell> columnGroupComponent, Group<Body> bodyGroupComponent, final List<String> columns) {
        List<List<String>> results = new ArrayList<>();
        List<Map<String, Cell>> allRowsComponent = getAllRowsComponentByColumns(columnGroupComponent, bodyGroupComponent, columns);

        for (Map<String, Cell> rowComponent : allRowsComponent) {
            List<String> dataList = getData(rowComponent);
            if (!dataList.stream().allMatch(String::isEmpty)) {
                results.add(dataList);
            }
        }
        return results;
    }

    /**
     * Filter rows component by criteria (dataTable)
     *
     * @param columnGroupComponent
     * @param bodyGroupComponent
     * @param dataTable
     *
     * @return list of filtered rows component by criteria
     *
     * @throws Exception
     */
    public static List<Map<String, Cell>> filterRowsComponent(Group<Cell> columnGroupComponent, Group<Body> bodyGroupComponent, DataTable dataTable) {
        List<Map<String, Cell>> results = new ArrayList<>();
        List<Map<String, Cell>> allRowsComponent = getAllRowsComponent(columnGroupComponent, bodyGroupComponent);

        for (Map<String, Cell> rowComponent : allRowsComponent) {
            for (Map<String, String> rowData : dataTable.asMaps()) {
                Map<String, Cell> foundRowComponent = findEntryInRowComponent(rowData, rowComponent);
                if (!foundRowComponent.isEmpty()) {
                    results.add(foundRowComponent);
                }
            }
        }
        return results;
    }

    /**
     * Get text from cell
     *
     * @param cell
     *
     * @return text from cell otherwise empty string
     */
    private static String getTextOrDefault(Cell cell) {
        return cell != null ? cell.getText() : "";
    }

    /**
     * Get sub cell texts from cell
     *
     * @param cell
     *
     * @return list of texts from cell otherwise empty list
     */
    private static List<String> getSubCellTextsOrDefault(Cell cell) {
        if (nonNull(cell)) {
            List<String> subCells = cell.getSubCellTexts();
            if (subCells.size() == 0) {
                subCells.add(cell.getText());
            }
            return subCells;
        }
        return new ArrayList<>();
    }

    /**
     * Get all data from row
     *
     * @return list of data
     */
    private static List<String> getData(Map<String, Cell> rowMap) {
        List<String> results = new ArrayList<>();
        for (Map.Entry<String, Cell> entry : rowMap.entrySet()) {
            Cell cell = entry.getValue();
            String text = getTextOrDefault(cell);
            results.add(text);
        }
        return results;
    }

    /**
     * Find entry in the row component
     *
     * @param rowData
     * @param rowComponent
     *
     * @return map of row
     */
    private static Map<String, Cell> findEntryInRowComponent(Map<String, String> rowData, Map<String, Cell> rowComponent) {
        for (Map.Entry<String, String> entry : rowData.entrySet()) {
            Cell cell = rowComponent.get(entry.getKey().toLowerCase());
            List<String> subCells = getSubCellTextsOrDefault(cell);
            if (!subCells.contains(entry.getValue())) {
                return new LinkedHashMap<>();
            }
        }
        return rowComponent;
    }
}
