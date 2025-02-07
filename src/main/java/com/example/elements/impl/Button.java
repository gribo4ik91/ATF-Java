package com.example.elements.impl;


import com.example.action.ClickAction;
import com.example.elements.IButton;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import org.openqa.selenium.WebElement;

public class Button extends ElementContainer implements IButton {

    public Button(WebElement element, String name, Browser browser) {
        super(element, name, browser);
    }

    @Override
    public void click() {
        new ClickAction(this, browser).execute();
    }

    @Override
    public void doAction(String value) {
        click();
    }


}