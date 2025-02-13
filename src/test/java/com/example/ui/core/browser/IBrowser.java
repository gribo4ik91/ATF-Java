package com.example.ui.core.browser;

//import com.example.elements.impl.Module;
import com.example.ui.core.AbstractPage;
import com.example.ui.core.ElementContainer;
import org.openqa.selenium.WebElement;

public interface IBrowser {

    void goToUrl(String url);

    void waitCurrentPageToLoad();

    ElementContainer findElementContainer(String name);

    AbstractPage findPageByName(String name);

    boolean waitUntilBrowserUrlContains(String part);

    String getCurrentUrl();

    Object executeJavaScript(String script);


    void verifyAndSetPage(AbstractPage page);
}
