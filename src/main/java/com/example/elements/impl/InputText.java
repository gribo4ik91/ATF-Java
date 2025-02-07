package com.example.elements.impl;

import com.example.action.*;
import com.example.elements.IInputText;

import com.example.ui.core.ElementContainer;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.example.ui.core.browser.Browser;

public class InputText extends ElementContainer implements IInputText {

    public InputText(WebElement element, String name, Browser browser) {
        super(element, name, browser);
    }

    @Override
    public void clearAndType(String text) {
        new ClearAndTypeAction(this, browser, text).execute();
    }

    @Override
    public void doAction(String value) {
        clearAndType(value);
    }

    @Override
    public String getText() {
        return new GetValueAction(this, browser).execute();
    }


}