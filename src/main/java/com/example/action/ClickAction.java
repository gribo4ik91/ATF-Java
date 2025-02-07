package com.example.action;


import com.example.ui.core.AbstractAction;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClickAction extends AbstractAction {

    public ClickAction(ElementContainer element, Browser browser) {
        super(element, browser);
    }

    public void execute() {
        browser.scrollToElement(element.getWrappedElement());
        super.execute(() -> element.getWrappedElement().click());
        log.info("[ACTION] Clicked content of element: " + element.getName());
    }
}
