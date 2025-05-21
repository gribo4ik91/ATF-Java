package com.example.ui.core.browser;

import com.example.config.DriverManager;
import com.example.ui.core.AbstractPage;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.Page;
import com.example.ui.utils.WaitUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Objects;

import static com.example.ATFAssert.assertNotNull;
import static com.example.ATFAssert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * Управляет WebDriver'ом, страницами и элементами в UI-тестах;
 * Автоматически инициализирует драйвер, находит страницы по аннотации @Page;
 * Предоставляет методы для навигации, ожидания, поиска и валидации URL/элементов.
 *
 * <p>Класс `Browser` реализует интерфейс `IBrowser` и служит основным инструментом взаимодействия
 * с браузером в автоматизированных UI-тестах. Он управляет жизненным циклом WebDriver,
 * инициализацией страниц, выполнением JavaScript, проверкой URL и динамическим поиском элементов.</p>
 */
//@Component
//@Scope("scenario") // Создание нового экземпляра Browser для каждого сценария (Cucumber)
@Slf4j
public class Browser implements IBrowser {

    private static final String PAGES_PATH = "com.example.ui.pages";

    @Getter
    @Value("30")
    private long defaultUiTimeout;

    @Value("10")
    private long defaultUiPageTimeout;

    private long step = 1;

    @Getter
    private WebDriver driver;

    private final ObjectProvider<WebDriver> webDriverProvider;

    @Getter
    @Setter
    private AbstractPage currentPage;

    @Autowired
    public Browser(ObjectProvider<WebDriver> webDriverProvider) {
        this.webDriverProvider = webDriverProvider;
        this.driver = webDriverProvider.getObject();
        DriverManager.setDriver(this.driver);
    }

    /**
     * Останавливает WebDriver и очищает его из DriverManager.
     */
    public void stop() {
        if (driver != null) {
            driver.quit();
            DriverManager.removeDriver();
            log.info("WebDriver остановлен и удалён из DriverManager");
        }
    }

    /**
     * Возвращает текущий активный WebDriver. Переинициализирует при необходимости.
     */
    public WebDriver getDriver() {
        if (this.driver == null || ((RemoteWebDriver) this.driver).getSessionId() == null) {
            log.warn("WebDriver is null or closed — reinitializing");
            driver = webDriverProvider.getObject();
            DriverManager.setDriver(this.driver);
        }
        return this.driver;
    }


