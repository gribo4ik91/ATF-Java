package com.example.ui.action;

import com.example.ui.core.AbstractAction;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetTextAction extends AbstractAction {

    public GetTextAction(ElementContainer element, Browser browser) {
        super(element, browser);
    }

    public String execute() {
        return super.execute(() -> {
            var text = this.element.getWrappedElement().getText();
            log.info("[ACTION] Got text " + text + " from element. ");
            return text;
        });
    }

}
