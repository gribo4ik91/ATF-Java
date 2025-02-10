package com.example.ui.core;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;

public class MyElementLocatorFactory  implements ElementLocatorFactory {

    private final SearchContext searchContext;

    private final int timeOutInSeconds;

    MyElementLocatorFactory(final SearchContext searchContext, final int timeOutInSeconds) {
        this.searchContext = searchContext;
        this.timeOutInSeconds = timeOutInSeconds;
    }

    @Override
    public ElementLocator createLocator(final Field field) {
        return new MyElementLocator(searchContext, field, timeOutInSeconds);
    }
}
