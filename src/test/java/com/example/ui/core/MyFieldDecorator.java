package com.example.ui.core;

import com.example.ui.core.browser.Browser;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;

public class MyFieldDecorator extends DefaultFieldDecorator {


    private final Browser browser;


    MyFieldDecorator(Browser browser) {
        super(new DefaultElementLocatorFactory(browser.getDriver()));
        this.browser = browser;
    }


    @Override
    public Object decorate(ClassLoader loader, Field field) {
        var fieldType = field.getType();
        if (ElementContainer.class.isAssignableFrom(fieldType)) {
            return decorateContainer(loader, field);
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
