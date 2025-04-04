package com.example.ui.core;

import com.example.ui.core.browser.Browser;
import org.openqa.selenium.support.PageFactory;

import org.openqa.selenium.support.ui.ExpectedCondition;

public class AbstractPage {

    public AbstractPage(Browser browser) {
        browser.waitPageToLoad(getWaitCondition());
        PageFactory.initElements(new MyFieldDecorator(browser), this);

    }


    public ExpectedCondition<Boolean> getWaitCondition() {
        return null;
    }

}
