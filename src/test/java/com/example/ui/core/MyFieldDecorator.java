package com.example.ui.core;


import com.example.ui.core.browser.Browser;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.lang.reflect.Field;

public class MyFieldDecorator extends DefaultFieldDecorator {


    private final Browser browser;

    private static final int ELEMENT_WAIT_TIMEOUT = 5;

    MyFieldDecorator(Browser browser) {
        super(new MyElementLocatorFactory(browser.getDriver(), ELEMENT_WAIT_TIMEOUT));
        this.browser = browser;
    }


    @Override
    public Object decorate(ClassLoader loader, Field field) {
        var fieldType = field.getType();
        if (ElementContainer.class.isAssignableFrom(fieldType)) {
            return decorateContainer(loader, field);
        }
        if (Select.class.isAssignableFrom(fieldType)) {
            return new Select(proxyForLocator(loader, factory.createLocator(field)));
        }

        return super.decorate(loader, field);
    }


    private Object decorateContainer(final ClassLoader loader, final Field field) {
        var element = proxyForLocator(loader, factory.createLocator(field));
        try {
            return field.getType()
                    .getDeclaredConstructor(WebElement.class, String.class, Browser.class)
                    .newInstance(element, field.getName(), browser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
