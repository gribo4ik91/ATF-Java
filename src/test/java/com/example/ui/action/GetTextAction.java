package com.example.ui.action;

import com.example.ui.core.AbstractAction;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import lombok.extern.slf4j.Slf4j;

/**
 * Получает текст из указанного элемента;
 * Наследуется от AbstractAction и использует обёртку выполнения;
 * Возвращает текст и логирует его.
 *
 * <p>Класс `GetTextAction` используется для получения текстового содержимого
 * из элементов DOM — например, из заголовков, сообщений, кнопок.
 * Полезен для assert-проверок значений на UI.
 * Вся логика выполняется через `super.execute(...)`, что добавляет единообразную обработку ошибок, логирование и ожидания.</p>
 */
@Slf4j
public class GetTextAction extends AbstractAction {

    /**
     * Конструктор действия для получения текста.
     *
     * @param element элемент, из которого нужно извлечь текст
     * @param browser экземпляр браузера, необходимый для работы с элементом
     */
    public GetTextAction(ElementContainer element, Browser browser) {
        super(element, browser);
    }

    /**
     * Извлекает и возвращает текст из web-элемента.
     *
     * @return текстовое содержимое элемента
     */
    public String execute() {
        return super.execute(() -> {
            var text = this.element.getWrappedElement().getText(); // Получение текста из элемента
            log.info("[ACTION] Got text '{}' from element: {}", text, element.getName());
            return text;
        });
    }
}
