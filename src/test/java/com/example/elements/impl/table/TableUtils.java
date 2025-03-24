package com.example.elements.impl.table;

import java.util.*;
import java.util.stream.Collectors;

import com.example.elements.impl.Group;
import io.cucumber.datatable.DataTable;


public class TableUtils {

    /**
     * Извлекает строки из тела таблицы, опционально фильтруя по нужным колонкам.
     */
    public static List<Map<String, Cell>> getRows(Group<Cell> columnGroup, Group<Body> bodyGroup, List<String> filterColumns) {
        List<String> allColumns = columnGroup.getTextFromGroupComponent();

        return bodyGroup.getAll().stream()
                .flatMap(body -> body.getRowsComponent(allColumns).stream())
                .map(row -> filterColumns.isEmpty() ? row : filterRowByColumns(row, filterColumns))
                .collect(Collectors.toList());
    }

    /**
     * Получает текстовые данные из строк таблицы, убирая пустые значения.
     */
    public static List<List<String>> getRowsData(Group<Cell> columnGroup, Group<Body> bodyGroup, List<String> columns) {
        return getRows(columnGroup, bodyGroup, columns).stream()
                .map(TableUtils::extractRowData)
                .filter(data -> data.stream().anyMatch(s -> !s.isEmpty()))
                .collect(Collectors.toList());
    }

    /**
     * Фильтрует строки на основе условий из DataTable.
     */
    public static List<Map<String, Cell>> filterRows(Group<Cell> columnGroup, Group<Body> bodyGroup, DataTable dataTable) {
        return getRows(columnGroup, bodyGroup, Collections.emptyList()).stream()
                .filter(row -> dataTable.asMaps().stream().anyMatch(criteria -> matchesCriteria(row, criteria)))
                .collect(Collectors.toList());
    }

    /**
     * Получает текст из ячейки, если её нет — возвращает пустую строку.
     */
    private static String getText(Cell cell) {
        return cell != null ? cell.getText() : "";
    }

    /**
     * Получает список значений из ячейки (если есть подячейки, иначе — основной текст).
     */
    private static List<String> getSubCellTexts(Cell cell) {
        if (cell == null) return Collections.emptyList();
        return cell.getSubCellTexts().isEmpty() ? List.of(cell.getText()) : cell.getSubCellTexts();
    }

    /**
     * Преобразует строку (Map<String, Cell>) в список текстов.
     */
    private static List<String> extractRowData(Map<String, Cell> row) {
        return row.values().stream()
                .map(TableUtils::getText)
                .collect(Collectors.toList());
    }

    /**
     * Фильтрует строку по указанным колонкам.
     */
    private static Map<String, Cell> filterRowByColumns(Map<String, Cell> row, List<String> columns) {
        return row.entrySet().stream()
                .filter(entry -> columns.contains(entry.getKey().toLowerCase()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Проверяет соответствие строки критериям.
     */
    private static boolean matchesCriteria(Map<String, Cell> row, Map<String, String> criteria) {
        return criteria.entrySet().stream()
                .anyMatch(entry -> getSubCellTexts(row.get(entry.getKey().toLowerCase())).contains(entry.getValue()));
    }
}
