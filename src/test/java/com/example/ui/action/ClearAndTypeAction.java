package com.example.ui.action;

import com.example.ui.core.AbstractAction;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import lombok.extern.slf4j.Slf4j;

/**
 * Очищает содержимое поля ввода и вводит указанный текст;
 * Используется в UI-тестах для симуляции ввода пользователем;
 * Расширяет AbstractAction и логирует шаги через Slf4j.
 *
 * <p>Класс `ClearAndTypeAction` реализует действие по очистке input-поля и вводу текста.
 * Это стандартное действие при заполнении форм, особенно в тестах с WebDriver (Selenium).
 * Наследуется от `AbstractAction`, чтобы использовать стандартный механизм выполнения с логированием и обработкой ошибок.</p>
 */
@Slf4j
public class ClearAndTypeAction extends AbstractAction {

    // Текст, который нужно ввести в элемент
    private final String text;

    /**
     * Конструктор действия очистки и ввода текста.
     *
     * @param element  элемент, в который нужно ввести текст
     * @param browser  текущий экземпляр браузера
     * @param text     текст для ввода
     */
    public ClearAndTypeAction(ElementContainer element, Browser browser, String text) {
        super(element, browser); // Инициализация базового действия
        this.text = text;
    }

    /**
     * Выполняет очистку поля и ввод текста.
     * Использует обёртку `super.execute(...)` из `AbstractAction`,
     * которая может содержать дополнительные механизмы (ожидания, логгирование, обработку исключений).
     */
    public void execute() {
        super.execute(() -> {
            element.getWrappedElement().clear();       // Очистка поля
            element.getWrappedElement().sendKeys(text); // Ввод текста
        });

        // Логирование действия
        log.info("[ACTION] Clear and type text '{}' into the element: {}", text, element.getName());
    }
}
