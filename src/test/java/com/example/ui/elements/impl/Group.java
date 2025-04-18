package com.example.ui.elements.impl;

import com.example.ui.core.ElementContainer;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Контейнер для группы UI-элементов (`ElementContainer`);
 * Предоставляет доступ к списку элементов, извлечение по индексу и получение текстов;
 * Универсальный и типизированный — может работать с любыми наследниками `ElementContainer`.
 *
 * <p>Класс `Group<T>` используется для представления наборов однотипных элементов:
 * строки таблицы, ячейки, блоки, карточки, списки и т.д.
 * Обеспечивает простое API для работы с этими элементами.</p>
 *
 * @param <T> тип элементов, наследующих `ElementContainer`
 */
@Slf4j
@NoArgsConstructor
public class Group<T extends ElementContainer> {

    public static final int SUBLIST_SIZE = 20; // Примерная константа на будущее (например, пагинация)

    private List<T> elements;

    /**
     * Конструктор группы элементов.
     *
     * @param elements список элементов, обёрнутых в тип T (например, Cell, Row, Item и т.д.)
     */
    public Group(List<T> elements) {
        this.elements = elements;
    }

    /**
     * Получает элемент по индексу.
     *
     * @param index позиция элемента (0-based)
     * @return элемент типа T
     */
    public T get(int index) {
        return elements.get(index);
    }

    /**
     * Возвращает весь список элементов.
     *
     * @return список элементов типа T
     */
    public List<T> getAll() {
        return elements;
    }

    /**
     * Возвращает список текстов из всех элементов группы.
     * Часто используется для получения названий колонок или значений в списке.
     *
     * @return список строк, по одной на каждый элемент
     */
    public List<String> getTextFromGroupComponent() {
        return elements.stream()
                .map(e -> e.getWrappedElement().getText().trim())
                .collect(toList());
    }
}
