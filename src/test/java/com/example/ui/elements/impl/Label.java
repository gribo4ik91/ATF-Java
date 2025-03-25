package com.example.ui.elements.impl;

import com.example.ui.action.GetTextAction;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import org.openqa.selenium.WebElement;

public class Label extends ElementContainer {

    public Label(WebElement element, String name, Browser browser) {
        super(element, name, browser);
    }

    @Override
    public String getText() {
        return new GetTextAction(this, browser).execute();
    }



}
