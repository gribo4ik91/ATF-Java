package com.example.ui.core;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;

public class ElementHandler extends LocatingElementHandler {

    public ElementHandler(final ElementLocator locator) {
        super(locator);
    }

    @Override
    public Object invoke(final Object object, final Method method, final Object[] objects) throws Throwable {
        try {
            return super.invoke(object, method, objects);
        } catch (InvocationTargetException | NullPointerException e) {
            if ("isDisplayed".equals(method.getName())) {
                return false;
            }
            throw e.getCause();
        }
    }

}