    /**
     * Ищет страницу по её имени через аннотацию @Page.
     */
    @Override
    public AbstractPage findPageByName(String name) {
        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Page.class));
        for (BeanDefinition bean : scanner.findCandidateComponents(PAGES_PATH)) {
            try {
                Class<?> clazz = Class.forName(bean.getBeanClassName());
                var classAnnotation = AnnotationUtils.getAnnotation(clazz, Page.class);
                if (name.equalsIgnoreCase(Objects.requireNonNull(classAnnotation).name())) {
                    return (AbstractPage) clazz.getDeclaredConstructor(Browser.class).newInstance(this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Выполняет переход по заданному URL в открытом браузере.
     * Если WebDriver не инициализирован или сессия завершена — выбрасывает исключение.
     *
     * @param url абсолютный или относительный URL, по которому нужно перейти
     */
    @Override
    public void goToUrl(String url) {
        if (driver == null || ((RemoteWebDriver) driver).getSessionId() == null) {
            throw new IllegalStateException("WebDriver is null or session is closed");
        }
        driver.navigate().to(url);
    }

    /**
     * Явно ожидает полной загрузки страницы, проверяя `document.readyState === 'complete'`.
     * Используется по умолчанию, если не указано кастомное условие ожидания.
     *
     * @return WebDriverWait, с которым была выполнена проверка
     */
    private WebDriverWait waitPageToLoad() {
        var wait = new WebDriverWait(driver, Duration.ofSeconds(defaultUiTimeout));
        wait.until(d -> executeJavaScript("return document.readyState").equals("complete"));
        return wait;
    }


    /**
     * Ожидает загрузку текущей страницы на основе условия, определённого в аннотации @Page.
     * Если страница или условие ожидания не заданы — используется дефолтная проверка document.readyState.
     */
    @Override
    public void waitCurrentPageToLoad() {
        if (Objects.isNull(getCurrentPage()) || Objects.isNull(getCurrentPage().getWaitCondition())) {
            waitPageToLoad(); // стандартное ожидание полной загрузки
        }
    }

    /**
     * Ожидает выполнения кастомного условия загрузки страницы, если оно передано.
     * Если условие null — используется стандартное ожидание полной загрузки.
     *
     * @param condition условие ожидания (например, наличие элемента, определённый URL и т.п.)
     */
    public void waitPageToLoad(@Nullable ExpectedCondition<Boolean> condition) {
        if (Objects.isNull(condition)) {
            waitPageToLoad();
        } else {
            waitPageToLoad().until(condition);
        }
    }

    /**
     * Явно ожидает, пока URL браузера будет содержать указанный подстроку.
     * Обычно используется при навигации на страницы и проверки перехода.
     *
     * @param part часть URL, которую ожидаем
     * @return true, если ожидание выполнено успешно
     */
    @Override
    public boolean waitUntilBrowserUrlContains(String part) {
        return WaitUtils.waitUntilCondition(
                () -> getCurrentUrl().contains(part),
                true,
                (int) defaultUiTimeout * 3
        );
    }

    /**
     * Получает текущий URL страницы через JavaScript.
     *
     * @return текущий URL как строка
     */
    @Override
    public String getCurrentUrl() {
        return (String) executeJavaScript("return document.URL");
    }

    /**
     * Выполняет JavaScript-команду в текущем окне браузера.
     *
     * @param script строка с JS-кодом (например, "return document.title")
     * @return результат выполнения скрипта
     */
    @Override
    public Object executeJavaScript(String script) {
        return ((JavascriptExecutor) this.driver).executeScript(script);
    }


    /**
     * Проверяет URL страницы и устанавливает её как текущую.
     */
    @Override
    public void verifyAndSetPage(AbstractPage page) {
        var classAnnotation = AnnotationUtils.getAnnotation(page.getClass(), Page.class);
        assertNotNull("Page annotation", classAnnotation);
        waitUntilBrowserUrlContains(classAnnotation.url());
        assertThat("Page url doesn't match", getCurrentUrl(), containsString(classAnnotation.url()));
        setCurrentPage(page);
        log.info("[Info] Page url set to {}", getCurrentUrl());
    }

    /**
     * Устанавливает текущую страницу без валидации URL.
     */
    @Override
    public void setPage(AbstractPage page) {
        var classAnnotation = AnnotationUtils.getAnnotation(page.getClass(), Page.class);
        waitUntilBrowserUrlContains(classAnnotation.url());
        setCurrentPage(page);
        log.info("[Info] Page url set to {}", getCurrentUrl());
    }

    /**
     * Ищет элемент по имени и оборачивает его в ElementContainer.
     */
    @Override
    public ElementContainer findElementContainer(String name) {
        var element = findElementByName(name);
        if (Objects.nonNull(element)) {
            log.info("[Info] The element was found successfully with name {}", name);
            return element;
        }
        fail("An element " + name + " wasn't found");
        return null;
    }

    /**
     * Ищет элемент по имени, сравнивая его с именами полей на странице.
     */
    public ElementContainer findElementByName(String name) {
        for (Field field : FieldUtils.getAllFieldsList(currentPage.getClass())) {
            field.setAccessible(true);
            if (ElementContainer.class.isAssignableFrom(field.getType())) {
                if (field.getName().equalsIgnoreCase(name.replaceAll("\\s", ""))) {
                    try {
                        return (ElementContainer) field.get(currentPage);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
