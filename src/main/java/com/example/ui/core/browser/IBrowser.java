package com.example.ui.core.browser;

//import com.example.elements.impl.Module;
import com.example.ui.core.AbstractPage;
import com.example.ui.core.ElementContainer;
import org.openqa.selenium.WebElement;

public interface IBrowser {

    void goToUrl(String url);

//    WebElement findElement(String xpath);

    void waitCurrentPageToLoad();

    ElementContainer findElementContainer(String name);

    void scrollToElement(WebElement element);

    AbstractPage findPageByName(String name);

    boolean waitUntilBrowserUrlContains(String part);

    String getCurrentUrl();

    Object executeJavaScript(String script);

//    Module findModuleByText(String text);
//
//    Module findModuleByName(String text);

    Object executeJavaScript(String script, WebElement... elements);

    void verifyAndSetPage(AbstractPage page);
}
