package com.example.ui.elements.impl;


import com.example.ui.action.ClickAction;

import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import org.openqa.selenium.WebElement;

public class Button extends ElementContainer {

    public Button(WebElement element, String name, Browser browser) {
        super(element, name, browser);
    }


    public void click() {
        new ClickAction(this, browser).execute();
    }

    @Override
    public void doAction(String value) {
        click();
    }


}