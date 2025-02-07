package com.example.ui.core.browser;

import com.example.elements.impl.Group;
//import com.example.elements.impl.GroupModule;
//import com.example.elements.impl.Module;
import com.example.ui.core.AbstractPage;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.FindByName;
import com.example.ui.core.Page;
import com.example.ui.utils.WaitUtils;
import com.google.common.base.Function;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.beans.factory.config.BeanDefinition;
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
//import static org.junit.Assert.assertNotNull;
//import static org.springframework.test.util.AssertionErrors.assertNotNull;
//import static org.springframework.test.util.AssertionErrors.assertNotNull;



//@Component("customBrowser")
//@Service("customBrowser")
@Slf4j
public class Browser implements IBrowser{

    private static final String PAGES_PATH = "com.example.pages";

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


//    @Autowired
//    @Getter
//    @Setter
//    private WebDriverConfig driver;


//    @Getter
//    @Setter
//    private WebDriverConfig driver;

    @Getter
    @Setter
    private AbstractPage currentPage;


//    @Value("${cucumber.screenshot:false}")
//    private boolean isCuke;
//    @Value("${local.screenshot:false}")
//    private boolean isLocal;


    @Override
    public void goToUrl(String url) {
        this.driver.navigate().to(url);
    }

//    @Override
//    public WebElement findElement(final String xpath) {
//        return new FluentWait<>(driver)
//                .withTimeout(Duration.ofSeconds(defaultUiTimeout))
//                .pollingEvery(Duration.ofSeconds(step))
//                .ignoring(WebDriverException.class)
//                .withMessage("Waiting for finding WebElement by xpath " + xpath)
//                .until((Function<? super WebDriver, WebElement>) webDriver -> driver.findElement(By.xpath(xpath)));
//    }

