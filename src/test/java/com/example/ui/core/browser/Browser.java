package com.example.ui.core.browser;

import com.example.ui.core.AbstractPage;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.Page;
import com.example.ui.utils.WaitUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
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

@Slf4j
public class Browser implements IBrowser {

    private static final String PAGES_PATH = "com.example.ui.pages";

    @Getter
    @Value("30")
    private long defaultUiTimeout;

    private long step = 1;

    //   @Value("${default.ui.page.timeout}")
    @Value("10")
    private long defaultUiPageTimeout;

    @Autowired
    @Getter
    @Setter
    private WebDriver driver;


    @Getter
    @Setter
    private AbstractPage currentPage;





    @Override
    public void goToUrl(String url) {
        this.driver.navigate().to(url);
    }


    private WebDriverWait waitPageToLoad() {
        var wait = new WebDriverWait(driver, Duration.ofSeconds(defaultUiTimeout));
        wait.until(d -> executeJavaScript("return document.readyState").equals("complete"));
        return wait;
    }

    @Override
    public void waitCurrentPageToLoad() {
        if (Objects.isNull(getCurrentPage()) || Objects.isNull(getCurrentPage().getWaitCondition())) {
            waitPageToLoad();
        }
    }

    public void waitPageToLoad(@Nullable ExpectedCondition<Boolean> condition) {
        if (Objects.isNull(condition)) waitPageToLoad();
        else {
            waitPageToLoad().until(condition);
        }
    }


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

    @Override
    public boolean waitUntilBrowserUrlContains(String part) {
        return WaitUtils.waitUntilCondition(() -> getCurrentUrl().contains(part), true, (int) defaultUiTimeout * 3);
    }

    @Override
    public String getCurrentUrl() {
        return (String) executeJavaScript("return document.URL");
    }

    @Override
    public Object executeJavaScript(String script) {
        return ((JavascriptExecutor) this.driver).executeScript(script);
    }


    @Override
    public void verifyAndSetPage(AbstractPage page) {
        var classAnnotation = AnnotationUtils.getAnnotation(page.getClass(), Page.class);
        assertNotNull("Page annotation", classAnnotation);
        waitUntilBrowserUrlContains(classAnnotation.url());
        assertThat("Page url doesn't match", getCurrentUrl(), containsString(classAnnotation.url()));
        setCurrentPage(page);
        log.info("[Info] Page url set to {}", getCurrentUrl());
    }

    @Override
    public void setPage(AbstractPage page) {
        var classAnnotation = AnnotationUtils.getAnnotation(page.getClass(), Page.class);
     //   assertNotNull("Page annotation", classAnnotation);
        waitUntilBrowserUrlContains(classAnnotation.url());
       // assertThat("Page url doesn't match", getCurrentUrl(), containsString(classAnnotation.url()));
        setCurrentPage(page);
        log.info("[Info] Page url set to {}", getCurrentUrl());
    }


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

    public ElementContainer findElementByName(String name) {
        for (Field field : FieldUtils.getAllFieldsList(currentPage.getClass())) {
            field.setAccessible(true); // Разрешаем доступ к приватному полю
            if (ElementContainer.class.isAssignableFrom(field.getType())) {
//                var annotationName = field.getDeclaredAnnotation(FindByName.class);
                if (
//                        (annotationName != null && annotationName.name().equalsIgnoreCase(name)) ||
                                field.getName().equalsIgnoreCase(name.replaceAll("\\s", ""))){
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
