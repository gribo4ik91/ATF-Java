package com.example.action;

import com.example.ui.core.AbstractAction;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClearAndTypeAction extends AbstractAction {

    private final String text;

    public ClearAndTypeAction(ElementContainer element, Browser browser, String text) {
        super(element, browser);
        this.text = text;
    }

    public void execute() {
        super.execute(() -> {
            element.getWrappedElement().clear();
            element.getWrappedElement().sendKeys(text);
        });
        log.info("[ACTION] Clear and type text " + text + " into the element: " + element.getName());
    }
}