    private WebDriverWait waitPageToLoad() {
        var wait = new WebDriverWait(driver, Duration.ofSeconds(defaultUiTimeout));
        wait.until(d -> executeJavaScript("return document.readyState").equals("complete"));
        wait.until(d -> {
            try {
                return ((Long) executeJavaScript("return jQuery.active") == 0);
            } catch (Exception e) {
                return true;
            }
        });
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//*[contains(translate(text(),'ABCDEFGHIJKLOMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'loading')]"))
        );
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//*[contains(translate(text(),'ABCDEFGHIJKLOMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'processing')]"))
        );
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("spinner")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loader")));
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
    public void scrollToElement(WebElement element) {
        executeJavaScript("arguments[0].scrollIntoView({block: 'center'});", element);
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

    @SuppressWarnings("RedundantCast")
    @Override
    public Object executeJavaScript(String script, WebElement... elements) {
        return ((JavascriptExecutor) this.driver).executeScript(script, (Object[]) elements);
    }

    @Override
    public void verifyAndSetPage(AbstractPage page) {
        var classAnnotation = AnnotationUtils.getAnnotation(page.getClass(), Page.class);
        assertNotNull("Page annotation", classAnnotation);
        waitUntilBrowserUrlContains(classAnnotation.url());
        assertThat("Page url doesn't match", getCurrentUrl(), containsString(classAnnotation.url()));
        setCurrentPage(page);
    }


    @Override
    public ElementContainer findElementContainer(String name) {
        var element = findElementByName(name);
        if (Objects.nonNull(element)) {
            return element;
        } else {
            element = findElementByText(name);
            if (Objects.nonNull(element)) {
                return element;
            }
        }
        fail("An element " + name + " wasn't found");
        return null;
    }

    public ElementContainer findElementByName(String name) {
        for (Field field : FieldUtils.getAllFieldsList(currentPage.getClass())) {
            field.setAccessible(true);
            if (ElementContainer.class.isAssignableFrom(field.getType())) {
                var annotationName = field.getDeclaredAnnotation(FindByName.class);
                if ((annotationName != null && annotationName.name().equalsIgnoreCase(name)) || field.getName().equalsIgnoreCase(name.replaceAll("\\s", ""))) {
                    try {
                        return (ElementContainer) field.get(currentPage);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
//            if (Module.class.isAssignableFrom(field.getType())) {
//                try {
//                    var obj = (Module) field.get(currentPage);
//                    var element = obj.getElementByName(name);
//                    if (Objects.nonNull(element)) {
//                        return element;
//                    } else {
//                        continue;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    continue;
//                }
//            }
//            if (GroupModule.class.isAssignableFrom(field.getType())) {
//                try {
//                    var obj = (GroupModule<?>) field.get(currentPage);
//                    var element = obj.getElementByName(name);
//                    if (Objects.nonNull(element)) {
//                        return element;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        }
        return null;
    }

    public ElementContainer findElementByText(String text) {
        var currentPage = getCurrentPage();
        for (Field field : FieldUtils.getAllFieldsList(currentPage.getClass())) {
            field.setAccessible(true);
            if (ElementContainer.class.isAssignableFrom(field.getType())) {
                try {
                    var obj = (ElementContainer) field.get(currentPage);
                    var element = obj.getByText(text);
                    if (Objects.nonNull(element)) {
                        return element;
                    } else {
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }

//            if (Module.class.isAssignableFrom(field.getType())) {
//                try {
//                    var obj = (Module) field.get(currentPage);
//                    var element = obj.getElementByText(text);
//                    if (Objects.nonNull(element)) {
//                        return element;
//                    } else {
//                        continue;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    continue;
//                }
//            }

//            if (Group.class.isAssignableFrom(field.getType())) {
//                try {
//                    var obj = (Group<?>) field.get(currentPage);
//                    var element = obj.getElementByText(text);
//                    if (Objects.nonNull(element)) {
//                        return element;
//                    } else {
//                        continue;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    continue;
//                }
//            }

//            if (GroupModule.class.isAssignableFrom(field.getType())) {
//                try {
//                    var obj = (GroupModule<?>) field.get(currentPage);
//                    var element = obj.getElementByText(text);
//                    if (Objects.nonNull(element)) {
//                        return element;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        }
        return null;
    }

//    public Module findModule(String name) {
//        var module = findModuleByName(name);
//        if (Objects.nonNull(module)) {
//            return module;
//        } else {
//            module = findModuleByText(name);
//            if (Objects.nonNull(module)) {
//                return module;
//            }
//        }
//        fail("The module " + name + " wasn't found");
//        return null;
//    }

//    @Override
//    public Module findModuleByText(String text) {
//        var currentPage = getCurrentPage();
//        for (Field field : FieldUtils.getAllFieldsList(currentPage.getClass())) {
//            field.setAccessible(true);
//            if (Module.class.isAssignableFrom(field.getType())) {
//                try {
//                    var module = (Module) field.get(currentPage);
//                    if (Objects.nonNull(module.getElementByText(text))) {
//                        return module;
//                    } else {
//                        continue;
//                    }
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (GroupModule.class.isAssignableFrom(field.getType())) {
//                try {
//                    var groupModule = (GroupModule<?>) field.get(currentPage);
//                    var module = groupModule.getByText(text);
//                    if (Objects.nonNull(module)) {
//                        return module;
//                    }
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return null;
//    }

//    @Override
//    public Module findModuleByName(String name) {
//        for (var field : FieldUtils.getAllFieldsList(currentPage.getClass())) {
//            field.setAccessible(true);
//            if (Module.class.isAssignableFrom(field.getType())) {
//                var annotationName = field.getDeclaredAnnotation(FindByName.class);
//                if ((annotationName != null && annotationName.name().equalsIgnoreCase(name))
//                        || field.getName().equalsIgnoreCase(name.replaceAll("\\s", ""))) {
//                    try {
//                        return (Module) field.get(currentPage);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    try {
//                        var obj = (Module) field.get(currentPage);
//                        var module = obj.getByName(name, Module.class);
//                        if (Objects.nonNull(module)) {
//                            return module;
//                        }
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        return null;
//    }
//    private void takeScreenShot(String filename, WebElement element) {
//        if (isCuke || isLocal) {
//            highlightElement(element);
//            screenShot(filename, false);
//            unHighlightElement(element);
//        }
//    }


}
