package com.example.ui.elements.impl.table;

import com.example.ui.action.ClickAction;
import com.example.ui.action.GetTextAction;
import com.example.ui.elements.impl.Group;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Представляет собой ячейку таблицы на UI;
 * Наследуется от ElementContainer и содержит поведение: клик, получение текста, доступ к вложенным ячейкам;
 * Поддерживает ленивую инициализацию вложенных ячеек (`subCellGroupComponent`).
 *
 * <p>Класс `Cell` оборачивает WebElement, представляющий одну ячейку HTML-таблицы,
 * и предоставляет удобные методы для взаимодействия с ней, включая:
 * - клик по ячейке;
 * - получение текста (с фильтрацией и форматированием);
 * - доступ к под-ячейкам, если ячейка содержит вложенные элементы (например, таблицу внутри).</p>
 */
public class Cell extends ElementContainer {

    // Ленивый список под-ячееек внутри этой ячейки (если есть вложенные <td> элементы)
    private final Lazy<Group<Cell>> subCellGroupComponent = new Lazy<>(() -> new Group<>(
            getWrappedElement()
                    .findElements(By.xpath("./td[not(@hidden='true')]"))
                    .stream()
                    .map(el -> new Cell(el, "", browser)) // создаём Cell для каждой найденной под-ячейки
                    .collect(Collectors.toList())
    ));

    /**
     * Конструктор ячейки таблицы.
     *
     * @param element  WebElement, соответствующий ячейке (<td>)
     * @param name     логическое имя элемента (для логирования и отчётов)
     * @param browser  объект браузера, через который выполняются действия
     */
    public Cell(final WebElement element, final String name, final Browser browser) {
        super(element, name, browser);
    }

    /**
     * Кликает по ячейке.
     */
    public void click() {
        new ClickAction(this, browser).execute();
    }

    /**
     * Получает текстовое содержимое ячейки с фильтрацией переносов строк и пробелов.
     *
     * @return строка текста из ячейки
     */
    @Override
    public String getText() {
        return new GetTextAction(this, browser).execute()
                .replace("\n", " ")
                .strip();
    }

    /**
     * Получает список текстов из под-ячееек (если внутри текущей ячейки вложенная таблица или строки <td>).
     *
     * @return список строк — текст каждой вложенной ячейки
     */
    public List<String> getSubCellTexts() {
        return subCellGroupComponent.get().getAll()
                .stream()
                .map(Cell::getText)
                .collect(Collectors.toList());
    }
}
