package com.example.ui.action;

import com.example.ui.core.AbstractAction;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import lombok.extern.slf4j.Slf4j;

/**
 * Кликает по элементу на странице;
 * Наследуется от AbstractAction и использует встроенную обработку действия;
 * Логирует факт клика по элементу через Slf4j.
 *
 * <p>Класс `ClickAction` реализует простое пользовательское действие — клик по элементу интерфейса.
 * Оно может применяться к кнопкам, ссылкам и другим кликабельным компонентам.
 * Используется механизм `super.execute(...)` для выполнения с обработкой и логированием.</p>
 */
@Slf4j
public class ClickAction extends AbstractAction {

    /**
     * Конструктор действия клика по элементу.
     *
     * @param element элемент, по которому нужно кликнуть
     * @param browser экземпляр браузера, в котором выполняется действие
     */
    public ClickAction(ElementContainer element, Browser browser) {
        super(element, browser);
    }

    /**
     * Выполняет клик по заданному элементу.
     * Метод обёрнут в стандартную обработку `super.execute(...)` — для надёжности и единообразия.
     */
    public void execute() {
        super.execute(() -> element.getWrappedElement().click());
        log.info("[ACTION] Clicked content of element: {}", element.getName());
    }
}
