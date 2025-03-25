package com.example.ui.elements.impl;


import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import org.openqa.selenium.WebElement;

public class Span extends ElementContainer  {

        public Span(WebElement element, String name, Browser browser) {
            super(element, name, browser);
        }

        public String getState() {
            return super.getAttributeValue("aria-checked").equalsIgnoreCase("true") ? getWrappedElement().getText() : "";
        }
    }

