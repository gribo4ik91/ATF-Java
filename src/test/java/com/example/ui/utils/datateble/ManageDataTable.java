package com.example.ui.utils.datateble;

import com.example.ATFAssert;
import com.example.ui.elements.impl.table.Table;
import com.example.ui.utils.datateble.paramreplacers.PlaceholderReplacer;
import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.DataTableTypeRegistry;
import io.cucumber.datatable.DataTableTypeRegistryTableConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;

/**
 * Утилитный класс для работы с Cucumber DataTable и UI-таблицами;
 * Позволяет заменять плейсхолдеры, сравнивать строки, проверять наличие/отсутствие записей.
 *
 * <p>Класс `ManageDataTable` используется в степах Cucumber для проверки данных таблиц на UI:
 * - Заменяет [PLACEHOLDER] на значения из GlobalMap;
 * - Преобразует DataTable с заменой данных;
 * - Проверяет, включены или исключены записи в таблице на UI.</p>
 */
public class ManageDataTable {

    @Autowired
    private PlaceholderReplacer placeholderReplacer;

    private static final DataTableTypeRegistry registry = new DataTableTypeRegistry(Locale.ENGLISH);
    private static final DataTable.TableConverter tableConverter = new DataTableTypeRegistryTableConverter(registry);

    /**
     * Заменяет плейсхолдеры в строке с использованием {@link PlaceholderReplacer}.
     *
     * @param data строка, возможно содержащая плейсхолдер (например, [EMAIL])
     * @return строка с подставленным значением или пустая строка
     */
    public String replaceData(final String data) {
        return StringUtils.isNotEmpty(data) ? placeholderReplacer.replaceAll(data) : StringUtils.EMPTY;
    }

    /**
     * Применяет `replaceData()` ко всем ячейкам в таблице и возвращает новую таблицу.
     *
     * @param dataTable исходная DataTable из .feature-файла
     * @return новая DataTable с заменёнными значениями
     */
    public DataTable overrideData(final DataTable dataTable) {
        List<List<String>> results = new ArrayList<>();
        for (List<String> row : dataTable.cells()) {
            List<String> newRow = row.stream()
                    .map(this::replaceData)
                    .collect(toList());
            results.add(newRow);
        }
        return DataTable.create(results, tableConverter);
    }

    /**
     * Проверяет, что данные из DataTable присутствуют в UI-таблице.
     *
     * @param table     объект таблицы (UI)
     * @param dataTable ожидаемые строки
     */
    public void includesRecordsInTable(Table table, DataTable dataTable) {
        List<List<String>> results = new ArrayList<>();

        // Заголовки
        List<String> columns = dataTable.row(0);
        results.add(columns);

        // Отфильтрованные строки по критериям
        results.addAll(table.filterRowsData(dataTable));

        // Если количество строк недостаточно — сравниваем всё
        if (dataTable.asLists().size() > results.size()) {
            List<List<String>> newResults = new ArrayList<>();
            newResults.add(columns);
            newResults.addAll(table.getRowsDataByColumns(columns));

            // Проверка с unorderedDiff
            DataTable actualDataTable = DataTable.create(newResults);
            dataTable.unorderedDiff(actualDataTable);
        }
    }

    /**
     * Проверяет, что строки из DataTable отсутствуют в таблице UI.
     *
     * @param table     объект таблицы
     * @param dataTable ожидаемые строки, которых не должно быть
     */
    public void notIncludesRecordsInTable(Table table, DataTable dataTable) {
        List<List<String>> filteredRows = table.filterRowsData(dataTable);

        ATFAssert.assertThat(
                String.format("The rows should not be displayed in the table.\n%s", DataTable.create(filteredRows)),
                filteredRows.size(),
                is(0),
                false
        );
    }
}
