package com.example.ui.elements.impl.table;

import com.example.ui.elements.impl.Group;
import io.cucumber.datatable.DataTable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Утилитный класс для работы с данными таблицы:
 * - Чтение строк и колонок;
 * - Фильтрация по колонкам и данным;
 * - Преобразование строк в списки значений.
 *
 * <p>Класс `TableUtils` предоставляет методы для чтения и фильтрации строк таблицы.
 * Работает с объектами `Group<Cell>`, `Group<Body>`, `Map<String, Cell>`, `DataTable`.
 * Поддерживает как плоский, так и вложенный (подячейки) формат таблицы.</p>
 */
public class TableUtils {

    /**
     * Извлекает строки из тела таблицы, с возможностью фильтрации по нужным колонкам.
     *
     * @param columnGroup  группа заголовков (колонок)
     * @param bodyGroup    группа тел таблицы
     * @param filterColumns список колонок, по которым нужно фильтровать данные (может быть пустым)
     * @return список строк таблицы в виде Map<"column", Cell>
     */
    public static List<Map<String, Cell>> getRows(Group<Cell> columnGroup, Group<Body> bodyGroup, List<String> filterColumns) {
        List<String> allColumns = columnGroup.getTextFromGroupComponent();

        return bodyGroup.getAll().stream()
                .flatMap(body -> body.getRowsComponent(allColumns).stream())
                .map(row -> filterColumns.isEmpty() ? row : filterRowByColumns(row, filterColumns))
                .collect(Collectors.toList());
    }

    /**
     * Получает текстовые данные из строк таблицы, исключая полностью пустые строки.
     *
     * @param columnGroup  группа заголовков
     * @param bodyGroup    группа тел таблицы
     * @param columns      список колонок, которые нужно извлечь
     * @return данные строк таблицы в виде списка списков строк
     */
    public static List<List<String>> getRowsData(Group<Cell> columnGroup, Group<Body> bodyGroup, List<String> columns) {
        return getRows(columnGroup, bodyGroup, columns).stream()
                .map(TableUtils::extractRowData)
                .filter(data -> data.stream().anyMatch(s -> !s.isEmpty()))
                .collect(Collectors.toList());
    }

    /**
     * Фильтрует строки таблицы на основе значений из DataTable.
     *
     * @param columnGroup  группа заголовков
     * @param bodyGroup    группа тел таблицы
     * @param dataTable    DataTable из Cucumber .feature файла
     * @return список строк, соответствующих критериям
     */
    public static List<Map<String, Cell>> filterRows(Group<Cell> columnGroup, Group<Body> bodyGroup, DataTable dataTable) {
        return getRows(columnGroup, bodyGroup, Collections.emptyList()).stream()
                .filter(row -> dataTable.asMaps().stream().anyMatch(criteria -> matchesCriteria(row, criteria)))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает текст ячейки. Если ячейка отсутствует — пустая строка.
     */
    private static String getText(Cell cell) {
        return cell != null ? cell.getText() : "";
    }

    /**
     * Возвращает список текстов из под-ячейки (если она есть).
     * Если подячейки отсутствуют — возвращается основной текст.
     */
    private static List<String> getSubCellTexts(Cell cell) {
        if (cell == null) return Collections.emptyList();
        return cell.getSubCellTexts().isEmpty()
                ? List.of(cell.getText())
                : cell.getSubCellTexts();
    }

    /**
     * Преобразует строку таблицы в список текстов из ячеек.
     *
     * @param row строка таблицы
     * @return список значений
     */
    private static List<String> extractRowData(Map<String, Cell> row) {
        return row.values().stream()
                .map(TableUtils::getText)
                .collect(Collectors.toList());
    }

    /**
     * Фильтрует строку, оставляя только ячейки по указанным колонкам.
     *
     * @param row     строка таблицы
     * @param columns список колонок
     * @return отфильтрованная строка
     */
    private static Map<String, Cell> filterRowByColumns(Map<String, Cell> row, List<String> columns) {
        return row.entrySet().stream()
                .filter(entry -> columns.contains(entry.getKey().toLowerCase()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Проверяет, соответствует ли строка условиям фильтра (по колонкам и значениям).
     *
     * @param row      строка таблицы
     * @param criteria условия фильтрации: Map<"column", "value">
     * @return true, если строка содержит все значения из фильтра
     */
    private static boolean matchesCriteria(Map<String, Cell> row, Map<String, String> criteria) {
        return criteria.entrySet().stream()
                .anyMatch(entry -> getSubCellTexts(row.get(entry.getKey().toLowerCase()))
                        .contains(entry.getValue()));
    }
}
