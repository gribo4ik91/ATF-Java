package com.example.ui.elements.impl;

import com.example.ui.action.ClearAndTypeAction;
import com.example.ui.core.ElementContainer;
import org.openqa.selenium.WebElement;
import com.example.ui.core.browser.Browser;

public class InputText extends ElementContainer  {

    public InputText(WebElement element, String name, Browser browser) {
        super(element, name, browser);
    }

    public void clearAndType(String text) {
        new ClearAndTypeAction(this, browser, text).execute();
    }

    @Override
    public void doAction(String value) {
        clearAndType(value);
    }


}