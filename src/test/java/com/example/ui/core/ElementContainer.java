package com.example.ui.core;

import com.example.elements.IElement;
import com.example.ui.core.browser.Browser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;

import static org.apache.commons.lang3.StringUtils.isNotBlank;



import lombok.Getter;

public class ElementContainer implements WrapsElement, IElement {

    private final WebElement element;

    @Getter
    private final String name;

    protected Browser browser;

    public ElementContainer(WebElement element, String name, Browser browser) {
        this.element = element;
        this.name = name;
        this.browser = browser;
    }

    @Override
    public WebElement getWrappedElement() {
        return element;
    }

    @Override
    public void doAction(String value) {
    }

    @Override
    public String getText() {
        return getWrappedElement().getText();
    }

    @Override
    public String getAttributeValue(String attribute) {
        return getWrappedElement().getAttribute(attribute);
    }

    public String getValue() {
        return getWrappedElement().getAttribute("value");
    }

    @Override
    public String toString() {
        return isNotBlank(name) ? name : getClass().getSimpleName();
    }

//    public ElementContainer getByText(String text) {
//
//        var elText = getWrappedElement().getText();
//        if (text.equalsIgnoreCase(elText) || elText.contains(text)) {
//            return this;
//        }
//
//        return null;
//    }
//
//    public String getState() {
//        return StringUtils.EMPTY;
//    }

}
