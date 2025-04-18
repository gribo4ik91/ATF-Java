package com.example.ui.core;

import com.example.ui.core.browser.Browser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;

import java.lang.reflect.Field;

/**
 * Кастомный декоратор для инициализации элементов, наследующих {@link ElementContainer};
 * Расширяет {@link DefaultFieldDecorator}, переопределяя логику создания прокси для кастомных компонентов;
 * Используется вместе с PageFactory для автоматического связывания полей страниц.
 *
 * <p>Этот класс позволяет использовать Page Object подход с кастомными компонентами,
 * такими как {@code Cell}, {@code Row}, {@code Button}, {@code CustomDropdown} и т.п.,
 * которые расширяют {@link ElementContainer} и требуют специфический конструктор.</p>
 */
public class MyFieldDecorator extends DefaultFieldDecorator {

    private final Browser browser;

    /**
     * Конструктор кастомного декоратора. Использует {@link Browser#getDriver()} для создания локаторов.
     *
     * @param browser текущий объект браузера, используется для передачи в компоненты
     */
    public MyFieldDecorator(Browser browser) {
        super(new DefaultElementLocatorFactory(browser.getDriver()));
        this.browser = browser;
    }

    /**
     * Переопределение стандартного поведения PageFactory для поддержки {@link ElementContainer}.
     * Если тип поля — пользовательский компонент, вызывается {@link #decorateContainer(...)}.
     *
     * @param loader загрузчик классов
     * @param field  поле, подлежащее инициализации
     * @return объект (прокси или созданный кастомный элемент)
     */
    @Override
    public Object decorate(ClassLoader loader, Field field) {
        Class<?> fieldType = field.getType();

        // Обрабатываем только поля, расширяющие ElementContainer
        if (ElementContainer.class.isAssignableFrom(fieldType)) {
            return decorateContainer(loader, field);
        }

        // Всё остальное обрабатывается стандартным способом
        return super.decorate(loader, field);
    }

    /**
     * Создаёт экземпляр кастомного элемента с использованием конструктора
     * (WebElement, String name, Browser).
     *
     * @param loader загрузчик классов
     * @param field  поле для инициализации
     * @return экземпляр кастомного компонента или null при ошибке
     */
    private Object decorateContainer(final ClassLoader loader, final Field field) {
        WebElement elementProxy = proxyForLocator(loader, factory.createLocator(field));

        try {
            return field.getType()
                    .getDeclaredConstructor(WebElement.class, String.class, Browser.class)
                    .newInstance(elementProxy, field.getName(), browser);
        } catch (Exception e) {
            e.printStackTrace(); // можно заменить на логгер
        }

        return null;
    }
}
