package com.example.ui.core;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;

import java.lang.reflect.Field;
import java.time.Clock;

public class MyElementLocator extends DefaultElementLocator {



    private final int timeOutInSeconds;

    private final Clock clock;

    MyElementLocator(SearchContext searchContext, Field field, int timeOutInSeconds) {
        super(searchContext, new Annotations(field));
        this.timeOutInSeconds = timeOutInSeconds;
        this.clock = Clock.systemDefaultZone();
    }
}
