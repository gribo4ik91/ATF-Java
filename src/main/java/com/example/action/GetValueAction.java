package com.example.action;


import com.example.ui.core.AbstractAction;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetValueAction extends AbstractAction {

    public GetValueAction(ElementContainer element, Browser browser) {
        super(element, browser);
    }

    public String execute() {
        return super.execute(() -> {
            String value = element.getValue();
            log.info("[ACTION] Got value " + value + " from element: " + element.getName());
            return value;
        });
    }

}
